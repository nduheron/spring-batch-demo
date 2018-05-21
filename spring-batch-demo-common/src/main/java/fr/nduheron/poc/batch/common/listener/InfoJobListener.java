package fr.nduheron.poc.batch.common.listener;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.stereotype.Component;

/**
 * Listener permettant de logguer le temps d'exécution du batch
 */
@Component
public class InfoJobListener implements JobExecutionListener {

	private LocalDateTime startTime, stopTime;

	private static final Logger LOG = LoggerFactory.getLogger(InfoJobListener.class);

	@Override
	@BeforeJob
	public void beforeJob(JobExecution jobExecution) {
		startTime = LocalDateTime.now();
	}

	@Override
	@AfterJob
	public void afterJob(JobExecution jobExecution) {
		stopTime = LocalDateTime.now();

		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			LOG.info("Job completed successfully");
		} else if (jobExecution.getStatus() == BatchStatus.FAILED) {
			LOG.warn("Job failed");
		}
		LOG.info("Total time take in millis: " + getTimeInMillis(startTime, stopTime));
	}

	private long getTimeInMillis(LocalDateTime start, LocalDateTime stop) {
		return stop.toInstant(ZoneOffset.UTC).toEpochMilli() - start.toInstant(ZoneOffset.UTC).toEpochMilli();
	}

}
