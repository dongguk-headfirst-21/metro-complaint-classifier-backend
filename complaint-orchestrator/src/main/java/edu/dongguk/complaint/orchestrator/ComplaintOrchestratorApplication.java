package edu.dongguk.complaint.orchestrator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ComplaintOrchestratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComplaintOrchestratorApplication.class, args);
	}

}
