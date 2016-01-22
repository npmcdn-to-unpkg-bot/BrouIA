$(document).ready(function () {


    $.ajax({
        url: "api/illes",
        type: 'GET',
        success: function (resp, status) {
            var obj = eval("(" + resp + ")");
            $.each(obj.illes, function (i, val) {
                $("#submenu-islas").append('<li class="menu_item"><a class="menu_link" href="#">' + val.nom_illa + '</a></li>');
            });
        },
        error: function (e) {
            alert("error: " + e.statusText);
            console.log(e);
        }
    }).then(function () {

        var mlMenu = new MLMenu(document.getElementById('menu'), {
            breadcrumbsCtrl: true, // show breadcrumbs
            initialBreadcrumb: 'all', // initial breadcrumb text
            backCtrl: false, // show back button
            itemsDelayInterval: 60, // delay between each menu item sliding animation
            onItemClick: loadData
        });

        $('.action_open').click(openMenu);
        $('.action_close').click(closeMenu);

        function openMenu() {
            $('#menu').addClass('menu_open');
        }
        function closeMenu() {
            $('#menu').removeClass('menu_open');
        }

        function loadData(ev, itemName) {
            $('.info')[0].innerHTML = itemName;
        }
    });
});