<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<html>
<head>
    <title>Monster List</title>
</head>
<body>

<h1>Monster List</h1>

<table>
    <thead>
    <tr>
        <th>API Index</th>
        <th>Name</th>
        <th>API URL</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="monster" items="${monstersResults}">
        <tr>
            <td>${monster.index}</td>
            <td>${monster.name}</td>
            <td>${monster.url}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>

</body>
</html>
