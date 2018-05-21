package fr.nduheron.poc.batch.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.converter.DefaultJobParametersConverter;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJvmExitCodeMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

public class SpringBatchUtils {
	private static final Logger LOG = LoggerFactory.getLogger(SpringBatchUtils.class);

	/**
	 * Méthode permettant de lancer un batch spring batch
	 * @param springBootClass la classe "spring boot" contenant la configuration spring du batch à lancer
	 * @param args les paramètres du batch
	 * @return le code de sortie (O = OK, 1 = Echec)
	 */
	public static int run(Class<?> springBootClass, String[] args) {
		SpringApplication app = new SpringApplication(springBootClass);
		app.setWebApplicationType(WebApplicationType.NONE);
		ConfigurableApplicationContext ctx = app.run(args);
		JobLauncher jobLauncher = ctx.getBean(JobLauncher.class);
		JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
		jobParametersBuilder.addLong("time", System.currentTimeMillis());
		jobParametersBuilder.addJobParameters(new DefaultJobParametersConverter()
				.getJobParameters(StringUtils.splitArrayElementsIntoProperties(args, "=")));
		JobParameters jobParameters = jobParametersBuilder.toJobParameters();
		Job job = (Job) ctx.getBean(jobParameters.getString("-jobName"));

		try {
			JobExecution jobExecution = jobLauncher.run(job, jobParameters);

			return new SimpleJvmExitCodeMapper().intValue(jobExecution.getExitStatus().getExitCode());

		} catch (Exception e) {
			LOG.error("Une erreur est survenue lors de l'exécution du batch.", e);
			return 1;
		}
	}

}
