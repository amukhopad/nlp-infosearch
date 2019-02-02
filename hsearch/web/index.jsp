<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<form method="post" action="api/customers">
    <input name="firstName" placeholder="First name"/><br>
    <input name="lastName" placeholder="Last name"/><br>
    <input name="countryCode" placeholder="Country"/><br>
    <input type="submit" value="Submit"/><br>
</form>

<form method="get" action="api/customers/search">
    <input name="query" placeholder="Search query">
    <input type="submit" value="Submit"/>
</form>

<c:if test="${result ne null}">
    <c:forEach var="item" items="${result}">
        ${item}
    </c:forEach>
</c:if>
</body>
<head>
    <title>$Title$</title>
</head>
</html>
