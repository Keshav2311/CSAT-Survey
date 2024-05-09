package com.impelsys.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

@WebServlet("/AccountServlet")

public class AccountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		  int accountId = Integer.parseInt(request.getParameter("accountId"));
		  System.out.println("Account Id is: "+accountId);	
		 
		  Map<String, String> map = new HashMap<>();
		  try (Connection conn = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5433/csat_survey","postgres", "727601")){ 
			  
			  String projectQuery ="SELECT p.id, p.project_name FROM project_info p, account_info a WHERE a.id = p.account_id and a.id = ?";
		      String userDetailsQuery ="SELECT u.user_name, u.email FROM Account_info a left join user_info u on a.account_head_id = u.id WHERE a.id = ?";
		  
		      try(PreparedStatement stmt = conn.prepareStatement(projectQuery)) { 
		    	  stmt.setInt(1,accountId); 
		    	  ResultSet rs = stmt.executeQuery(); 
		    	  while (rs.next()) { //
		    		  map.put(String.valueOf(rs.getInt("id")),
		    		  rs.getString("project_name")); 
		    	  } 	
		      }
		      
		       
		      try(PreparedStatement stmt = conn.prepareStatement(userDetailsQuery)) { 
		    	  stmt.setInt(1,accountId); 
		    	  ResultSet rs = stmt.executeQuery(); 
		    	  while (rs.next()) { //
		    		  map.put("user_name",rs.getString("user_name"));
		    		  map.put("email",rs.getString("email"));
		    	  } 
		      }
		        	 
		        	 
		  
		   	} 
		  	catch (SQLException e) {
		  		e.printStackTrace(); 
		  	} 
		        
		    JSONObject jsonObject = new JSONObject(map);
		    String orgJsonData = jsonObject.toString();
		    //System.out.println(orgJsonData);
		        
	        response.setContentType("application/json");
		    request.setAttribute("projects", orgJsonData);
					  
		    RequestDispatcher rd= getServletContext().getRequestDispatcher("/index.jsp");
		    rd.forward(request, response);
		        
	}
}

