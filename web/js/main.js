$(document).ready(function () {
    var token = '311eee4f67111a1d6375a9451297790ce177817d5e7c84c012aa6429f0af7db31fca340eae8c05c947aba53be5e5ea25563bfc1e63f20043cbc38fe4d67cb972';
    $('.content').addClass('content-loading');
    $("#menus ul.main-menu_level").append("<li class='menu_item'><a data-submenu='submenu-islas' class='menu_link' href='#'>Islas</a></li>");

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

        $.ajax({
            url: 'api/chat/users',
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token
            },
            success: function(resp) {
                console.log(resp);
            }
        });

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
            var parent = ev.target.parentElement.parentElement.getAttribute('data-menu');
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
    });
});