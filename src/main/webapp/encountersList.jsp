<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<html>
<head>
    <title>Encounter List</title>
</head>
<body>

<h1>Encounter List</h1>

<table>
    <thead>
    <tr>
        <th>ID</th>
        <th>Title</th>
        <th>Difficulty</th>
        <th>Description</th>
        <th>User</th>

    </tr>
    </thead>
    <tbody>
    <c:forEach var="encounter" items="${encounters}">
        <tr>
            <td>${encounter.id}</td>
            <td>${encounter.title}</td>
            <td>${encounter.difficulty}</td>
            <td>${encounter.description}</td>
            <td>${encounter.user.id}</td>
            <td>
                <ul>
                    <c:forEach var="character" items="${encounter.encounterCharacters}">
                        <li>${character.name}</li>
                    </c:forEach>
                </ul>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

</body>
</html>
