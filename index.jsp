<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
  <%@ page import = "java.io.*,java.util.*,java.sql.*"%>
<%@ page import = "javax.servlet.http.*,javax.servlet.*" %>

     
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/sql" prefix = "sql"%>

   
<!doctype html>
<html>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<!-- <head>
	  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>CSAT Survey</title>
    <!--  <script src="handle.js"></script>
</head> -->
<body>
	<sql:setDataSource var = "options" driver = "org.postgresql.Driver" url = "jdbc:postgresql://127.0.0.1:5433/csat_survey" user = "postgres"  password = "727601"/>
	<sql:query dataSource = "${options}" var = "option">
            SELECT * from account_info;
    </sql:query>
	
  	<form action="SubmitServlet" method="post" id="questions">
  		<table border = "2" align="center" width="50%">
  			<tr>
  				<th align="center" bgcolor="grey" style="color: white" colspan="6"><h1>CSAT Survey ADMIN Page</h1></th>
  			</tr>
  			<tr>
  				<th align="right" id="q">Account: </th>
  				<td>
  					<select name="accountId" id="accountId" onchange="getProjects()">
  						<option value="0">Select Account</option>
  						<c:forEach items="${option.rows}" var="row">
  							<option value="${row.id}">${row.account_name}</option>
  						</c:forEach>
  					</select>
  				</td>	
  			</tr>
  			<tr>
  				<th align="right">Project: </th>
  				<td id="projectDropdown">
  					<select name="projectId" id="projectId" onchange="getQuestions()">
   						<option value="0">Select Project</option>
  	  				</select>
  				</td>
  			</tr>
  			<tr>
  				<th align="right" id="y">Account Head:</th>
  				<td id="head">
  					<input type="text" id="accountHead" name="accountHead" >
  				</td>			
  			</tr>
  			<tr>
  				<th align="right">Account Head Email ID:</th>
  				<td id="EmailId">
  					<input type="text" id="accountHeadEmail" name="accountHeadEmail" >
  				</td>
  			</tr>
  			<tr>
  				<th align="right">CSAT For(Duration):</th>
  				<td>
  					<label for="calendar1">Start date:</label>
    				<input type="date" id="startDate" name="startDate"><br>
  				</td>
  				<td>
  					<label for="calendar1">End date:</label>
    				<input type="date" id="endDate" name="endDate"><br>
  				</td>
  			</tr>
  			<!--  <tr>
  				<td colspan="3" align="center">
  					<input type= "submit" value = "SAVE" >
  				</td>
  			</tr>-->
  			<br><br>
  			<tr>
  				<th align="center" bgcolor="grey" style="color: white" colspan="3"><h4>Questions</h4></th>
  			</tr>
  			<tr>
  				<th align="right">*</th>
  				<td align="center">
  					<div colspan = "6" id="container" name="container"></div>
  				</td>
  				
  			</tr>
  			<tr>
  				<td colspan="3" align="center">
  					<input type= "submit" value = "SAVE" >
  				</td>
  			</tr>
  		</table>
  	</form>
  	
<script>
function getProjects() {
    var accountId = document.getElementById('accountId').value;
    console.log(accountId);
    if (accountId != 0) {
        var xhr = new XMLHttpRequest();
        console.log(xhr);
        xhr.onreadystatechange = function() {
            if (xhr.readyState == XMLHttpRequest.DONE) {
                if (xhr.status == 200) {
                    handleFirstResponse(xhr.responseText);
                } 
            }
        };
        
        xhr.open("GET", "AccountServlet?accountId=" + accountId, true);
        xhr.send();
    }

}
function handleFirstResponse(responseText) {
	console.log(responseText);
	
	var p = '${projects}';
	var pattern = /var p = '([^']*)';/;
	console.log(pattern);
	var match = pattern.exec(responseText);
	console.log(match);

	if (match && match.length >= 2) {
	    // Extract the JSON string from the matched group
	    var jsonString = match[1];
	    var projects = JSON.parse(jsonString);
	    // Now jsonString contains the JSON string stored in the p variable
	    //console.log(jsonString);
	} else {
	    console.log("JSON string not found in the response text.");
	}
	
	//var projects = JSON.parse(jsonString);
		
	console.log(projects);
	
	 document.getElementById("accountHead").value = projects["user_name"];
	 document.getElementById("accountHeadEmail").value = projects["email"];

	 delete projects["user_name"];
	 delete projects["email"];
	    
	 var dropdown = document.getElementById("projectId");
	    
	 for (var key in projects) {
	  	if (projects.hasOwnProperty(key)) {   	
	     	var option = document.createElement("option");
	        option.text = projects[key];
	        option.value = parseInt(key);
	        dropdown.appendChild(option);
	   	}
	 }
}
function getQuestions(){
	var accountId = document.getElementById('accountId').value;
	var projectId = document.getElementById('projectId').value;
	console.log(accountId);
	console.log(projectId);
	if(accountId != 0 && projectId != 0){
		var xhr = new XMLHttpRequest();
		console.log(xhr);
		xhr.onreadystatechange = function(){
			if(xhr.readyState == XMLHttpRequest.DONE){
				if(xhr.status == 200){
					handle1SecondResponse(xhr.responseText);
				}
				else{
					console.log("xhr request didn't go!!");
				}
			}
		};
		
		xhr.open("GET","QuestionServlet?accountId=" + accountId +"&projectId=" + projectId, true);
		xhr.send();
	}
}
function handle1SecondResponse(responseText){
	//console.log(responseText);
	 eval(responseText); // Evaluate the JavaScript code

    // Now you can access the questionsData variable
    console.log(questionsData);
    var container = document.getElementById("container");
    
    //var form = document.getElementById("questions");
    var addButton = document.createElement("button");
    addButton.textContent = "+";
    addButton.onclick = function() {
    	 event.preventDefault();
    	var textbox = document.createElement("input");
        textbox.setAttribute("type", "text");
        textbox.setAttribute("name", "question");
        var removeButton = document.createElement("button"); // Create the "-" button
        removeButton.textContent = "-";
        removeButton.onclick = function(){
        	container.removeChild(textbox);
        	container.removeChild(removeButton);
        	//form.removeChild(textbox);
        }
        container.appendChild(textbox);
        container.appendChild(removeButton);
        container.appendChild(document.createElement("br"));
        //form.appendChild(textbox);
    }
    container.appendChild(addButton);
    container.appendChild(document.createElement("br"));
    container.appendChild(document.createElement("br"));
        // Add functionality for the + button here
       // for (var i = 0; i < 5; i++) {
         //   var textbox = document.createElement("input");
           // textbox.setAttribute("type", "text");
            //container.appendChild(textbox);
            //var removeButton = document.createElement("button"); // Create the "-" button
            //removeButton.textContent = "-";
            //removeButton.onclick = function() {
            	//event.preventDefault();
                // Remove the textbox and the associated "-" button
                //container.removeChild(textbox);
                //container.removeChild(removeButton);
            //};
            //container.appendChild(removeButton); // Append the "-" button
            //container.appendChild(document.createElement("br")); // Add line break after each textbox
        //}
   // };
    //container.appendChild(addButton);
    //container.appendChild(addButton);
    // Access individual properties of the questionsData object
    for (var key in questionsData) {
        if (questionsData.hasOwnProperty(key)) {
            var question = questionsData[key];
            var text = document.createElement("input");
            var brea = document.createElement("br");
            text.setAttribute("type", "text");
           	//textbox.setAttribute("name", "question"); // Add name attribute for the question
            text.setAttribute("value", question);
            var removeButton = document.createElement("button"); // Create the "-" button
            removeButton.textContent = "-";
            event.preventDefault();
            removeButton.onclick = function(){
            	event.preventDefault();
            	container.removeChild(text);
            	container.removeChild(removeButton);
            	//form.removeChild(textbox);
            }
            //textbox.value = questionsData[key]; // Use the value associated with the key
            container.appendChild(text);
            container.appendChild(removeButton);
            container.appendChild(brea);
            text.setAttribute("name", "question");
            //form.appendChild(textbox);
            //console.log("Question ID: " + key + ", Question: " + question);
        }
    }
    //removeButton.onclick = function(){
    	//event.preventDefault();
    	//container.removeChild(textbox);
    	//container.removeChild(removeButton);
    	//form.removeChild(textbox);
    //}
    
    
    

}

</script>
</body>
</html>