package fr.nduheron.poc.batch.demo.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.dataset.Column;
import org.dbunit.dataset.filter.IColumnFilter;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.ext.mysql.MySqlMetadataHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.springtestdbunit.bean.DatabaseConfigBean;
import com.github.springtestdbunit.bean.DatabaseDataSourceConnectionFactoryBean;
import com.google.common.collect.Sets;

@Configuration
public class DBUnitConfiguration {

	@Autowired
	@Qualifier("inputDataSource")
	private DataSource dataSource;

	@Bean
	public DatabaseConfigBean dbUnitDatabaseConfig() {
		DatabaseConfigBean dbConfig = new DatabaseConfigBean();
		dbConfig.setDatatypeFactory(new MySqlDataTypeFactory());
		dbConfig.setMetadataHandler(new MySqlMetadataHandler());
		dbConfig.setPrimaryKeyFilter(new InputDbPrimaryKeyFilter());
		return dbConfig;
	}

	@Bean
	public DatabaseDataSourceConnection dbUnitDatabaseConnection() throws Exception {
		DatabaseDataSourceConnectionFactoryBean dbConnection = new DatabaseDataSourceConnectionFactoryBean(dataSource);
		dbConnection.setDatabaseConfig(dbUnitDatabaseConfig());
		return dbConnection.getObject();
	}

	private class InputDbPrimaryKeyFilter implements IColumnFilter {
		private Map<String, Set<String>> pseudoKey = new HashMap<>();

		public InputDbPrimaryKeyFilter() {
			pseudoKey.put("people", Sets.newHashSet("first_name", "last_name"));
		}

		public boolean accept(String tableName, Column column) {
			return pseudoKey.get(tableName.toLowerCase()).contains(column.getColumnName().toLowerCase());
		}

	}

}