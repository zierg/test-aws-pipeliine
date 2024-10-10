data "aws_region" "current" {}
data "aws_caller_identity" "current" {}
data "aws_partition" "current" {}

resource "aws_iam_policy" "codepipeline_policy" {
  name        = "application-codepipeline-policy"
  description = "Policy for CodePipeline to access S3 and CodeBuild"

  policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Effect = "Allow",
        Action = [
          "s3:GetObject",
          "s3:PutObject"
        ],
        Resource = "${var.pipeline_artifacts_s3_bucket_arn}/*"
      },
	#  {
	#	"Effect": "Allow",
	#	"Action": [
	#	  "codebuild:BatchGetBuilds",
	#	  "codebuild:StartBuild",
	#	  "codebuild:BatchGetProjects"
	#	],
	#	"Resource": "arn:aws:codebuild:${data.aws_region.current.id}:${data.aws_caller_identity.current.account_id}:project/${var.codebuild_project_name}*"
	#},
	#{
    #  "Effect": "Allow",
    #  "Action": [
    #    "codebuild:CreateReportGroup",
    #    "codebuild:CreateReport",
    #    "codebuild:UpdateReport",
    #    "codebuild:BatchPutTestCases"
    #  ],
    #  "Resource": "arn:aws:codebuild:${data.aws_region.current.id}:${data.aws_caller_identity.current.account_id}:report-group/${var.codebuild_project_name}*"
    #},
		
    {
      "Effect": "Allow",
      "Action": [
        "logs:CreateLogGroup",
        "logs:CreateLogStream",
        "logs:PutLogEvents"
      ],
      "Resource": "arn:${data.aws_partition.current.partition}:logs:${data.aws_region.current.id}:${data.aws_caller_identity.current.account_id}:log-group:*"
    },
    {
      "Effect": "Allow",
      "Action": [
        "codestar-connections:UseConnection"
      ],
      "Resource": var.github_codestar_connection_arn
    }
    ]
  })
}