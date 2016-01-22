$(document).ready(function () {
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

    }
});