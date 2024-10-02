package com.myorg;


import software.amazon.awscdk.Stage;
import software.amazon.awscdk.StageProps;
import software.amazon.awscdk.services.codebuild.Project;
import software.amazon.awscdk.services.codepipeline.Artifact;
import software.amazon.awscdk.services.codepipeline.actions.CodeBuildAction;
import software.constructs.Construct;

import java.util.List;

public class BuildLambdasStage extends Stage {

    public BuildLambdasStage(final Construct scope, final String id, final StageProps props, Project buildLambdasProject) {
        super(scope, id, props);

        Artifact input = Artifact.artifact("zierg_test_aws_pipeliine_Source");
        Artifact output = Artifact.artifact("lambda_jars");
        this.output = output;

        CodeBuildAction.Builder.create()
                .actionName("BuildLambdas")
                .input(input)
                .outputs(List.of(output))
                .project(buildLambdasProject)
                .build();
    }

    public Artifact getOutput() {
        return output;
    }

    private final Artifact output;
}
