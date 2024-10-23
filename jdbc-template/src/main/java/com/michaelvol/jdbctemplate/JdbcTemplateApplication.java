package com.michaelvol.jdbctemplate;

import com.michaelvol.jdbctemplate.relationaldataaccess.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class JdbcTemplateApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(JdbcTemplateApplication.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run(JdbcTemplateApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        log.info("Creating tables");

        jdbcTemplate.execute("DROP TABLE CUSTOMERS IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE CUSTOMERS(" + "ID SERIAL, FIRST_NAME VARCHAR(255), LAST_NAME VARCHAR(255))");
        List<Object[]> splitUpNames = Arrays.asList("John Woo", "Jeff Dean", "Josh Bloch", "Josh Long").stream()
                                            .map(name -> name.split(" "))
                                            .collect(Collectors.toList());
        splitUpNames.forEach(name -> log.info(String.format("Inserting customer record for %s %s", name[0], name[1])));
        jdbcTemplate.batchUpdate("INSERT INTO CUSTOMERS(FIRST_NAME, LAST_NAME) VALUES(?, ?)", splitUpNames);
        jdbcTemplate.query("SELECT * FROM CUSTOMERS WHERE FIRST_NAME = ?", (rs, rowNum) ->
                            new Customer(rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name")), "Josh")
                    .forEach(customer -> log.info(customer.toString()));
    }


}
