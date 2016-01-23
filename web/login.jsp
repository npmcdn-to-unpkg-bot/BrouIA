<%@include file="_header.jsp" %>  
<%@include file="_nav.jsp" %>  

<section class="content">
    <form class="fancy-form">
        
        <div class="ff-group">
            <div class="placeholder">
                <label for="name">Nombre: </label>
            </div>
            <input type="text" id="name"/>
        </div>
        
        <div class="ff-group">
            <div class="placeholder">
                <label for="pass">Contraseña: </label>
            </div>
            <input type="password" id="pass"/>
        </div>
        
        <button type="submit">Login</button>
    </form>
</section>

<%@include file="_footer.jsp" %> 
