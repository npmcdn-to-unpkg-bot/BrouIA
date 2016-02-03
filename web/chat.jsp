<%@include file="_header.jsp" %>  
<%@include file="_nav.jsp" %>  

<%  if (session.getAttribute("user") != null && session.getAttribute("pass") != null) {
        String nombre = (String) session.getAttribute("user");
        String password = (String) session.getAttribute("pass");
        boolean isOk = actions.logInUser(nombre, password);
        if (!isOk) {
            response.setStatus(response.SC_MOVED_TEMPORARILY);
            response.setHeader("Location", request.getContextPath() + "/login.jsp");
        }
    } else {
        response.setStatus(response.SC_MOVED_TEMPORARILY);
        response.setHeader("Location", request.getContextPath() + "/login.jsp");
    }
%>


<section  class="content">

    <div class="chat-window">
        <div class="chat-messages">
            <ol class="chat-messages-list"></ol>
        </div>
        <div class="chat-input-bar">
            <form id="chat-form">
                <input type="text" id="chat-message"/>
                <input type="submit" value="send"/>
            </form>
        </div>
    </div>

</section>

</div><!-- /Container -->
<%@include file="_scripts.jsp" %> 
<!-- Load users and build menu -->
<script>
    var Notification = window.Notification || window.mozNotification || window.webkitNotification;

    Notification.requestPermission(function (permission) {
        // console.log(permission);
    });

    function show(title, message) {
        var instance = new Notification(
                title, {
                    body: message
                }
        );

        instance.onclick = function () {
            // Something to do
        };
        instance.onerror = function () {
            // Something to do
        };
        instance.onshow = function () {
            // Something to do
        };
        instance.onclose = function () {
            // Something to do
        };
    }


    //cls: {myself, friend}
    function appendMessage(cls, msg) {
        $('.chat-messages-list').append('<li class="chat-message chat-message-' + cls + '"><div class="chat-message-bubble">' + msg + '</div></li>');
    }

    function getPast(myself, friend) {
        $.ajax({
            url: 'api/chat/past?myself=' + myself + '&friend=' + friend,
            method: 'GET',
            success: function (resp) {
                var obj = eval("(" + resp + ")");
                $.each(obj.messages, function (i, val) {
                    var cls = "myself";
                    if (myself !== val.to) {
                        cls = "friend"
                    }
                    appendMessage(cls, val.message);
                });
            }
        });
    }

    var polling = false;
    function startPoll(user) {
        polling = true;
        $.ajax({
            url: 'api/chat/read/' + user,
            type: 'GET',
            success: function (data) {
                if (data != "") {
                    var msgs = data.split("\n");
                    for (i = 0; i < msgs.length; i++) {
                        var obj = JSON.parse(msgs[i]);
                        appendMessage("friend", obj.message);
                        show(obj.from, obj.message);
                    }
                }
            }
        }).always(function () {
            if (polling)
                startPoll(user);
        });
    }

    $(document).ready(function () {
        var friend = "";
        $('.content').addClass('content-loading');
        $.ajax({
            url: 'api/chat/users',
            method: 'GET',
            success: function (resp) {
                var obj = eval("(" + resp + ")");
                $("#menus ul.main-menu_level").append("<li class='menu_item'><a data-submenu='submenu-users' class='menu_link' href='#'>Usuarios</a></li>");

                var $users = $("<ul id='submenu-users' data-menu='submenu-users' class='menu_level'></ul>");
                $.each(obj.users, function (i, val) {
                    if (val.name !== '<% out.print(session.getAttribute("user"));%>') {
                        $users.append("<li class='menu_item'><a class='menu_link user-" + val.name + "' href='#'>" + val.name + "</a></li>");
                    }
                });

                $users.appendTo('#menus');
            },
            error: function (e) {
                console.log('error: ' + e);
            }
        }).then(function () {
            $('.content').removeClass('content-loading');
            var mlMenu = new MLMenu(document.getElementById('menu'), {
                breadcrumbsCtrl: true, // show breadcrumbs
                initialBreadcrumb: 'chat', // initial breadcrumb text
                backCtrl: false, // show back button
                itemsDelayInterval: 60, // delay between each menu item sliding animation
                onItemClick: loadData
            });

            $('.action-open').click(openMenu);
            $('.action-close').click(closeMenu);

            function openMenu() {
                $('#menu').addClass('menu_open');
            }
            function closeMenu() {
                $('#menu').removeClass('menu_open');
            }

            $('#chat-form').submit(function () {
                var msg = $('#chat-message').val();
                $('#chat-message').val("");
                appendMessage('myself', msg);

                $.ajax({
                    url: 'api/chat/send',
                    headers: {
                        'to': friend,
                        'from': '<% out.print(session.getAttribute("user"));%>',
                        'msg': msg
                    },
                    type: 'POST',
                });
                return false;
            });

            function loadData(ev, itemName) {
                friend = itemName;
                polling = false;
                getPast('<% out.print(session.getAttribute("user"));%>', friend);
                startPoll(friend);
                closeMenu();
            }
        });
    });
</script>

<%@include file="_footer.jsp" %> 
