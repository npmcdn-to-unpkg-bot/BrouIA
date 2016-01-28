$(document).ready(function () {

    $('.content').addClass('content-loading');

    $.ajax({
        url: "api/illes",
        type: 'GET',
        success: function (resp, status) {
            var obj = eval("(" + resp + ")");
            
            $("#menus ul.main-menu_level").append("<li class='menu_item'><a data-submenu='submenu-islas' class='menu_link' href='#'>Islas</a></li>")
            $("#menus").append("<ul id='submenu-islas' data-menu='submenu-islas' class='menu_level'></ul>");
            
            $.each(obj.illes, function (i, val) {
                var subMenuId = "submenu-islas-" + val.nom_illa.trim();
                $("#submenu-islas").append('<li class="menu_item"><a data-submenu="' + subMenuId + '" class="menu_link" href="#">' + val.nom_illa + '</a></li>');
                //var ulIlla = $("<ul id=" + subMenuId + "' data-menu='" + subMenuId + "' class='menu_level'><li class='menu_item'><a class='menu_link' href='#'>ALL</a></li></ul>");
                var ulIlla = $("<ul id=" + subMenuId + "' data-menu='" + subMenuId + "' class='menu_level'><li class='menu_item'><a class='menu_link' href='#'>ALL</a></li></ul>");
                $.each(val.municipis, function(i2, val2){
                    console.log(ulIlla);
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

//animació del menu
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
            $('.info')[0].innerHTML = itemName;
            
            closeMenu();
        }
    });
});