package com.myorg;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

public class MyPipelineApp {

    public static void main(final String[] args) {
        App app = new App();

        new MyPipelineStack(app, "PipelineStack", StackProps.builder()
                .env(Environment.builder()
                             .account("266735842067")
                             .region("eu-west-1")
                             .build())
                .build());

        app.synth();
    }
}
