<%@include file="_header.jsp" %>  
<%@include file="_nav.jsp" %>  

<%    String nombre = request.getParameter("nombre");
    String password = request.getParameter("pass");

    if (nombre != null && password != null) {

        boolean isOk = actions.logInUser(nombre, password);
        if (isOk) {
            session.setAttribute("user", nombre);
            session.setAttribute("pass", password);
            session.setAttribute("level", "1");

            response.setStatus(response.SC_MOVED_TEMPORARILY);
            response.setHeader("Location", request.getContextPath() + "/index.jsp");
        } else {
            // msg de que alguna cosa ha fallat
        }

    }
%>

<section class="content">
    <form class="fancy-form">

        <div class="ff-group">
            <div class="placeholder">
                <label for="name">Nombre: </label>
            </div>
            <input type="text" id="name" name="nombre"/>
        </div>

        <div class="ff-group">
            <div class="placeholder">
                <label for="pass">Contraseña: </label>
            </div>
            <input type="password" id="pass" name="pass"/>
        </div>

        <button type="submit">Login</button>
    </form>
</section>

<%@include file="_footer.jsp" %> 
