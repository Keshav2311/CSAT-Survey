package com.impelsys.web;

import java.io.IOException;
import java.sql.Connection;
//import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/SubmitServlet")
public class SubmitServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	System.out.println("Server is running...!");
    	
    	
    	
    	int account_id = Integer.parseInt(request.getParameter("accountId"));
    	
        int project_id = Integer.parseInt(request.getParameter("projectId"));
        String startDateString = request.getParameter("startDate");
        String endDateString = request.getParameter("endDate");
        int customer_id = 0;
        int csat_id = 0;
        
        String []questions = request.getParameterValues("question");
        for(int i=0;i<questions.length;i++) {
        	System.out.println("Questions: "+questions[i]);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date startDate = null;
        java.util.Date endDate = null;
        try {
            startDate = dateFormat.parse(startDateString);
            endDate = dateFormat.parse(endDateString);
        } catch (Exception e) {
            e.printStackTrace(); // Handle parsing errors appropriately
        }

        // Calculate duration in milliseconds
        long durationInMillis = endDate.getTime() - startDate.getTime();

        // Convert duration to days (or any other unit as needed)
        long durationInDays = durationInMillis / (1000 * 60 * 60 * 24); // Milliseconds to days

        // Convert duration to integer (if needed)
        int durationInDaysInt = (int) durationInDays;

        try {
            String customerIdQuery = "SELECT p.id FROM project_customer_info p WHERE p.project_id = ?";
            try (Connection conn = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5433/csat_survey",
                    "postgres", "727601");
                 PreparedStatement stmt = conn.prepareStatement(customerIdQuery)) {

                stmt.setInt(1, project_id);
                java.sql.ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    customer_id = rs.getInt("id");
                }
            }

            // Perform insertion into the database
            String insertQuery = "INSERT INTO project_csat_request_info (account_id, project_id, csat_duration, customer_id) VALUES (?, ?, ?, ?)";
            try (Connection conn = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5433/csat_survey",
                    "postgres", "727601");
                 PreparedStatement stmt = conn.prepareStatement(insertQuery)) {

                stmt.setInt(1, account_id);
                stmt.setInt(2, project_id);
                stmt.setInt(3, durationInDaysInt);
                stmt.setInt(4, customer_id);

                // Execute the insertion query
                stmt.executeUpdate();
//                if (rowsInserted > 0) {
//                    // Insertion successful
//                    response.sendRedirect("/success.jsp");
//                } else {
//                    // Insertion failed
//                    response.getWriter().println("Failed to insert data into the database.");
//                }
            }
            String deleteQuery = "DELETE FROM project_csat_questions where project_id = ?";
            try (Connection conn = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5433/csat_survey","postgres","727601");
            		PreparedStatement stmt = conn.prepareStatement(deleteQuery)){
            	stmt.setInt(1,  project_id);
            	
            	int deletedRow = stmt.executeUpdate();
            	if(deletedRow > 0) {
            		System.out.println("Delete successful");
            	}
            	else
            		System.out.println("Delete unsuccessful");
            }
            String csatIdQuery = "SELECT id FROM project_csat_request_info WHERE project_id = ?";
            try (Connection conn = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5433/csat_survey",
                    "postgres", "727601");
                 PreparedStatement stmt = conn.prepareStatement(csatIdQuery)) {

                stmt.setInt(1, project_id);
                java.sql.ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    csat_id = rs.getInt("id");
                }
            }
            String insertQuestions = "INSERT INTO project_csat_questions (csat_request_id, questions, project_id) VALUES (?,?,?)";
            try (Connection conn = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5433/csat_survey",
                    "postgres", "727601");
                 PreparedStatement stmt = conn.prepareStatement(insertQuestions)) {
            	for(int i=0;i<questions.length;i++) {
            		stmt.setInt(1, csat_id);
            		stmt.setString(2, questions[i]);
            		stmt.setInt(3, project_id);
            	
            	stmt.executeUpdate();
            	System.out.println("Insert happend");
            	}
//            	if (rowsInserted > 0) {
//            	    // Insertion successful
//            	    response.sendRedirect("/success.jsp");
//            	} else {
//            	    // Insertion failed
//            	    response.getWriter().println("Failed to insert data into the database.");
//            	}
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle SQL exceptions appropriately
        }
        response.sendRedirect("/success.jsp");
    }
}
