<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="css/icons.css"/>
        <link rel="stylesheet" type="text/css" href="css/style.css"/>
        <title>ADI | Administacio</title>
    </head>
    <body class="user-page">
        <h1>Canviar contrasenya</h1>

        <form class="user-form" id="manage-form" method="POST" action="api/auth/changepass">
            <div>
                <span class="icon icon-lock"></span>
                <label for="oldPass">Contasenya vella</label>
                <input type="password" name="oldpass" id="oldPass">
            </div>
            <div>
                <span class="icon icon-lock"></span>
                <label for="password">Contasenya nova</label>
                <input type="password" name="password" id="password">
            </div>
            <div>
                <span class="icon icon-lock"></span>
                <label for="pass2">Repeteix la conteasenya nova</label>
                <input type="password" name="password2" id="pass2">
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
                $('#manage-form').submit(function () {
                    var oldpass = $('#oldPass').val();
                    var pass1 = $('#password').val();
                    var pass2 = $('#pass2').val();

                    if (oldpass === '' || pass1 === '' || pass2 === '') {
                        alert("Has d'omplir tots el camps.");
                        $('#oldPass').parent().addClass('alert');
                        $('#password').parent().addClass('alert');
                        $('#pass2').parent().addClass('alert');
                        return false;
                    }

                    if (pass1 !== pass2) {
                        alert("Les dues contrasenyes no coincideixen.");
                        $('#password').parent().addClass('alert');
                        $('#pass2').parent().addClass('alert');
                        return false;// contrasenyes no cincidents
                    }
                });
            });
        </script>

    </body>
</html>