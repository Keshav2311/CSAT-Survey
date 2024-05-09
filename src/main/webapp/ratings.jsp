<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
  <%@ page import = "java.io.*,java.util.*,java.sql.*"%>
<%@ page import = "javax.servlet.http.*,javax.servlet.*" %>

     
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/sql" prefix = "sql"%>

   
<!doctype html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>CSAT Ratings</title>
     <style>
        /* Define your CSS styles here */
        body {
            background-color: #f0f0f0; /* Set the background color here */
            font-family: Arial, sans-serif;
            margin: 0; /* Optional: Remove default margin */
            padding: 0; /* Optional: Remove default padding */
        }

        /* Example of CSS styles for specific elements */
        #container {
            margin-bottom: 10px;
        }

        input[type="text"],
        input[type="number"] {
            width: 300px;
            padding: 5px;
            margin-bottom: 5px;
        }

        #questions h4 {
            font-size: 18px;
            color: #333;
        }
		
        #ratings input[type="text"] {
            width: 400px;
            height: 100px;
            padding: 10px;
            margin-bottom: 10px;
            resize: vertical;
            overflow-wrap: break-word;
        }

        input[type="submit"] {
            background-color: #007bff;
            color: #fff;
            border: none;
            padding: 10px 20px;
            cursor: pointer;
            border-radius: 5px;
        }

        input[type="submit"]:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>

	<sql:setDataSource var = "options" driver = "org.postgresql.Driver" url = "jdbc:postgresql://127.0.0.1:5433/csat_survey" user = "postgres"  password = "727601"/>

         <sql:query dataSource = "${options}" var = "option">
            SELECT * from project_info;
         </sql:query>
	
  	<form action="SaveServlet" method="post">
  		<table border = "2" align="center" width="50%">
  			<tr>
  				<th align="center" bgcolor="grey" style="color: white" colspan="6"><h1>CSAT Survey Ratings Page</h1></th>
  			</tr>
  			<tr>
  				<th align="right" id="q">Project: </th>
  				
  					<td>
  						<select name="projectId" id="projectId" onchange="getRatings()">

  							<option value="0">Select Project</option>
  							<c:forEach items="${option.rows}"
  								var="row">
  								<option value="${row.id}">
  									${row.project_name}
  								</option>
  							</c:forEach>
  						</select>
  					</td>
  				
  			</tr>
  			
  			<tr>
  				<th align="right">Customer Id: </th>
  				<td>
  					<input type="text" id="customerId" name="customerId" >
  				</td>
  			</tr>
  			
  			
  		</table>
  	
  		<table border = "2" align="center" width="50%">
  			<tr>
  				<th align="center" bgcolor="grey" style="color: white" colspan="6"><h1>Ratings</h1></th>
  			</tr>
  			<tr>
  				<th> <h4>Questions</h4></th>
  					<td>
  						<div id="container" name="container"></div>
  					</td>
  				
  			</tr>
  			<tr>	
  				<th><div id="questions" name="questions"></div></th>
  			
  				<td><div id="ratings" name="ratings"></div></td>
  			</tr>
  			<tr>
  				<td colspan="3" align="center">
  					<input type= "submit" value = "SAVE" >
  				</td>
  			</tr>
  		</table>
  	</form>
  	<script>
  	function getRatings(){
  		var projectId = document.getElementById('projectId').value;
  	    console.log(projectId);
  	    if (projectId != 0) {
  	        var xhr = new XMLHttpRequest();
  	        console.log(xhr);
  	        xhr.onreadystatechange = function() {
  	            if (xhr.readyState == XMLHttpRequest.DONE) {
  	                if (xhr.status == 200) {
  	                    handleFirstResponse(xhr.responseText);
  	                } 
  	            }
  	        };
  	        
  	        xhr.open("GET", "RatingsServlet?projectId=" + projectId, true);
  	        xhr.send();
  	    }
  	}
  	function handleFirstResponse(responseText){
  		console.log(responseText);
  		eval(responseText);
  		
  		document.getElementById("customerId").value = questionsData[1];
  		
  		delete questionsData[1];
  		
  		
  		for (var key in questionsData) {
  		    if (questionsData.hasOwnProperty(key)) {
  		        var questionId = key;
  		        var question = questionsData[key];
  		        createInput(questionId, question); // Call a function to create the input field
  		    }
  		}

  		function createInput(questionId, question) {
  		    var questionIdInput = document.createElement("input");
  		  	var questionInput = document.createElement("input");
  		  	var ratingInput = document.createElement("input");
  		    var brea = document.createElement("br");
  		    
  		  	questionIdInput.setAttribute("type","text");
  			questionIdInput.setAttribute("value",questionId);
  			questionIdInput.setAttribute("name","questionId");
  			questionIdInput.style.width = "40px";
  		    
  			questionInput.setAttribute("type", "text");
  			questionInput.setAttribute("value", question);
  			questionInput.setAttribute("name", "question");
  		  	//text.style.height = "100px";
    		questionInput.style.width = "300px";
    		questionInput.setAttribute("name", "question_" + questionId);
  		    // You can also set the question ID as a custom attribute if needed
  		    questionInput.setAttribute("data-question-id", questionId);
  		    
  		    
  		  	ratingInput.setAttribute("type", "number");
  			ratingInput.setAttribute("name", "rating");
  			ratingInput.setAttribute("min", "0");
  			ratingInput.setAttribute("max", "10");
  			ratingInput.style.width = "80px";
  			ratingInput.setAttribute("placeholder", "Rate");
  	    	
  	    	container.appendChild(questionIdInput);
  		    container.appendChild(questionInput);
  		    container.appendChild(ratingInput);
  		    container.appendChild(brea);
  		}
  		
  		var box = document.createElement("h4");
  		//box.setAttribute("type","text");
  		//box.setAttribute("name","feedback");
  		//box.setAttribute("value","Overall Feedback");
  		box.textContent = "Overall Feedback";
  		
  		//box.setAttribute("size", "100");
  		//questions.append(document.createElement("br"));
  		questions.appendChild(box);
  		
  		var rate = document.createElement("input");
  		rate.setAttribute("type","text");
  		rate.setAttribute("name","rate");
  		rate.style.height = "100px";
  		rate.style.width = "400px";
  		rate.style.overflowWrap = "break-word";
  		//rate.setAttribute("size","100");
  		ratings.appendChild(rate);

  	}
  	
  	
  	
  	</script>
  			
  	
 	
</body>
</html>
