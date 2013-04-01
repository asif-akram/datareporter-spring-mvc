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
    	<td><select name="sel">
			<option value="byAuthor">By Author</option>
			<option value="byUniversity">By University</option>
			<option value="embargo">Embargo Content</option>
			<option value="notAccessed">Not Accessed</option>
		</select>    	
		</td>
    </tr>
    
     <tr>
        <td><form:label path="inputValue">Value:</form:label></td>
        <td><form:input path="inputValue" /></td>
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
    	<td><form:label path="reportType">Export Format:</form:label></td>
    	<td><select name="reportType" id="reportType">
			<option value="csv">CSV</option>
			<option value="xml">XML</option>
			<option value="graph">Graph</option>
		</select>    	
		</td>
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