package fr.nduheron.poc.batch.demo.reader;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import fr.nduheron.poc.batch.demo.domain.Person;

@Component
public class PersonReader extends JdbcCursorItemReader<Person> {

	@Autowired
	@Qualifier("inputDataSource")
	private DataSource dataSource;

	@PostConstruct
	public void init() {
        setDataSource(dataSource);
        setRowMapper(new BeanPropertyRowMapper<>(Person.class));
        setSql("SELECT first_name, last_name FROM people");
	}
	
}
