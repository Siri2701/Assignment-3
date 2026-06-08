<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
  <title>Create Contact Entry</title>
  <style>
    .wrapper { width: 360px; margin: 40px auto; font-family: Arial, sans-serif; }
    .group { margin-bottom: 15px; }
    .group label { display: block; margin-bottom: 5px; font-weight: bold; }
    .group input { width: 100%; padding: 8px; box-sizing: border-box; border: 1px solid #ccc; }
    /* Highlight invalid fields in red */
    .invalid-input { border: 2px solid #dc3545 !important; background-color: #fff5f5; }
    .err-label { color: #dc3545; font-size: 0.85em; margin-top: 4px; display: block; }
    .alert { padding: 10px; margin-bottom: 15px; border-radius: 4px; background-color: #f8d7da; color: #721c24; }
    .spinner { display: none; margin-top: 10px; color: #666; font-style: italic; }
  </style>
</head>
<body>
<div class="wrapper">
  <h2>New Contact</h2>

  <%-- Server Error Alert Box --%>
  <c:if test="${not empty error}">
    <div class="alert">${error}</div>
  </c:if>

  <form action="${pageContext.request.contextPath}/contacts/post" method="POST" id="entryForm">
    <%-- Name Input Group --%>
    <div class="group">
      <label for="name">Name *</label>
      <input type="text" id="name" name="name" value="<c:out value='${presetName}'/>" class="${not empty errors.name ? 'invalid-input' : ''}">
      <c:if test="${not empty errors.name}">
        <span class="err-label">${errors.name}</span>
      </c:if>
    </div>

    <%-- Email Input Group --%>
    <div class="group">
      <label for="email">Email *</label>
      <input type="text" id="email" name="email" value="<c:out value='${presetEmail}'/>" class="${not empty errors.email ? 'invalid-input' : ''}">
      <c:if test="${not empty errors.email}">
        <span class="err-label">${errors.email}</span>
      </c:if>
    </div>

    <%-- Phone Input Group --%>
    <div class="group">
      <label for="phone">Phone</label>
      <input type="text" id="phone" name="phone" value="<c:out value='${presetPhone}'/>" class="${not empty errors.phone ? 'invalid-input' : ''}">
      <c:if test="${not empty errors.phone}">
        <span class="err-label">${errors.phone}</span>
      </c:if>
    </div>

    <button type="submit" id="saveBtn">Submit Contact</button>
    <%-- Loading spinner during form processing --%>
    <div id="spinner" class="spinner">Processing fields...</div>
  </form>
</div>

<script>
  // Auto-focus first invalid field on load
  window.onload = function() {
    var inputErr = document.querySelector('.invalid-input');
    if (inputErr) { inputErr.focus(); }
  };

  // Display loading spinner during form submission processing
  document.getElementById('entryForm').onsubmit = function() {
    document.getElementById('saveBtn').style.display = 'none';
    document.getElementById('spinner').style.display = 'block';
  };
</script>
</body>
</html>