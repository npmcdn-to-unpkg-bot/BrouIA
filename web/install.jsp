<%@page import="database.DBActions"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>


<%
    DBActions actions = new DBActions();
    actions.installOrResetAll();
    response.setStatus(response.SC_MOVED_TEMPORARILY);
    response.setHeader("Location", request.getContextPath() + "/index.jsp");

%>
