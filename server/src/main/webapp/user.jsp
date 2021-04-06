<%@ page import="com.etheapp.brainserver.logic.GGame" %>
<%@ page import="com.etheapp.brainserver.logic.Score" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.etheapp.brainserver.logic.Scores" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%
    GGame game = (GGame) session.getServletContext().getAttribute("Game");
    String uuid = request.getParameter("uuid");
    SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm");
%>
<html>
<head>
    <title><%=game.getPlayer(uuid).getName()%>
    </title>
    <style>
        table {
            border-collapse: collapse;
            border: 2px solid grey;
        }

        td {
            border: 1px solid grey;
            padding: 4px;
            font-size: small;
        }

    </style>
</head>
<body>
<h3><a href="list.jsp">[TOP]</a>&nbsp;&nbsp;<%=game.getPlayer(uuid).getName()%>
</h3>
<%=uuid%><br><br>

<%
    for (int level = 3; level <= 5; level++) {
        if (game.getPlayer(uuid).hasScores(level)) {
            Scores scores = game.getPlayer(uuid).getScores(level);
%>
level <%=level%> best <%=scores.getBest().getValue()%><br>
<table>
    <%for (Score score : scores.getList()) {%>
    <tr>
        <td><%=sdf.format(score.getDate())%>
        </td>
        <td><%=score.getValue()%>
        </td>
    </tr>
    <%}%>
</table>
<br>
<%
        }
    }
%>

</body>
</html>
