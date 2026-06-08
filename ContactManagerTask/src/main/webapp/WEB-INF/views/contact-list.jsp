<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Contact Dashboard</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; background-color: #f8f9fa; }
        .dashboard-container { max-width: 900px; margin: 0 auto; background: white; padding: 30px; border-radius: 8px; box-shadow: 0 4px 15px rgba(0,0,0,0.05); }
        .toast { padding: 12px 20px; background-color: #d4edda; color: #155724; border: 1px solid #c3e6cb; margin-bottom: 20px; border-radius: 4px; font-weight: bold; }
        .search-wrapper { margin-bottom: 25px; display: flex; gap: 10px; }
        .search-bar { padding: 10px; width: 320px; border: 1px solid #ccc; border-radius: 4px; font-size: 14px; }
        .search-btn { padding: 10px 20px; background-color: #28a745; color: white; border: none; border-radius: 4px; cursor: pointer; font-weight: bold; }
        .clear-btn { padding: 10px 15px; background-color: #6c757d; color: white; text-decoration: none; border-radius: 4px; font-size: 14px; line-height: 20px; }
        table { width: 100%; border-collapse: collapse; margin-top: 10px; }
        th, td { border: 1px solid #e9ecef; padding: 14px; text-align: left; }
        th { background-color: #f1f3f5; color: #495057; font-weight: bold; }
        tr:hover { background-color: #f8f9fa; }
        .fab { position: fixed; bottom: 40px; right: 40px; background-color: #007bff; color: white; width: 60px; height: 60px; border-radius: 50%; text-align: center; line-height: 56px; font-size: 32px; text-decoration: none; box-shadow: 0 4px 12px rgba(0,123,255,0.3); transition: transform 0.2s; }
        .fab:hover { transform: scale(1.05); background-color: #0056b3; }
    </style>
</head>
<body>

<div class="dashboard-container">
    <h2>Contact Administration Dashboard</h2>

    <%-- Dynamic Status Confirmation Notifications --%>
    <c:if test="${not empty sessionScope.successToast}">
        <div class="toast">${sessionScope.successToast}</div>
        <c:remove var="successToast" scope="session" />
    </c:if>

    <%-- Optimized Server-side Search Form Container --%>
    <form action="${pageContext.request.contextPath}/contacts" method="GET" class="search-wrapper">
        <input type="text" name="search" value="<c:out value='${param.search}'/>" class="search-bar" placeholder="Search contacts by name or email...">
        <button type="submit" class="search-btn">Search</button>
        <c:if test="${not empty param.search}">
            <a href="${pageContext.request.contextPath}/contacts" class="clear-btn">Clear Filter</a>
        </c:if>
    </form>

    <table>
        <thead>
        <tr>
            <th>System ID</th>
            <th>Name</th>
            <th>Email</th>
            <th>Phone</th>
        </tr>
        </thead>
        <tbody>
        <c:choose>
            <%-- Read request-scoped list loaded by ContactServlet --%>
            <c:when test="${empty contacts}">
                <tr>
                    <td colspan="4" style="text-align: center; color: #868e96; padding: 30px;">No matching contacts found in the system.</td>
                </tr>
            </c:when>
            <c:otherwise>
                <c:forEach var="item" items="${contacts}">
                    <tr>
                        <td><strong>#<c:out value="${item.id}"/></strong></td>
                            <%-- XSS injection protection escaping wrappers --%>
                        <td><c:out value="${item.name}"/></td>
                        <td><c:out value="${item.email}"/></td>
                        <td>
                            <c:choose>
                                <c:when test="${not empty item.phone}"><c:out value="${item.phone}"/></c:when>
                                <c:otherwise><span style="color:#adb5bd; font-style:italic;">None supplied</span></c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </c:otherwise>
        </c:choose>
        </tbody>
    </table>
</div>

<%-- Floating Creation Trigger Action Button --%>
<a href="${pageContext.request.contextPath}/contacts/add" class="fab">+</a>

</body>
</html>