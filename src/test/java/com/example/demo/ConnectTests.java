package com.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConnectTests {

	@Test
	public void testConnection() throws Exception {
		Class.forName("oracle.jdbc.driver.OracleDriver");
		
		//Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "webdb", "webdb");
		Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "webdb", "webdb");
		
		Assertions.assertNotNull(connection);
		connection.close();
	}
}
