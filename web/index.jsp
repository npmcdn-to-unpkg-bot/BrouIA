<%@include file="_header.jsp" %>  
<%@include file="_nav.jsp" %>  
<script type="text/javascript">
    $(function () { 
    $('#container').highcharts({
        chart: {
            type: 'bar'
        },
        title: {
            text: 'Fruit Consumption'
        },
        xAxis: {
            categories: ['Apples', 'Bananas', 'Oranges']
        },
        yAxis: {
            title: {
                text: 'Fruit eaten'
            }
        },
        series: [{
            name: 'Jane',
            data: [1, 0, 4]
        }, {
            name: 'John',
            data: [5, 7, 3]
        }]
    });
});

</script>

<section  class="content">
    <p class="info">Hola</p>
    
     <div id="container" style="width:100%; height:400px;"></div>
    
</section>

 
<%@include file="_footer.jsp" %> 
