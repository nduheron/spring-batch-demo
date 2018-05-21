package fr.nduheron.poc.batch.demo.config;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@ComponentScan(basePackageClasses = DefaultBatchConfigurer.class)
@Profile("test")
public class TestDatasourceConfiguration {

	/**
	 * 
	 * @return la datasource vers le schéma spring-batch
	 */
	@Bean(name = "batchDataSource")
	@Primary
	public DataSource batchDataSource() {
		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		return builder.setType(EmbeddedDatabaseType.H2).setName("springbatch")
				.addScript("classpath:/org/springframework/batch/core/schema-h2.sql").build();
	}

	/**
	 * 
	 * @return la datasource du reader
	 */
	@Bean(name = "inputDataSource")
	public DataSource inputDataSource() {
		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		return builder.setType(EmbeddedDatabaseType.H2).setName("input").addScript("classpath:/scripts/schema.sql").build();
	}

	/**
	 * 
	 * @return la datsource du writer
	 */
	@Bean(name = "outputDataSource")
	public DataSource outputDataSource() {
		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		return builder.setType(EmbeddedDatabaseType.H2).setName("output").addScript("classpath:/scripts/schema.sql").build();
	}

	/**
	 * 
	 * @return la gestion des transaction du writer
	 */
	@Bean(name = "outputTransactionManager")
	public PlatformTransactionManager outputTransactionManager() {
		return new DataSourceTransactionManager(outputDataSource());
	}

}
