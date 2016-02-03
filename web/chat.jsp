<%@include file="_header.jsp" %>  
<%@include file="_nav.jsp" %>  


<section  class="content">
    <div id="chat-msgs"></div>
    <form id="chat-form">
        <div>
            <input type="text" id="to"/>
        </div>
        <div>
            <input type="text" id="msg"/>
        </div>
        <div>
            <input type="submit" value="send"/>
        </div>
    </form>

</section>

</div><!-- /Container -->
<%@include file="_scripts.jsp" %> 

<script>
    $(document).ready(function () {
        var me = 'bob';

        function poll() {
            $.ajax({
                url: 'api/chat/read/' + me,
                type: 'GET',
                success: function (data) {
                    if (data != "") {
                        console.log(data);
                        $('#chat-msgs').append('<div><p>' + data + '</p></div>');
                    }
                    //poll();
                }
            }).always(poll);
        }

        $('#chat-form').submit(function () {
            var to = $('#to').val();
            var from = $('#from').val();
            var msg = $('#msg').val();

            $.ajax({
                url: 'api/chat/send',
                headers: {
                    'to': to,
                    'from': me,
                    'msg': msg
                },
                type: 'POST',
            });

            return false;
        });
        poll();
    })
</script>

<%@include file="_footer.jsp" %> 
