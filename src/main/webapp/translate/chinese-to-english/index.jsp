<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>My JSP 'index.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
   <form id="translateForm" action="" >
       <textarea rows="10" cols="100" id="translateInput"  name="translateInput"></textarea>
       <button type="button" id="translateBtn">翻译</button>
   </form>
    
    <form id="translateResultForm">
      <textarea rows="25" cols="100" id="translateResult"  name=""></textarea>
            
    </form>
     <script src="../common/js/jquery-1.10.2.min.js"></script>
     <script src="js/index.js"></script>
  </body>
</html>
