<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <c:set var="title" value="${param.action == 'add' ? 'Add' : 'Edit'}"/>
    <title>${title}</title>
</head>
<body>
<jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.Meal"/>
<c:url value="/meals" var="addUrl">
    <c:param name="action" value="add"/>
</c:url>
<c:url value="/meals" var="editUrl">
    <c:param name="id" value="${meal.id}"/>
    <c:param name="action" value="edit"/>
</c:url>
<form action="${param.action == 'add' ? addUrl : editUrl}" name="meal" method="POST">
    <c:if test="${param.action == 'add'}">
        <p>Add new meal</p>
    </c:if>
    <c:if test="${param.action == 'edit'}">
        <p>Edit meal</p>
        <input type="hidden" name="id" value="${meal.id}">
    </c:if>
    <p><input type="datetime-local" name="dateTime" placeholder="dateTime" value="${meal.dateTime}" required>
    <p><input type="text" name="description" placeholder="description" value="${meal.description}" required>
    <p><input type="text" name="calories" placeholder="calories" value="${meal.calories}" required>
    <p><input type="submit" value="${param.action == 'add' ? 'add' : 'edit'}">
</form>
</body>
</html>
