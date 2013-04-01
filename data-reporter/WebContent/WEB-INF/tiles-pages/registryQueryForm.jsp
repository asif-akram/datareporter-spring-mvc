<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style type="text/css">
.simpletablestyle,  td {
	 padding:10px;
}
</style>

<title>Query Metadata Registry</title>
</head>
	<body>
	
		<form:form commandName="newRegistryQuery" id="reg" action="registryQuery.html">
		<h2>Query Metadata Registry</h2>
		
		<table>
			<tbody>
				<tr>
					<td><form:label path="registryBaseURL">Registry URL:</form:label></td>
					<td><form:input path="registryBaseURL" size="40"/></td>					
					<td><form:errors class="invalid" path="registryBaseURL" /></td>
				</tr>
				<tr>
					<td><form:label path="from">From:</form:label></td>
					<td><form:input path="from" /></td>
					<td><form:errors class="invalid" path="from" /></td>
				</tr>
				<tr>
					<td><form:label path="until">Until:</form:label>
					<td><form:input path="until" /></td>
					<td><form:errors class="invalid" path="until" /></td>
				</tr>
				<tr>
					<td><form:label path="numberOfRecords">Number of Records:</form:label>
					<td><form:input path="numberOfRecords" /></td>
					<td><form:errors class="invalid" path="numberOfRecords" /></td>
				</tr>

			</tbody>
		</table>
		<table>
			<tr>
				<td><input type="submit" value="Query"/>
				</td>
			</tr>
		</table>
	</form:form>
	
	</body>
</html>