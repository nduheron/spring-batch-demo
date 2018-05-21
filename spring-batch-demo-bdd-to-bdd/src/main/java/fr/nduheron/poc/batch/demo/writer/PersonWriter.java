package fr.nduheron.poc.batch.demo.writer;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import fr.nduheron.poc.batch.demo.domain.Person;

@Component
public class PersonWriter extends JdbcBatchItemWriter<Person> {
	
	@Autowired
	@Qualifier("outputDataSource")
	private DataSource dataSource;
	
	@PostConstruct
	public void init() {
		setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        setDataSource(dataSource);
        setSql("insert into people(first_name, last_name) values (:firstName, :lastName)");
    }
}
