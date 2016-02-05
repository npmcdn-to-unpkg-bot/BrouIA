<%-- 
    Document   : proves
    Created on : 05-feb-2016, 14:24:43
    Author     : johnn
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>

        <script src="http://code.jquery.com/jquery-2.2.0.min.js"></script>
        <script>

            <%
                session.setAttribute("attr", "hola mundo");
            %>


            $(document).ready(function () {
                alert('${sessionScope.attr}');
            });
        </script>

    </body>
</html>
