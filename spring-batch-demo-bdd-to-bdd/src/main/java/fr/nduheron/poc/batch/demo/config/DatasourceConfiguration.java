package fr.nduheron.poc.batch.demo.config;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@ComponentScan(basePackageClasses = DefaultBatchConfigurer.class)
@Profile("!test")
public class DatasourceConfiguration {

	@Autowired
	private Environment environment;
	
	/**
	 * 
	 * @return la datasource vers le schéma spring-batch
	 */
	@Bean(name = "batchDataSource")
	@Primary
	public DataSource batchDataSource() {
		return buildDataSource("datasource.springbatch");
	}

	/**
	 * 
	 * @return la datasource du reader
	 */
	@Bean(name = "inputDataSource")
	public DataSource inputDataSource() {
		return buildDataSource("datasource.input");
	}
	
	/**
	 * 
	 * @return la datsource du writer
	 */
	@Bean(name = "outputDataSource")
	public DataSource outputDataSource() {
		return buildDataSource("datasource.output");
	}
	
	/**
	 * 
	 * @return la gestion des transaction du writer
	 */
	@Bean(name = "outputTransactionManager")
	public PlatformTransactionManager outputTransactionManager() {
		return new DataSourceTransactionManager(outputDataSource());
	}
	
	private DataSource buildDataSource(String prefix) {
		HikariDataSource dataSource = new HikariDataSource();
		dataSource.setAutoCommit(false);
		dataSource.setJdbcUrl(environment.getRequiredProperty(prefix + ".url"));
		dataSource.setUsername(environment.getRequiredProperty(prefix + ".username"));
		dataSource.setPassword(environment.getRequiredProperty(prefix + ".password"));
		dataSource.setDriverClassName(environment.getRequiredProperty(prefix + ".driver-class-name"));
		return dataSource;
	}
}
