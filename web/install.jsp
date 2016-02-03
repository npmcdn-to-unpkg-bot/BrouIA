<%@page import="database.DBActions"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="es" class="no-js">
    <head>
        <meta charset="UTF-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>ADI | Install</title>

        <link rel="stylesheet" type="text/css" href="css/icons.css"/>
        <link rel="stylesheet" type="text/css" href="css/style.css"/>
        <script src="js/modernizr.min.js"></script>
    </head>
    <body>

        <div class="container">
            <section class="content content-loading">
                <p class="info">Instal·lant tots els components</p>
            </section>
        </div>

    </body>
</html>

<%
    DBActions actions = new DBActions();
    actions.installOrResetAll();
    response.setStatus(response.SC_MOVED_TEMPORARILY);
    response.setHeader("Location", request.getContextPath() + "/index.jsp");

%>
