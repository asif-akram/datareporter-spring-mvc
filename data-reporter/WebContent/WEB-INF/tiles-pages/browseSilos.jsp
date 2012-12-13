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
	<h2>Silos</h2>
	<c:choose>
					<c:when test="${fn:length(silosNameList)==0}">
						<em>No registered Silos.</em>
					</c:when>
					<c:otherwise>
						<table>
							<thead>
								<tr>									
									<th>Silo Name</th>
									<th>Dataset URL</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${silosNameList}" var="siloName">
									<tr>
										<td>${siloName}</td>
										<td><a href="<c:url value="/${siloName}/browseDataset.html"/>">/browseDataset/${siloName}</a></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</c:otherwise>
					</c:choose>
</body>
</html>