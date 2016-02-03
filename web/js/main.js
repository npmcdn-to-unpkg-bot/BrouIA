$(document).ready(function () {

    $('.content').addClass('content-loading');

    $.ajax({
        url: "api/data/illes",
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
                subtitle: {
                    text: "subtitle"
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


        function loadData(ev, itemName) {
            var serieName = itemName;
            var query = "municipio=" + itemName;
            if (itemName === "ALL") {
                serieName = ev.target.parentElement.parentElement.getAttribute('data-menu').substring(14);
                query = "isla=" + ev.target.parentElement.parentElement.getAttribute('data-menu').substring(14);
            }

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
                url: "api/data/municipioByDays?" + query,
                type: 'GET',
                success: function (resp, status) {
                    var obj = eval("(" + resp + ")");
                    var series = {
                        name: serieName,
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
                        series.data[val.pes] = val.total;
                    });

                    cao.series.push(series);
                }
            }).then(function () {
                $('#chart01').highcharts(cao);
            });


            $.ajax({
                url: "api/data/municipioByTime?" + query,
                type: 'GET',
                success: function (resp, status) {
                    var obj = eval("(" + resp + ")");
                    var series = {
                        name: serieName,
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
            })

            closeMenu();
        }
    });
});