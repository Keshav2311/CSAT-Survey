package com.impelsys.web;

import java.io.IOException;
import java.security.Timestamp;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/SaveServlet")
public class SaveServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("Server is running...!");
		
		int customerId = Integer.parseInt(request.getParameter("customerId"));
		System.out.println("Customer Id: "+customerId);
		
		//Map<Integer, String> map = new HashMap<>();
		String[] questionIdStrings = request.getParameterValues("questionId");
		int[] questionIds = new int[questionIdStrings.length];
		for (int i = 0; i < questionIdStrings.length; i++) {
			try {
				questionIds[i] = Integer.parseInt(questionIdStrings[i]);
			} catch (NumberFormatException e) {
		            // Handle parsing error
		            // For example, log the error or provide a default value
				questionIds[i] = 0; // Default value or another appropriate action
		    	}
		}   
		for(int j=0;j<questionIds.length;j++) {
			System.out.println("Question: " +questionIds[j]);
		}
		String feedback = request.getParameter("rate");
		System.out.println("Feedback: "+feedback);
		
		String[] ratingStrings = request.getParameterValues("rating");
		int[] ratings = new int[ratingStrings.length];
		  	for (int j = 0; j < ratingStrings.length; j++) {
		        try {
		            ratings[j] = Integer.parseInt(ratingStrings[j]);
		        } catch (NumberFormatException e) {
		            // Handle parsing error
		            // For example, log the error or provide a default value
		            ratings[j] = 0; // Default value or another appropriate action
		        }
		    }
		for(int k=0;k<ratings.length;k++) {
			System.out.println("Question: " +ratings[k]);
		}
		Map<Integer, Integer> questionRatingsMap = new HashMap<>();

		// Assuming questionIds and ratings arrays have been populated

		for (int i = 0; i < questionIds.length; i++) {
		    questionRatingsMap.put(questionIds[i], ratings[i]);
		}
		//Date currentDate = new Date(i);
	    
	    // Convert the java.util.Date to java.sql.Timestamp
	    //Timestamp receivedDate = new Timestamp(currentDate.getTime());
		
		
		String insert1Query = "INSERT INTO project_csat_customer_rating (csat_request_id, csat_question_id, csat_rating) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5433/csat_survey",
                "postgres", "727601");
             PreparedStatement stmt = conn.prepareStatement(insert1Query)) {

            stmt.setInt(1, customerId);
            for (Map.Entry<Integer, Integer> entry : questionRatingsMap.entrySet()) {
                int questionId = entry.getKey();
                int rating = entry.getValue();
                
                stmt.setInt(2, questionId); // Assuming the index 2 corresponds to the question ID in your PreparedStatement
                stmt.setInt(3, rating); // Assuming the index 3 corresponds to the rating in your PreparedStatement
                
                // Execute the INSERT statement for each row
                int rowsInserted = stmt.executeUpdate(); // Execute the statement
                
                // Check if insertion was successful
                if (rowsInserted > 0) {
                    // Insertion successful
                	System.out.println("Successful");
                } else {
                    // Insertion failed
                	System.out.println("UnSuccessful");
                }
            }

            // Execute the insertion query
            stmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exceptions appropriately
        }
        
        String insert2Query = "INSERT INTO project_csat_overall_feedback (csat_request_id, customer_feedback) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5433/csat_survey",
                "postgres", "727601");
             PreparedStatement stmt = conn.prepareStatement(insert2Query)) {

            stmt.setInt(1, customerId);
            stmt.setString(2, feedback);
            //stmt.setInt(3, durationInDaysInt);
            

            // Execute the insertion query
            stmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exceptions appropriately
        }
	}

}
