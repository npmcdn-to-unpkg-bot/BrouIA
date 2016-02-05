<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>ADI | Signin</title>
    </head>
    <body>
        <h1>Sign in</h1>
        
        <form method="POST" action="api/auth/signin">
            <input type="text" name="username">
            <input type="password" name="password">
            <input type="submit" value="Sign in"/>
        </form>
        
    </body>
</html>
