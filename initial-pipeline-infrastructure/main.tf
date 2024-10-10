terraform {
  backend "s3" {
    bucket = "ivkomi-evolution-board-game-bucket"
    key    = "initial-pipeline-state"
    region = "eu-west-1"
  }
}

resource "aws_s3_bucket" "application_pipeline_artifacts" {
  bucket_prefix = "my-pipeline-artifacts-bucket"
  acl    = "private"
}

resource "aws_codestarconnections_connection" "application_github_connection" {
  name          = "application-example-connection"
  provider_type = "GitHub"
}

resource "aws_iam_role" "application_codepipeline_role" {
  name = "application-codepipeline-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Effect = "Allow",
        Principal = {
          Service = "codepipeline.amazonaws.com"
        },
        Action = "sts:AssumeRole"
      }
    ]
  })
}

module "codepipeline_policy" {
  source = "./codepipeline_policy"

  pipeline_artifacts_s3_bucket_arn = aws_s3_bucket.application_pipeline_artifacts.arn
  #codebuild_project_name = aws_codebuild_project.terraform_build.name
  github_codestar_connection_arn = aws_codestarconnections_connection.application_github_connection.arn
}

resource "aws_iam_role_policy_attachment" "codepipeline_policy_attach" {
  role       = aws_iam_role.application_codepipeline_role.name
  policy_arn = module.codepipeline_policy.policy_arn
}

resource "aws_codepipeline" "application_pipeline" {
  name     = "terraform-pipeline"
  role_arn = aws_iam_role.application_codepipeline_role.arn

  artifact_store {
    location = aws_s3_bucket.application_pipeline_artifacts.bucket
    type     = "S3"
  }

  stage {
    name = "Source"

    action {
      name             = "Source"
      category         = "Source"
      owner            = "AWS"
      provider         = "CodeStarSourceConnection"
      version          = "1"
      output_artifacts = ["source_output"]

      configuration = {
        ConnectionArn    = aws_codestarconnections_connection.application_github_connection.arn
        FullRepositoryId = "zierg/test-aws-pipeliine"
        BranchName       = "main"
      }
    }
  }
}