package com.txst.restapi.lib;

import java.sql.*;

public class DBModel {

	private static Connection connection = null;
	private static Statement statement = null;
	private static PreparedStatement preparedStatement = null;

	protected static Connection getConnection() throws Exception {
		if (connection == null) {
			Class.forName("com.mysql.jdbc.Driver");  
			connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/paparazzi_db","root","password");  
		}
		return connection;
	}

	public static ResultSet executeQuery(String queryString) throws Exception {
		statement = getConnection().createStatement();
		ResultSet resultSet = statement.executeQuery(queryString);
		return resultSet;
	}

	public static void close() {
		try {
			if (connection != null) {
				connection.close();
				connection = null;
			}

			if (statement != null) {
				statement.close();
				statement = null;
			}

			if (preparedStatement != null) {
				preparedStatement.close();
				preparedStatement = null;
			}
		} catch (Exception e) {
			System.out.println("There is a problem closing Database Manager");
		}
	}

//	public String getUsername(long userId) {
//		String userName = "";
//
//		try {
//
//			getConnection();
//
//			Statement stmt=connection.createStatement();
//			ResultSet rs=stmt.executeQuery("select * from login_table");
//
//
//			while(rs.next()) {
//				if (userId == rs.getInt(1)) {
//					userName = rs.getString(2);
//					break;
//				}
//			}
//
//		} catch (Exception e) {
//			System.out.println(e);
//		}
//
//		return userName;
//	}
}