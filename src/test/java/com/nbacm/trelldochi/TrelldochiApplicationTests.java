package com.nbacm.trelldochi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@EnableAutoConfiguration
@TestPropertySource(properties = {
        "cloud.aws.credentials.access-key=${AWS_ACCESS_KEY:dummyAccessKey}",
        "cloud.aws.credentials.secret-key=${AWS_SECRET_KEY:dummySecretKey}",
        "cloud.aws.region.static=${AWS_REGION:us-east-1}",
        "cloud.aws.s3.bucket=${AWS_S3_BUCKET:dummyBucket}",
        "slack.webhook-url=${SLACK_WEBHOOK_URL:http://dummy-webhook-url}"

})
class TrelldochiApplicationTests {

    @Test
    void contextLoads() {



    }

}
