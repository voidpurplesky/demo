package com.example.demo;

import java.sql.Connection;
import java.sql.SQLException;


import javax.sql.DataSource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.Cleanup;
import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class DataSourceTests {

	@Autowired
	private DataSource dataSource;
	
	@Test
	public void testConnection() throws SQLException {
		
		@Cleanup
		Connection con = dataSource.getConnection();
		
		log.info(con);
//2024-08-08T17:44:00.156+09:00  INFO 7856 --- [demo] [           main] com.example.demo.DataSourceTests         : HikariProxyConnection@1193430365 wrapping oracle.jdbc.driver.T4CConnection@6684f7f2
		Assertions.assertNotNull(con);
	}
}
