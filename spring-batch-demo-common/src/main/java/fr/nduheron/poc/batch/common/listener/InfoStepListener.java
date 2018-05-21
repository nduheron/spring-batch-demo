package fr.nduheron.poc.batch.common.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.stereotype.Component;

/**
 * Listener permettant de logguer l'exécution d'une step.
 *
 */
@Component
public class InfoStepListener implements StepExecutionListener {

	private static final Logger LOG = LoggerFactory.getLogger(InfoStepListener.class);

	public void beforeStep(StepExecution stepExecution) {
	}

	@AfterStep
	public ExitStatus afterStep(StepExecution stepExecution) {

		if (!stepExecution.getFailureExceptions().isEmpty()) {
			LOG.warn("Erreur d'éxecution de l'étape : " + stepExecution.getStepName());
		}

		if (LOG.isInfoEnabled()) {
			int nbElementsLus = stepExecution.getReadCount();
			if (nbElementsLus > 0) {
				LOG.info(nbElementsLus + " éléments lus dans l'étape " + stepExecution.getStepName());
				int nbElementsIgnores = stepExecution.getSkipCount();
				if (nbElementsIgnores > 0) {
					LOG.info(nbElementsIgnores + " éléments ignorés dans l'étape " + stepExecution.getStepName());
				}
				int nbElementsEcrits = stepExecution.getWriteCount();
				LOG.info(nbElementsEcrits + " éléments écrits dans l'étape " + stepExecution.getStepName());
			}
			long endTime = System.currentTimeMillis();
			LOG.info("Fin de l'étape : " + stepExecution.getStepName() + ". Temps d'exécution : "
					+ (endTime - stepExecution.getStartTime().getTime()) + " ms.");
		}

		return stepExecution.getExitStatus();
	}
}
