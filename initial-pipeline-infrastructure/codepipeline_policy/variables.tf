variable "pipeline_artifacts_s3_bucket_arn" {
  description = "ARN of the bucket for storing artifacts"
  type        = string
}

#variable "codebuild_project_name" {
#  description = "Name of the Codebuild project that executes terraform apply"
#  type        = string
#}

variable "github_codestar_connection_arn" {
    description = "ARN of the connection to the Github Repository that would triggers the pipeline"
    type        = string
}
