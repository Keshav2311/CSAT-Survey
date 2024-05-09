# CSAT-Survey
Customer Satisfaction Score 

This is a project which collects the Customer Satisfation Score by giving them appropriate questions about their projects and customer can give Ratings and also can give overall feedback.

This projects consist of Three pages:
1)  Admin Page - here admin enters his/her personal details and questions to be provided to every projects.
2)  Oth Verification Page - here Client writes therir oth code which redirect to Ratings Page.
3)  Ratings Page - here client can give ratings to each questions and also can give overall feedback.

This whole project is exclusively built in Eclipse.

Technologies Used: 
1)  Front-End - We use JSPs from Java and CSS and JavaScript.
2)  Server - Tomcat Server which is inbuilt inside Eclipse.
3)  Back-End - For writing APIs and connections we use Servlets from Java only.
4)  Database - For database we have used PostgreSql and create statement of every database is written inside database.db.create.

Steps followed so the project will work:
1)  Fisrt set up the Tomcat Server and clean it.
2)  Now built the project without any errors.
3)  The first page index.jsp will open in web browser which will take some information of admin. Here we can add and delete the questions that will be required to be added for every projects.
4)  Now on submit a link will be generated and on clicking on the link an oth code and URL will be generated which will be stored in the database. We can share the oth code to client.
5)  Now on oth verification page client can write the oth code which will redirect it to Ratings page.
6)  On Ratings page client can give ratings to every questions and also give overall feedback which will be saved inside the database.
   
