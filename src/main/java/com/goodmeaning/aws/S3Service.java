package com.goodmeaning.aws;


import java.io.ByteArrayInputStream;

import java.io.File;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;

import com.amazonaws.util.IOUtils;

import lombok.Setter;


//spring이 생성자를 호출 -> 인스턴스화 한 후에 -> @Value 실행 
@Service
public class S3Service {
	  @Autowired
	  final private AmazonS3 s3Client;

	  String bucketName;
	 
	  String accessKey ;
	  
	  String secretKey ;

	  public S3Service(@Value("${cloud.aws.s3.bucket}") String bucketName, 
			  @Value("${cloud.aws.credentials.accessKey}") String accessKey, 
			  @Value("${cloud.aws.credentials.secretKey}") String secretKey) {
		this.bucketName = bucketName;
		this.accessKey = accessKey;
		this.secretKey = secretKey;
		System.out.println("accessKey: " + accessKey);
	    AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

	    this.s3Client = AmazonS3ClientBuilder.standard()
	                        .withCredentials(new AWSStaticCredentialsProvider(credentials))
	                        .withRegion(Regions.AP_NORTHEAST_2)
	                        .build();
	  }

	  // upload original image file
	  public void uploadFile(MultipartFile image, String s3Path) {
	    try {
	      // set ObjectMatadata
	      byte[] bytes = IOUtils.toByteArray(image.getInputStream());

	      ObjectMetadata objectMetadata = new ObjectMetadata();
	      objectMetadata.setContentLength(bytes.length);
	      objectMetadata.setContentType(image.getContentType());

	      // save in S3
	      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
	      this.s3Client.putObject(this.bucketName, s3Path.replace(File.separatorChar, '/'), 
	    		  									byteArrayInputStream, objectMetadata);
		  
	      byteArrayInputStream.close();
	      
	      }catch(Exception e){
	    	  e.printStackTrace();
	      }
	    }

  
}