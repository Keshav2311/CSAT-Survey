package com.impelsys.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

@WebServlet("/RatingsServlet")
public class RatingsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int projectId = Integer.parseInt(request.getParameter("projectId"));
		System.out.println("Account Id is: "+projectId);
		
		
		Map<Integer, String> map = new HashMap<>();
		  try (Connection conn = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5433/csat_survey","postgres", "727601")){ 
			  
			  String projectQuery ="SELECT id FROM project_csat_request_info WHERE project_id = ?";
		      //String userDetailsQuery ="SELECT u.user_name, u.email FROM Account_info a left join user_info u on a.account_head_id = u.id WHERE a.id = ?";
		  
		      try(PreparedStatement stmt = conn.prepareStatement(projectQuery)) { 
		    	  stmt.setInt(1,projectId); 
		    	  ResultSet rs = stmt.executeQuery(); 
		    	  if (rs.next()) { //
		    		  map.put(1,String.valueOf(rs.getInt("id"))); 
		    	  } 	
		      }
		  }
		  catch (SQLException e) {
				e.printStackTrace(); 
			}
		  
		  try (Connection conn = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5433/csat_survey","postgres", "727601")){ 
			  
			  String ratingsQuery ="SELECT id, questions FROM project_csat_questions WHERE project_id = ?";
		      //String userDetailsQuery ="SELECT u.user_name, u.email FROM Account_info a left join user_info u on a.account_head_id = u.id WHERE a.id = ?";
		  
		      try(PreparedStatement stmt = conn.prepareStatement(ratingsQuery)) { 
		    	  stmt.setInt(1,projectId); 
		    	  ResultSet rs = stmt.executeQuery(); 
		    	  while (rs.next()) { //
		    		  map.put(rs.getInt("id"),rs.getString("questions")); 
		    	  } 	
		      }
		  }
		  catch (SQLException e) {
			  e.printStackTrace(); 
		  }
		  for (Map.Entry<Integer, String> entry : map.entrySet()) {
			  System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
		  }
		  
		  JSONObject jsonObject = new JSONObject(map);
		  String orgJsonData = jsonObject.toString();
		  //System.out.println(orgJsonData);
		        
		  String jsCode = "var questionsData = " + orgJsonData + ";";
		  System.out.println(jsCode);
		  response.setContentType("application/javascript");

		  try (PrintWriter out = response.getWriter()) {
			  out.println(jsCode);
		  }
	}
}
