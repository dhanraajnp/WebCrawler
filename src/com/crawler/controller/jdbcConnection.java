package com.crawler.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class jdbcConnection {
			
	public static Connection getConn() {
		try
		{
			Class.forName("com.mysql.jdbc.Driver");  		
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/patil1","root","Dhanu");  
			
			if(con != null){
				System.out.println("Connected!!");
			}else{
				System.out.println("Not Connected!!");
			}
			return con;  
		}
		catch(Exception e){ 
			System.out.println(e);
		}		
		return null;  		
	}
	
	public boolean saveUrl(String Url){
		
		Connection con = getConn();
		PreparedStatement stmt = null;
		String tableName = "url_Records";
	    String query = ("INSERT INTO "+tableName+" (`url`) VALUES " + "(?);");
	    //System.out.println("Query is :"+query);           
	    try {
			stmt = con.prepareStatement(query);  				
			stmt.setString(1, Url);
			stmt.execute();
			
		} catch (SQLException e) {
			System.out.println("Exception while inserting!!");
			e.printStackTrace();
			return false;
		}  
		//System.out.println("Inserted successfully!!");
	    return true;
		
	}
}