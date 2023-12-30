<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ page import="com.demo.model.User" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Home page</title>
</head>
<body>
<%
   // session = request.getSession(false);
//	User user = (User) session.getAttribute("userInfo");
	//String remmerberMe = (String) session.getAttribute("remmerberMe");
    %>
	<h1>Welcome to home page</h1>
	<h2>User Information</h2>

    <p>User ID: ${userInfo.getUserID()}</p>
    <p>Name: ${userInfo.getName()}</p>
    <p>Role ID: ${userInfo.getRoleID()}</p>
    <p>Remember me: ${remmemberMe}</p>


</body>
</html>