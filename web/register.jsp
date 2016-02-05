<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>ADI | Register</title>
    </head>
    <body>
        <h1>Register</h1>
        
        <form id="register-form" method="POST" action="api/auth/register">
            <input type="text" name="username" id="user">
            <input type="password" name="password" id="pass1">
            <input type="password" name="password" id="pass2">
            <input type="submit" value="Sign in"/>
        </form>
        
        <script src="http://code.jquery.com/jquery-2.2.0.min.js"></script>
        <script>
            $(document).ready(function() {
                $('#register-form').submit(function(){
                    var uname = $('#user').val();
                    var pass1 = $('#pass1').val();
                    var pass2 = $('#pass2').val();
                    
                    if (uname === '' || pass1 === '' || pass2 === '') {
                        return false;//falten coses
                    }
                    
                    if (pass1 !== pass2) {
                        return false;// contrasenyes no cincidents
                    }
                });
            });
        </script>
        
    </body>
</html>

