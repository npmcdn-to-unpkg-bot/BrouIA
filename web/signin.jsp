<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="css/icons.css"/>
        <link rel="stylesheet" type="text/css" href="css/style.css"/>
        <title>ADI | Signin</title>
    </head>
    <body class="user-page">
        <h1>Sign in</h1>

        <form class="user-form" id="signin-form" method="POST" action="api/auth/signin">
            <div>
                <span class="icon icon-head"></span>
                <label for="oldPass">Usuari</label>
                <input type="text" name="username" id="user">
            </div>
            <div>
                <span class="icon icon-lock"></span>
                <label for="oldPass">Contasenya</label>
                <input type="password" name="password" id="pass">
            </div>
            <div class="right">
                <button type="submit">
                    <span class="icon icon-check"></span>Sign in
                </button>
            </div>
        </form>

        <script src="http://code.jquery.com/jquery-2.2.0.min.js"></script>
        <script>
            if (typeof jQuery === 'undefined') {
                document.write(unescape("%3Cscript src='/ADI/js/jquery-2.2.0.min.js' type='text/javascript'%3E%3C/script%3E"));
            }
        </script>
        <script>
            $(document).ready(function () {
                $('#signin-form').submit(function () {
                    var uname = $('#user').val();
                    var pass = $('#pass').val();

                    if (uname === '' || pass === '') {
                        alert("Has d'omplir tots el camps.");
                        return false;
                    }
                });
            });
        </script>
    </body>
</html>
