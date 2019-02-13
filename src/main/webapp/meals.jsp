<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Meals</h2>
<table>
    <tr>
        <th>№</th>
        <th>id</th>
        <th>date-time</th>
        <th>description</th>
        <th>calories</th>
        <th>excess</th>
        <th colspan="2">action</th>
    </tr>
    <jsp:useBean id="list" scope="request" type="java.util.List"/>
    <jsp:useBean id="formatter" scope="request" type="java.time.format.DateTimeFormatter"/>
    <c:forEach var="mealTo" items="${list}" varStatus="i">
        <tr style="color: ${mealTo.excess == true ? 'red' : 'green'}">
            <td>${i.index + 1}</td>
            <td>${mealTo.id}</td>
            <td>${mealTo.dateTime.format(formatter)}</td>
            <td>${mealTo.description}</td>
            <td>${mealTo.calories}</td>
            <td>${mealTo.excess}</td>
            <c:url value="/meals" var="editUrl">
                <c:param name="id" value="${mealTo.id}"/>
                <c:param name="action" value="edit"/>
            </c:url>
            <td><a href="${editUrl}">edit</a></td>
            <c:url value="/meals" var="deleteUrl">
                <c:param name="id" value="${mealTo.id}"/>
                <c:param name="action" value="delete"/>
            </c:url>
            <td><a href="${deleteUrl}">delete</a></td>
        </tr>
    </c:forEach>
</table>
<c:url value="/meals" var="add">
    <c:param name="action" value="add"/>
</c:url>
<a href="${add}">Add new meal</a>
</body>
</html>
