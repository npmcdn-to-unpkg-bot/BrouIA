<%@include file="_header.jsp" %>  
<%@include file="_nav.jsp" %>  

<%    String nombre = request.getParameter("nombre");
    String password = request.getParameter("pass1");
    String password2 = request.getParameter("pass2");

    if (nombre != null && password != null && password2 != null) {
        if (password.equals(password2)) {
            //store user
            boolean isOk = actions.createUser(nombre, password);
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

    }
%>

<section class="content">
    <form class="fancy-form" action="register.jsp" method="POST">

        <div class="ff-group">
            <div class="placeholder">
                <label for="name">Nombre: </label>
            </div>
            <input type="text" id="name" name="nombre"/>
        </div>

        <div class="ff-group">
            <div class="placeholder">
                <label for="pass1">Contraseņa: </label>
            </div>
            <input type="password" id="pass1" name="pass1"/>
        </div>

        <div class="ff-group">
            <div class="placeholder">
                <label for="pass2">Contraseņa: </label>
            </div>
            <input type="password" id="pass2" name="pass2"/>
        </div>

        <button type="submit">Registrarse</button>
    </form>

</section>

<%@include file="_footer.jsp" %> 
