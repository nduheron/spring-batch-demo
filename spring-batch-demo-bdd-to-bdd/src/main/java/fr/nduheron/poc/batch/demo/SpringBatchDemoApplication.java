package fr.nduheron.poc.batch.demo;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

import fr.nduheron.poc.batch.common.utils.SpringBatchUtils;

@SpringBootApplication(scanBasePackages = "fr.nduheron.poc.batch") // on inclus les packages du projet common
@EnableBatchProcessing
@ImportResource("classpath:batch-jobs/*.xml") // on ajoute la configuration xml du batch
public class SpringBatchDemoApplication {

	public static void main(String[] args) {
		System.exit(SpringBatchUtils.run(SpringBatchDemoApplication.class, args));
	}
}
