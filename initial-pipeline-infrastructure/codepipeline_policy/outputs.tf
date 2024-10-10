output "policy_arn" {
  value       = aws_iam_policy.codepipeline_policy.arn
  description = "ARN of the created IAM Policy"
}