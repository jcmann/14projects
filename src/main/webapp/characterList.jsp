<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<html>
<head>
    <title>Character List</title>
</head>
<body>

    <h1>Character List</h1>

    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Level</th>
                <th>Race</th>
                <th>Class</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="character" items="${characters}">
                <tr>
                    <td>${character.id}</td>
                    <td>${character.name}</td>
                    <td>${character.level}</td>
                    <td>${character.race}</td>
                    <td>${character.characterClass}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

</body>
</html>
