package com.Seo.SeoHotel.service;

import com.Seo.SeoHotel.exception.OurException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class AwsS3Service {
    private final String bucketName = "seo-hotel-images";

    @Value("${aws.s3.secret.key}")
    private String awsS3SecretKey;

    @Value("${aws.s3.access.key}")
    private String awsS3AccessKey;

    // method save image to s3
    public String saveImageToS3(MultipartFile photo) {
        String s3LocationImage = null;

        try {
            // get original filename
            String s3FileName = photo.getOriginalFilename();

            // create aws credential using access and secret keys
            BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsS3AccessKey, awsS3SecretKey);

            // build an amazon s3 client with credential and region
            AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .withRegion(Regions.AP_SOUTHEAST_2)
                    .build();

            // obtain an inputstream from photo file upload
            InputStream inputStream = photo.getInputStream();

            // create metadata for object -> jpeg image
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/jpeg");

            // create putObjectRequest
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, s3FileName, inputStream, metadata);

            // upload file to the s3
            amazonS3.putObject(putObjectRequest);

            // return url of uploaded image
            return "https://" + bucketName + ".s3.amazonaws.com/" + s3FileName;

        } catch (Exception e) {
            e.printStackTrace();
            throw new OurException("Unable to upload image to s3 bucket. " + e.getMessage());

        }
    }
}
