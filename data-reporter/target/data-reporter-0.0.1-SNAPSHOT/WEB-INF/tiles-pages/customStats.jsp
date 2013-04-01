<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head>
    <title>Custom Statistics</title>
</head>
<body>
<h2>Statistics Manager</h2>
<form:form method="post" action="customStatsForm.html">
 
    <table>
    <tr>
        <td><form:label path="statsType">Select Statistics Type:</form:label></td>
        <td><form:input path="statsType" /></td>
    </tr>
    <tr>
        <td><form:label path="from">From:</form:label></td>
        <td><form:input path="from" /></td>
    </tr>
    <tr>
        <td><form:label path="to">To:</form:label></td>
        <td><form:input path="to" /></td>
    </tr>
    <tr>
        <td colspan="2">
            <input type="submit" value="View Stats"/>
        </td>
    </tr>
</table> 
     
</form:form>
</body>
</html>