<%@ include file="/WEB-INF/template/include.jsp"%>

<%@ include file="/WEB-INF/template/header.jsp"%>

<h2><spring:message code="imbpathologyrequest.title" /></h2>

<br/>
<table border="1">
  <tr>
   <th>User Id</th>
   <th>System ID</th>
   <th>User Name</th>
  </tr>
  <c:forEach var="user" items="${users}">
      <tr>
        <td>${user.userId}</td>
        <td>${user.systemId}</td>
        <td>${user.username}</td>
      </tr>		
  </c:forEach>
</table>

<%@ include file="/WEB-INF/template/footer.jsp"%>
