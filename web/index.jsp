<%-- 
    Document   : index
    Created on : 22-ene-2016, 12:05:02
    Author     : johnn
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es" class="no-js">
    <head>
        
	<meta charset="UTF-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>ADI</title>
        
        <link rel="stylesheet" type="text/css" href="css/style.css"/>
        <script src="js/modernizr.min.js"></script>
    </head>
    <body>
        
        <!-- MainContainer -->
        <div class="container">
            <header class="header">
                <div class="logo">
                    <div class="logo-image">
                        <h1 class="logo-text">LOGO img</h1>
                    </div>
                    <h2 class="logo-text">ADI</h2>
                </div>
            </header>
            
            <button class="action action_open" aria-label="Open Menu">
                <span class="icon icon_menu"></span>
            </button>
            
            <nav id="menu" class="menu">
                
                <button class="action action_close" aria-label="Close Menu">
                    <span class="icon icon_cross"></span>
                </button>
                
                <div class="menu_wrap">
                    
                    <ul data-menu="main" class="menu_level">
                        <li class="menu_item">
                            <a data-submenu="submenu-islas" class="menu_link" href="#">Islas</a>
                        </li>
                        <li class="menu_item">
                            <a  class="menu_link" href="#">fsdafsdasdfa</a>
                        </li>
                    </ul>
                    
                    <ul id="submenu-islas" data-menu="submenu-islas" class="menu_level">
                    </ul>
                    
                </div>
                
            </nav>
            
            <section class="content">
                <p class="info">Hola</p>
            </section>
        </div>
        
    </body>
    
    <script src="http://code.jquery.com/jquery-2.2.0.min.js"></script>
    <script src="js/classie.js"></script>
    <script src="js/mlMenu.js"></script>
    <script src="js/main.js"></script>

</html>
