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
    <body class="user-page error-404">
        <h1>Error 404.</h1>
        <div>
            <a href="index.jsp">Tornar a la página principal.</a>
        </div>

        <div id="video404">
        <video  autoplay>
            <source src="inc/404.mp4" type="video/mp4">
        </video>
        </div>


        <script src="http://code.jquery.com/jquery-2.2.0.min.js"></script>
        <script>
            if (typeof jQuery === 'undefined') {
                document.write(unescape("%3Cscript src='/ADI/js/jquery-2.2.0.min.js' type='text/javascript'%3E%3C/script%3E"));
            }
        </script>
        <script>
            $(document).ready(function () {
                $('#404video');
            });
        </script>

    </body>
</html>
