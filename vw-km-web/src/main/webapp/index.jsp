<%@page import="vwg.vw.km.integration.persistence.model.UserModel"%><html>
<head>
    <title>JSP Page</title>
</head>
<body>
<% 
if(session.getAttribute(UserModel.class.getName())!=null){
    response.sendRedirect("pages/home.jsf");
 }else{
    session.invalidate();
   response.sendRedirect("pages/login.jsf" );
 } 
 %>
</body>
</html>