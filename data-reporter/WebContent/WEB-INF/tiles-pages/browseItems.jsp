<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<h2>Items</h2>
	<c:choose>
					<c:when test="${fn:length(itemsNameList)==0}">
						<em>No registered Item.</em>
					</c:when>
					<c:otherwise>
						<table>
							<thead>
								<tr>									
									<th>Item Name</th>
									<th>Items URL</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${itemsNameList}" var="ItemName">
									<tr>
										<td>${ItemName}</td>
										<td><a href="<c:url value="/${siloName}/${ItemName}/browseDataset.html"/>">/browseDataset/${dataPackageName}</a></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</c:otherwise>
					</c:choose>
</body>
</html>