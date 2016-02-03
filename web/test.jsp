<%@include file="_header.jsp" %>  
<%@include file="_nav.jsp" %>  

<section  class="content">
    <div id="chat-msgs"></div>


</section>

</div><!-- /Container -->
<%@include file="_scripts.jsp" %> 

<script>
    $(document).ready(function () {
        $.ajax({
            url: 'api/chat/users',
            method: 'GET',
            success: function (resp) {
                var obj = eval("(" + resp + ")");
                $("#menus ul.main-menu_level").append("<li class='menu_item'><a data-submenu='submenu-users' class='menu_link' href='#'>Usuarios</a></li>")

                var $users =
                        $("<ul id='submenu-users' data-menu='submenu-users' class='menu_level'></ul>");
                $.each(obj.users, function (i, val) {
                    $users.append("<li class='menu_item'><a class='menu_link' href='#'>" + val.name + "</a></li>")
                });

                $users.appendTo('#menus');
            },
            error: function (e) {
                console.log('error: ' + e);
            }
        }).then(function () {
            var mlMenu = new MLMenu(document.getElementById('menu'), {
                breadcrumbsCtrl: true, // show breadcrumbs
                initialBreadcrumb: 'all', // initial breadcrumb text
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
            
            function loadData(ev, itemName) {
                alert(itemName);
            }
            
        });
    });
</script>

<%@include file="_footer.jsp" %> 