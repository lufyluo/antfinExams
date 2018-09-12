package com.antfin.exam.filereader;

import com.antfin.exam.filereader.services.CalculateDataService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.ExecutionException;

@SpringBootApplication
public class FilereaderApplication {
	private static CalculateDataService calculateDataService= new CalculateDataService();
	public static void main(String[] args) throws ExecutionException, InterruptedException {
		SpringApplication.run(FilereaderApplication.class, args);
		calculateDataService.method1();
		Thread.sleep(3000);
	}
}
