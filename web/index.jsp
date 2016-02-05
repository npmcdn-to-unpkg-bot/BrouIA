<%@page import="database.DBActions"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>


<%
    DBActions actions = new DBActions();
    if (actions.installIsNeed()) {
        response.setStatus(response.SC_MOVED_TEMPORARILY);
        response.setHeader("Location", request.getContextPath() + "/install.jsp");
    }
    if (request.getParameter("auth-token") != null) {
        session.setAttribute("token", request.getParameter("auth-token"));
    }
%>

<!DOCTYPE html>
<html lang="es" class="no-js">
    <head>
        <meta charset="UTF-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>ADI</title>

        <link rel="stylesheet" type="text/css" href="css/icons.css"/>
        <link rel="stylesheet" type="text/css" href="css/style.css"/>
        <script src="js/modernizr.min.js"></script>

    </head>
    <body>

        <div class="container">
            <header class="header">
                <div class="logo">
                    <div class="logo-image">
                        <h1 class="logo-text">LOGO img</h1>
                    </div>
                    <h2 class="logo-text">ADI</h2>
                </div>

                <div class="user-opts">
                    <% if (session.getAttribute("token") == null) { %>

                    <!-- no ususri logeat -->
                    <a href="signin.jsp">Entrar</a>
                    <a href="register.jsp">Registrar-se</a>

                    <% } else { %>

                    <!-- amb usuari logeat -->
                    <a id="hi" href="manage.jsp">Hola </a>

                    <% } %>
                </div>
            </header>

            <button class="action action-open" aria-label="Open Menu"><span class="icon icon-menu"></span></button>
            <nav id="menu" class="menu">
                <button class="action action-close" aria-label="Close Menu"><span class="icon icon-cross"></span></button>

                <div class="menu_wrap">
                    <div id="menus">
                        <ul data-menu="main" class="menu_level main-menu_level"></ul>
                    </div>
                </div>
            </nav>

            <section  class="content">

                <div class="chart-grid tab tab-visible">
                    <div id="chart01" class="chart"></div>
                    <div id="chart02" class="chart"></div>
                </div>

                <div class="chat-window tab">
                    <div class="chat-messages">
                        <ol class="chat-messages-list"></ol>
                    </div>
                    <div class="chat-input-bar">
                        <form id="chat-form">
                            <input placeholder="Escriu alguna cosa" type="text" id="chat-message"/>
                            <input type="submit" value="send"/>
                        </form>
                    </div>
                </div>

            </section>

        </div>

        <script src="http://code.jquery.com/jquery-2.2.0.min.js"></script>
        <script>
            if (typeof jQuery === 'undefined') {
                document.write(unescape("%3Cscript src='/ADI/js/jquery-2.2.0.min.js' type='text/javascript'%3E%3C/script%3E"));
            }
        </script>
        <script src="https://npmcdn.com/masonry-layout@4.0.0/dist/masonry.pkgd.min.js"></script>
        <script>
            if (typeof Masonry === 'undefined') {
                document.write(unescape("%3Cscript src='/ADI/js/masonry.min.js' type='text/javascript'%3E%3C/script%3E"));
            }
        </script>
        <script src="js/classie.js"></script>
        <script src="js/mlMenu.js"></script>
        <script src="js/js-charts/highcharts.js"></script>
        <script src="js/js-charts/modules/exporting.js"></script>

        <script>
            function appendMessage(cls, msg) {
                $('.chat-messages-list').append('<li class="chat-message chat-message-' + cls + '"><div class="chat-message-bubble">' + msg + '</div></li>');
            }

            function cleanChat() {
                $('.chat-messages-list li').remove()
            }

            function getPast(myself, friend, token) {
                $.ajax({
                    url: 'api/chat/past?friend=' + friend,
                    method: 'GET',
                    headers: {
                        'Authorization': 'Bearer ' + token
                    },
                    success: function (resp) {
                        var obj = eval("(" + resp + ")");
                        $.each(obj.messages, function (i, val) {
                            var cls = "myself";
                            if (myself !== val.to) {
                                cls = "friend"
                            }
                            appendMessage(cls, val.message);
                        });
                        $('.chat-messages').animate({scrollTop: $('.chat-messages-list').height()}, "slow");
                    }
                });
            }
        
            var audio = new Audio('inc/ding.mp3');
            var polling = false;
            function startPoll(user, token) {
                polling = true;
                $.ajax({
                    url: 'api/chat/read/' + user,
                    type: 'GET',
                    headers: {
                        'Authorization': 'Bearer ' + token
                    },
                    success: function (data) {
                        if (data !== "") {

                            var msgs = data.split("\n");
                            for (i = 0; i < msgs.length; i++) {
                                var msg = $.trim(msgs[i]);
                                if (msg !== '') {
                                    console.log(i + ".- " + msg);
                                    var obj = eval("(" + msg + ")");
                                    appendMessage("friend", obj.message);
                                    audio.play();
                                }
                            }
                            $('.chat-messages').animate({scrollTop: $('.chat-messages-list').height()}, "slow");
                        }
                    }
                }).always(function () {
                    if (polling)
                        startPoll(user, token);
                });
            }

            $(document).ready(function () {
                var token = '<% if (session.getAttribute("token") != null) {
                        out.print(session.getAttribute("token"));
                    }%>';
                var myself = '';

                $('.content').addClass('content-loading');
                $("#menus ul.main-menu_level").append("<li class='menu_item'><a data-submenu='submenu-islas' class='menu_link' href='#'>Islas</a></li>");

                if (token === '') {
                    $("#menus ul.main-menu_level").append("<li class='menu_item'><a class='menu_link' href='signin.jsp'>Chats</a></li>");
                } else {
                    $("#menus ul.main-menu_level").append("<li class='menu_item'><a data-submenu='submenu-chats' class='menu_link' href='#'>Chats</a></li>");
                    $.ajax({
                        url: 'api/auth/info',
                        type: 'GET',
                        headers: {
                            'Authorization': 'Bearer ' + token
                        },
                        success: function (data) {
                            var obj = eval("(" + data + ")");
                            myself = obj.name;
                            $('#hi').append(obj.name);
                        }
                    });
                }

                $.ajax({
                    url: "api/data/illes",
                    type: 'GET',
                    success: function (resp, status) {
                        var obj = eval("(" + resp + ")");

                        $("#menus").append("<ul id='submenu-islas' data-menu='submenu-islas' class='menu_level'></ul>");

                        $.each(obj.illes, function (i, val) {
                            var subMenuId = "submenu-islas-" + val.nom_illa.trim();
                            $("#submenu-islas").append('<li class="menu_item"><a data-submenu="' + subMenuId + '" class="menu_link" href="#">' + val.nom_illa + '</a></li>');

                            var ulIlla = $("<ul id=" + subMenuId + "' data-menu='" + subMenuId + "' class='menu_level'></ul>");
                            $.each(val.municipis, function (i2, val2) {
                                $('<li class="menu_item"><a class="menu_link" href="#">' + val2.nom_municipi + '</a></li>').appendTo(ulIlla);
                            });

                            ulIlla.appendTo("#menus");
                        });
                        $('.content').removeClass('content-loading');
                    },
                    error: function (e) {
                        alert("error: " + e.statusText);
                        console.log(e);
                    }
                }).then(function () {

                    $('.action-open').click(openMenu);
                    $('.action-close').click(closeMenu);
                    $('.reset').click(reset);
                    function openMenu() {
                        $('#menu').addClass('menu_open');
                    }
                    function closeMenu() {
                        $('#menu').removeClass('menu_open');
                    }
                    function reset() {
                        cao = {
                            chart: {type: 'area'},
                            title: {
                                text: "Total per dies de la setmana"
                            },
                            xAxis: {
                                categories: []
                            },
                            yAxis: {
                                title: {
                                    text: 'Total'
                                }
                            },
                            series: []
                        };
                        hco = {
                            title: {
                                text: "Total per hores del dia"
                            },
                            yAxis: {
                                title: {
                                    text: 'Total'
                                }
                            },
                            series: []
                        };
                    }
                    var cao, hco;
                    reset();
                    cao.xAxis.categories.push("Lunes");
                    cao.xAxis.categories.push("Martes");
                    cao.xAxis.categories.push("Miercoles");
                    cao.xAxis.categories.push("Jueves");
                    cao.xAxis.categories.push("Viernes");
                    cao.xAxis.categories.push("Sabado");
                    cao.xAxis.categories.push("Domingo");

                    var friend = '';
                    function loadData(ev, itemName) {
                        var parent = ev.target.parentElement.parentElement.getAttribute('data-menu');
                        if (parent === 'submenu-chats') {
                            if (!$('.chat-window').hasClass('tab-visible')) {
                                $('.tab').toggleClass('tab-visible');
                            }
                            if (friend !== itemName) {
                                $('.user-' + friend).removeClass('unread');
                                //$('.user-' + friend).html(friend);
                                cleanChat();
                                friend = itemName;
                                polling = false;
                                getPast(myself, friend, token);
                                startPoll(friend, token);
                            }

                            closeMenu();
                        } else {

                            if (!$('.chart-grid').hasClass('tab-visible')) {
                                $('.tab').toggleClass('tab-visible');
                            }

                            var municipio = itemName;
                            var isla = parent.substring(14);

                            function getRandomColor() {
                                var letters = '0123456789ABCDEF'.split('');
                                var color = '#';
                                for (var i = 0; i < 6; i++) {
                                    color += letters[Math.floor(Math.random() * 16)];
                                }
                                return color;
                            }
                            var color = getRandomColor();

                            $.ajax({
                                url: "api/data/days/" + isla + "/" + municipio,
                                type: 'GET',
                                success: function (resp, status) {
                                    var obj = eval("(" + resp + ")");
                                    var series = {
                                        name: municipio,
                                        color: color,
                                        data: []
                                    };

                                    series.data.push(0);
                                    series.data.push(0);
                                    series.data.push(0);
                                    series.data.push(0);
                                    series.data.push(0);
                                    series.data.push(0);
                                    series.data.push(0);
                                    $.each(obj.dies, function (i, val) {
                                        series.data[val.dia_num] = val.total;
                                    });

                                    cao.series.push(series);
                                }
                            }).then(function () {
                                $('#chart01').highcharts(cao);
                            });


                            $.ajax({
                                url: "api/data/hours/" + isla + "/" + municipio,
                                type: 'GET',
                                success: function (resp, status) {
                                    var obj = eval("(" + resp + ")");
                                    var series = {
                                        name: municipio,
                                        color: color,
                                        data: []
                                    };
                                    $.each(obj.times, function (i, val) {
                                        var data = [val.desde, val.total];
                                        series.data.push(data);
                                    });
                                    hco.series.push(series);
                                }
                            }).then(function () {
                                $('#chart02').highcharts(hco);
                            });

                            closeMenu();

                            $('.chart-grid').masonry({
                                itemSelector: '.chart',
                                columnWidth: 400,
                                isFitWidth: true
                            });
                        }
                    }

                    if (token !== '') {
                        $.ajax({
                            url: 'api/chat/users',
                            method: 'GET',
                            headers: {
                                'Authorization': 'Bearer ' + token
                            },
                            success: function (resp) {
                                var obj = eval("(" + resp + ")");
                                var $chat = $("<ul id='submenu-chats' data-menu='submenu-chats' class='menu_level'></ul>");
                                $.each(obj.users, function (i, val) {
                                    $chat.append("<li class='menu_item'><a class='menu_link user-" + val.name + "' href='#'>" + val.name + "</a></li>");
                                });
                                $chat.appendTo('#menus');

                            }
                        }).then(function () {

                            $.ajax({
                                url: 'api/chat/unreads',
                                method: 'GET',
                                headers: {
                                    'Authorization': 'Bearer ' + token
                                },
                                success: function(resp) {
                                    if (resp !== '') {
                                        var obj = eval("(" + resp + ")");
                                        $.each(obj.unreads, function(i, val) {
                                            $('.user-' + val.name).addClass('unread');
                                            //$('.user-' + val.name).html(val.name + " " + val.total);
                                        });
                                    }
                                }
                            });

                            var mlMenu = new MLMenu(document.getElementById('menu'), {
                                breadcrumbsCtrl: true, // show breadcrumbs
                                initialBreadcrumb: 'all', // initial breadcrumb text
                                backCtrl: false, // show back button
                                itemsDelayInterval: 60, // delay between each menu item sliding animation
                                onItemClick: loadData
                            });

                            $('#chat-form').submit(function () {
                                var msg = $('#chat-message').val();
                                if (msg === '') {
                                    return false;
                                }

                                $('#chat-message').val("");
                                appendMessage('myself', msg);

                                $.ajax({
                                    url: 'api/chat/send',
                                    headers: {
                                        'Authorization': 'Bearer ' + token,
                                        'to': friend,
                                        'msg': msg
                                    },
                                    type: 'POST',
                                });
                                return false;
                            });
                        });
                    } else {
                        //animació del menu
                        var mlMenu = new MLMenu(document.getElementById('menu'), {
                            breadcrumbsCtrl: true, // show breadcrumbs
                            initialBreadcrumb: 'all', // initial breadcrumb text
                            backCtrl: false, // show back button
                            itemsDelayInterval: 60, // delay between each menu item sliding animation
                            onItemClick: loadData
                        });


                    }
                });
            });
        </script>

    </body>
</html>