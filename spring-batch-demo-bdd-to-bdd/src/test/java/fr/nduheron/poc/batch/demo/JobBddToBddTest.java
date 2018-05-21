package fr.nduheron.poc.batch.demo;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;

import fr.nduheron.poc.batch.common.test.dbunit.AutoCommitTransactionDatabaseLookup;
import fr.nduheron.poc.batch.demo.config.DBUnitConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Import({ DBUnitConfiguration.class, JobLauncherTestUtils.class })
@ActiveProfiles("test")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class, DbUnitTestExecutionListener.class })
@DbUnitConfiguration(databaseConnection = "dbUnitDatabaseConnection", databaseOperationLookup = AutoCommitTransactionDatabaseLookup.class)
public class JobBddToBddTest {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Autowired
	@Qualifier("jobBddToBdd")
	private Job jobBddToBdd;

	@DatabaseSetup(value = "/datasets/people.xml", type = DatabaseOperation.REFRESH)
	@DatabaseTearDown(value = "/datasets/people.xml", type = DatabaseOperation.DELETE)
	@Test
	public void test() throws Exception {
		JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
		jobParametersBuilder.addLong("time", System.currentTimeMillis());

		JobExecution jobExecution = jobLauncherTestUtils.getJobLauncher().run(jobBddToBdd,
				jobParametersBuilder.toJobParameters());

		BatchStatus batchStatus = jobExecution.getStatus();
		assertEquals(BatchStatus.COMPLETED, batchStatus);

		ExitStatus exitStatus = jobExecution.getExitStatus();
		assertEquals("COMPLETED", exitStatus.getExitCode());
	}

}
