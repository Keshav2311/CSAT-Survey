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
import java.util.Objects;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;


@WebServlet("/QuestionServlet")
public class QuestionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Server is running...!!!!!!!!!");
		int accountId= Integer.valueOf(request.getParameter("accountId"));
		int projectId= Integer.valueOf(request.getParameter("projectId"));
		System.out.println("Account id: "+accountId);
		System.out.println("Project id: "+projectId);
		int CsatId = 0;
		try (Connection conn =
        		DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5433/csat_survey",
        				"postgres", "727601")){ 
	  
        	String projectCsatQuery ="SELECT id FROM project_csat_request_info WHERE account_id=? and project_id=?";
  
        	try(PreparedStatement stmt = conn.prepareStatement(projectCsatQuery)) { 
        		stmt.setInt(1,accountId); 
        		stmt.setInt(2,projectId); 
        		ResultSet rs = stmt.executeQuery(); 
        		if(rs.next()) { //
        			CsatId = rs.getInt("id");
        			System.out.println("CSATID: "+CsatId);
        		} 
        		
        	} 
        	
		}
		catch (SQLException e) {
	  		e.printStackTrace(); 
	  	}
		Map<String, String> map = new HashMap<String, String>();
		try (Connection connn = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5433/csat_survey","postgres", "727601")){ 
			  
			  
		      String questionsDetailsQuery ="SELECT id, questions FROM project_csat_questions WHERE project_id = ?";
		  
		      try(PreparedStatement stmt = connn.prepareStatement(questionsDetailsQuery)) { 
		    	  stmt.setInt(1,projectId); 
		    	  ResultSet rs = stmt.executeQuery(); 
		    	  while (rs.next()) { //
		    		  map.put(String.valueOf(rs.getInt("id")), rs.getString("questions")); 
		    	  } 	
		      }
		}
		catch (SQLException e) {
	  		e.printStackTrace(); 
	  	}
//		    for (Map.Entry<String, String> entry : map.entrySet()) {
//	            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
//	        }
		
        	if (map.isEmpty()) {
        		
        		
        		JSONObject jsonObject = new JSONObject(map);
    		    String orgJsonData = jsonObject.toString();
    		    //System.out.println(orgJsonData);
    		        
    		    String jsCode = "var questionsData = " + orgJsonData + ";";
    		    System.out.println(jsCode);
    		    response.setContentType("application/javascript");

    		    try (PrintWriter out = response.getWriter()) {
    		        out.println(jsCode);
    		    }
        		
//        		request.setAttribute("questions", null);
//        		RequestDispatcher rd= getServletContext().getRequestDispatcher("/index.jsp");
//    		    rd.forward(request, response);
    		    System.out.println("IF part is running!!");
        	}
        	else {
//        		Map<String, String> map = new HashMap<String, String>();
//        		try (Connection connn = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5433/csat_survey","postgres", "727601")){ 
//      			  
//      			  
//      		      String questionsDetailsQuery ="SELECT id, questions FROM project_csat_questions WHERE project_id = ?";
//      		  
//      		      try(PreparedStatement stmt = connn.prepareStatement(questionsDetailsQuery)) { 
//      		    	  stmt.setInt(1,projectId); 
//      		    	  ResultSet rs = stmt.executeQuery(); 
//      		    	  while (rs.next()) { //
//      		    		  map.put(String.valueOf(rs.getInt("id")), rs.getString("questions")); 
//      		    	  } 	
//      		      }
//      		    for (Map.Entry<String, String> entry : map.entrySet()) {
//      	            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
//      	        }
      		      
      		    JSONObject jsonObject = new JSONObject(map);
    		    String orgJsonData = jsonObject.toString();
    		    //System.out.println(orgJsonData);
    		        
    		    String jsCode = "var questionsData = " + orgJsonData + ";";
    		    System.out.println(jsCode);
    		    response.setContentType("application/javascript");

    		    try (PrintWriter out = response.getWriter()) {
    		        out.println(jsCode);
    		    }
    		    
//    	        response.setContentType("application/json");
//      		    request.setAttribute("questions", orgJsonData);
//      		    
//      		  RequestDispatcher rd= getServletContext().getRequestDispatcher("/index.jsp");
//      		  rd.forward(request, response);
      		  System.out.println("ELSE part is  running!!");
        	}
        	// query project_csat_questions based on csatId and get the questions
        	// QuestionsMap - id, questions
        	// request.setAttribute("questions", map)
        	
		
	
//		catch (SQLException e) {
//	  		e.printStackTrace(); 
//	  	}
        	
        		
	

    		   // System.out.println("Server is ofcourse running!!");
        	}
}
