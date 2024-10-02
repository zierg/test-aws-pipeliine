package com.myorg;


import software.amazon.awscdk.Stage;
import software.amazon.awscdk.StageProps;
import software.amazon.awscdk.services.codebuild.BuildSpec;
import software.amazon.awscdk.services.codebuild.Project;
import software.amazon.awscdk.services.codepipeline.Artifact;
import software.amazon.awscdk.services.codepipeline.actions.CodeBuildAction;
import software.constructs.Construct;

import java.util.List;
import java.util.Map;

public class BuildLambdasStage extends Stage {
    @SuppressWarnings("unused")
    public BuildLambdasStage(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public BuildLambdasStage(final Construct scope, final String id, final StageProps props) {
        super(scope, id, props);

        Artifact input = Artifact.artifact("zierg_test_aws_pipeliine_Source");
        Artifact output = Artifact.artifact("lambda_jars");
        this.output = output;

        Map<String, ?> buildSpec = Map.of(
                "phases", Map.of(
                        "build", Map.of(
                                "commands", List.of("mvn -pl lambda package")
                        )
                )
        );

        Project buildLambdas = Project.Builder.create(this, "BuildLambdas")
                .buildSpec(BuildSpec.fromObject(buildSpec))
                .build();

        CodeBuildAction.Builder.create()
                .actionName("BuildLambdas")
                .input(input)
                .outputs(List.of(output))
                .project(buildLambdas)
                .build();
    }

    public Artifact getOutput() {
        return output;
    }

    private final Artifact output;
}
