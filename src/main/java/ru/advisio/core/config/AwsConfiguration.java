package ru.advisio.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Configuration
@PropertySource("secrets.properties")
public class AwsConfiguration {
    @Value("${aws.accessKeyId}")
    private String key;

    @Value("${aws.secret}")
    private String secret;

    @Bean
    public S3Client s3Client(){
        Region region = Region.US_WEST_2;
        AwsCredentials awsCredentials = AwsBasicCredentials.create(key, secret);
        return S3Client.builder()
                .region(region)
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                .endpointOverride(URI.create("https://hb.bizmrg.com"))
                .build();
    }
}
