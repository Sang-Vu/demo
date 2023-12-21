<%@ page import="com.demo.model.User" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Home page</title>
</head>
<body>
<%
        // Retrieve user information from the session
        session = request.getSession();
	User user = (User) session.getAttribute("userInfo");
    %>
	<h1>Welcome to home page</h1>
	<h2>User Information</h2>
 <%   if (user != null) {
%>
    <p>User ID: <%= user.getUserID() %></p>
    <p>Role ID: <%= user.getRoleID() %></p>
    <p>Remember Me: </p>
    <!-- Add more fields as needed -->
<%
    }
%>
    <p>Password: </p>
    <p>Remember Me: </p>
</body>
</html>