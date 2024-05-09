import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/RedirectServlet")
public class RedirectServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve project ID from the request parameter
        int projectId = Integer.parseInt(request.getParameter("projectId"));
        
        // Generate URL and OTP based on the project ID
        String url = "https://example.com/project/" + projectId; // Replace with your actual URL
        String otp = generateOTP();
        
        // Store URL and OTP in the database
        saveURLAndOTP(projectId, url, otp);
        
        // Send a success response
        response.getWriter().write("URL and OTP stored in the database successfully.");
    }

    // Method to generate a random OTP
    private String generateOTP() {
        // Generate a 6-digit OTP
        Random random = new Random();
        int otpValue = 100000 + random.nextInt(900000);
        return String.valueOf(otpValue);
    }

    // Method to save URL and OTP in the database
    private void saveURLAndOTP(int projectId, String url, String otp) {
        // Database connection parameters
        String dbUrl = "jdbc:postgresql://127.0.0.1:5433/csat_survey";
        String dbUsername = "postgres";
        String dbPassword = "727601";

        try {
            // Load MySQL JDBC driver
            Class.forName("org.postgresql.Driver");

            // Connect to the database
            Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);

            // Prepare SQL statement to insert URL and OTP into the database
            String sql = "INSERT INTO poj_csat_status (project_id, gen_code, gen_url) VALUES (?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, projectId);
            statement.setString(2, otp);
            statement.setString(3, url);

            // Execute the SQL statement
            statement.executeUpdate();

            // Close database connection
            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
