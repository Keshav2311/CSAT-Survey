<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>OTP Verification</title>
<style>
	body {
  font-family: Arial, sans-serif;
  background-color: #f4f4f4;
}

.container {
  width: 300px;
  margin: 100px auto;
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

h2 {
  margin-top: 0;
}

input[type="text"] {
  width: calc(100% - 10px);
  margin-bottom: 10px;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

button {
  background-color: #007bff;
  color: #fff;
  border: none;
  padding: 10px 20px;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #0056b3;
}

.error-message {
  color: red;
}
	
</style>
</head>
<body>
<!--  <form action="RedirectServlet" method="post">-->
<div class="container">
  <h2>Oth Code Verification</h2>
  <p></p>
  <input type="text" id="otpInput" maxlength="6">
  <button onclick="verifyOTP()">Submit</button>
  <p id="errorText" class="error-message"></p>
</div>

<script>
function verifyOTP() {
	  var otpInput = document.getElementById('otpInput').value;
	  var otpPattern = /^\d{6}$/;

	  if (!otpPattern.test(otpInput)) {
	    document.getElementById('errorText').innerText = 'Please enter a valid OTP';
	    return;
	  }

	  // Here you can add code to send the OTP to your server for verification
	  // For demo purposes, let's just log the OTP
	  console.log('Entered OTP:', otpInput);
	  window.location.href = 'ratings.jsp';
	}

</script>
</body>
</html>
	
