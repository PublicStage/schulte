<%@ page import="com.etheapp.brainserver.logic.GGame" %>
<%@ page import="com.etheapp.brainserver.logic.Scores" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%
    int level = 3;
    if (request.getParameter("level") != null) {
        level = Integer.parseInt(request.getParameter("level"));
    }
    GGame game = (GGame) session.getServletContext().getAttribute("Game");

    SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm");

%>
<html>
<head>
    <title>Список</title>
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
<h3>TOP results</h3>
<a href='list.jsp?level=3'>[3x3]</a>&nbsp;&nbsp;<a href='list.jsp?level=4'>[4x4]</a>&nbsp;&nbsp;<a
        href='list.jsp?level=5'>[5x5]</a>&nbsp;&nbsp;
<a href='restart'>[restart]</a>&nbsp;&nbsp;
<br><br>
<table>
    <%for (Scores item : game.getTopContainer().get(level).getListCopy()) {%>
    <tr>
        <td><a href="user.jsp?uuid=<%=item.getPlayer().getUuid()%>"><%=item.getPlayer().getName()%>&nbsp;</a></td>
        <td><%=item.getBest().getValue()%>
        </td>
        <td><%=sdf.format(item.getBest().getDate())%>
        </td>
    </tr>
    <%}%>
</table>
</br>
</body>
</html>
