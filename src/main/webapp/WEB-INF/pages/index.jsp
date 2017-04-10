<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <script type="text/javascript" src="https://code.jquery.com/jquery-1.12.4.min.js"></script>
    <script type='text/javascript'>
        $(window).load(function(){
            $("#searchInput").keyup(function () {
                //split the current value of searchInput
                var data = this.value.toUpperCase().split(" ");
                //create a jquery object of the rows
                var jo = $("p");
                if (this.value == "") {
                    jo.show();
                    return;
                }
                //hide all the rows
                jo.hide();

                //Recusively filter the jquery object to get results.
                jo.filter(function (i, v) {
                    var $t = $(this);
                    for (var d = 0; d < data.length; ++d) {
                        if ($t.text().toUpperCase().indexOf(data[d]) > -1) {
                            return true;
                        }
                    }
                    return false;
                })
                //show the rows that match.
                    .show();
            }).focus(function () {
                this.value = "";
                $(this).css({
                    "color": "black"
                });
                $(this).unbind('focus');
            }).css({
                "color": "#C0C0C0"
            });
        });
    </script>
</head>
<body>
<c:if test="${empty data}" >
<input id="searchInput" value=""> <br><br>

<c:forEach var="offer" items="${offersList}" >
<p><a href="/${offer.id}">${offer}</a> </p>
</c:forEach>
</c:if>

<c:if test="${not empty data}" >
    <p> ${data}</p>
    <a href="/">back</a> <br>
</c:if>

<br><br><br>
<a href="/refresh"><b>refresh offer list <i>(last update ${offerListUpdate})</i></b></a>

</body>
</html>