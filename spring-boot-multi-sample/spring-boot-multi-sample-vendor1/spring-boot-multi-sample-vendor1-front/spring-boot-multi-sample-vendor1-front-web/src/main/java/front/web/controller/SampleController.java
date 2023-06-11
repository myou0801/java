package front.web.controller;

import java.io.IOException;
import java.io.OutputStream;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.s3.AmazonS3;

@RestController
@RequestMapping("sample")
public class SampleController {

	private static Logger logger = LoggerFactory.getLogger(SampleController.class);

	@Inject
	private AmazonS3 amazonS3;

	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	private ApplicationContext applicationContext;

	@GetMapping("hello")
	public String showHello() throws IOException {

		Resource resource = this.resourceLoader.getResource("s3://sample-bucket/test.log");
		WritableResource writableResource = (WritableResource) resource;
		try (OutputStream outputStream = writableResource.getOutputStream()) {
			outputStream.write("test".getBytes());
		}

		//		PathMatchingSimpleStorageResourcePatternResolver resourcePatternResolver = new PathMatchingSimpleStorageResourcePatternResolver(
		//				amazonS3, applicationContext);
		//		
		//		Resource resource = resourcePatternResolver.getResource("s3://sample-bucket/sample.log");
		//		
		//		logger.info(String.valueOf(resource.exists()));

		//		logger.info(amazonS3.getBucketLocation("sample-bucket"));

		return "Hello";
	}

}
