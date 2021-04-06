<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.etheapp.brainserver.Version" %>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<html>
<head>
    <title>brainserver</title>
    <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/favicon.png"/>
</head>
<body>
BrainServer version of <%=Version.version()%>
</body>
</html>
