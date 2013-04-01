<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<h2>Pre-set Reports</h2>
	
			<table>

				<thead>
					<tr>
						<th>Report Name</th>
						<th>Report URL</th>
					</tr>
				</thead>
				<tbody>					
						<tr>
							<td>DataFinder Snapshot</td>
							<td>
								<form action="snapshotReport.html" method="post">
									<table align="right" style="width:100%;">
										<tr align="left">
									    <td>From Date </td>
									    <td align="right"><input type="text" name="fromDate" /></td> </tr>
									    <tr>
									    <td>To Date: </td>
									    <td align="right"><input type="text" name="toDate" /></td>   </tr>
									    <tr>
									    <td></td>
									    <td align="right"><input type="submit" value="   Submit   " /></td></tr>
								    </table>
								</form>
							</td>
						</tr>
						<tr>
							<td>Full University Report</td>
							<td>
								<form action="universityReport.html" method="post">
									<table align="right" style="width:100%;">
										<tr>
									    <td>University Name</td>
									    <td align="right"><input type="text" name="universityName" /></td> </tr>
										<tr>
									    <td>From Date </td>
									    <td align="right"><input type="text" name="fromDate" /></td> </tr>
									    <tr>
									    <td>To Date: </td>
									    <td align="right"><input type="text" name="toDate" /></td>   </tr>
									    <tr>
									    <td></td>
									    <td align="right"><input type="submit" value="   Submit   " /></td></tr>
								    </table>
								</form>
							</td>
						</tr>
						<tr>
							<td>Departmental Report</td>
							<td>
								<form action="departmentReport.html" method="post">
									<table align="right" style="width:100%;">
										<tr>
									    <td>University Name</td>
									    <td align="right"><input type="text" name="universityName" /></td> </tr>
									    <tr>
									    <td>Department Name</td>
									    <td align="right"><input type="text" name="departmentName" /></td> </tr>
										<tr>
									    <td>From Date </td>
									    <td align="right"><input type="text" name="fromDate" /></td> </tr>
									    <tr>
									    <td>To Date: </td>
									    <td align="right"><input type="text" name="toDate" /></td>   </tr>
									    <tr>
									    <td></td>
									    <td align="right"><input type="submit" value="   Submit   " /></td></tr>
								    </table>
								</form>
							</td>
						</tr>
						<tr>
							<td>Divisional Report</td>
							<td>
								<form action="divisionReport.html" method="post">
									<table align="right" style="width:100%;">
										<tr>
									    <td>University Name</td>
									    <td align="right"><input type="text" name="universityName" /></td> </tr>
									    <tr>
									    <td>Division Name</td>
									    <td align="right"><input type="text" name="divisionName" /></td> </tr>
										<tr>
									    <td>From Date </td>
									    <td align="right"><input type="text" name="fromDate" /></td> </tr>
									    <tr>
									    <td>To Date: </td>
									    <td align="right"><input type="text" name="toDate" /></td>   </tr>
									    <tr>
									    <td></td>
									    <td align="right"><input type="submit" value="   Submit   " /></td></tr>
								    </table>
								</form>
							</td>
						</tr>
						<tr>
							<td>Personal Report</td>
							<td>
								<form action="personalReport.html" method="post">
									<table align="right" style="width:100%;">
										<tr>
									    <td>Person Name</td>
									    <td align="right"><input type="text" name="personName" /></td> </tr>
										<tr>
									    <td>From Date </td>
									    <td align="right"><input type="text" name="fromDate" /></td> </tr>
									    <tr>
									    <td>To Date: </td>
									    <td align="right"><input type="text" name="toDate" /></td>   </tr>
									    <tr>
									    <td ></td>
									    <td align="right"><input type="submit" value="   Submit   " /></td></tr>
								    </table>
								</form>
							</td>
						</tr>
						<tr>
							<td>Research Group Report</td>
							<td>
								<form action="researchGroupReport.html" method="post">
									<table align="right" style="width:100%;">
										<tr>
									    <td>ResearchGroup</td>
									    <td align="right"><input type="text" name="researchGroupName" /></td> </tr>
										<tr>
									    <td>From Date </td>
									    <td align="right"><input type="text" name="fromDate" /></td> </tr>
									    <tr>
									    <td>To Date: </td>
									    <td align="right"><input type="text" name="toDate" /></td>   </tr>
									    <tr>
									    <td></td>
									    <td align="right"><input type="submit" value="   Submit   " /></td></tr>
								    </table>
								</form>
							</td>
						</tr>
						<tr>
							<td>Funding Council Report</td>
							<td>
								<form action="fundingCouncilReport.html" method="post">
									<table align="right" style="width:100%;">
										<tr>
									    <td>Funding Council Name</td>
									    <td align="right"><input type="text" name="fundingCouncilName" /></td> </tr>
										<tr>
									    <td>From Date </td>
									    <td align="right"><input type="text" name="fromDate" /></td> </tr>
									    <tr>
									    <td>To Date: </td>
									    <td align="right"><input type="text" name="toDate" /></td>   </tr>
									    <tr>
									    <td></td>
									    <td align="right"><input type="submit" value="   Submit   " /></td></tr>
								    </table>
								</form>
							</td>
						</tr>
						<tr>
							<td>Individual record report</td>
							<td>
								<form action="individualRecordReport.html" method="post">
									<table align="right" style="width:100%;">
										<tr>
									    <td>Record ID</td>
									    <td align="right"><input type="text" name="recordID" /></td> </tr>
										<tr>
									    <td>From Date </td>
									    <td align="right"><input type="text" name="fromDate" /></td> </tr>
									    <tr>
									    <td>To Date: </td>
									    <td align="right"><input type="text" name="toDate" /></td>   </tr>
									    <tr>
									    <td></td>
									    <td align="right"><input type="submit" value="   Submit   " /></td></tr>
								    </table>
								</form>
							</td>
						</tr>
						<tr>
							<td>Records requiring retention assessment</td>
							<td>
								<form action="retentionAssessmentReport.html" method="post">
									<table align="right" style="width:100%;">
										<tr>
									    <td>Number of Years</td>
									    <td align="right"><input type="text" name="numberOfYears" /></td> </tr>
										
									    <tr>
									    <td></td>
									    <td align="right"><input type="submit" value="   Submit   " /></td></tr>
								    </table>
								</form>
							</td>
						</tr>
						<tr>
							<td>Records lacking mandatory fields</td>
							<td>
								<form action="lackManFieldsReport.html" method="post">
									<table align="right" style="width:100%;">										
									    <tr>
									    <td></td>
									    <td align="right"><input type="submit" value="   Submit   " /></td></tr>
								    </table>
								</form>
							</td>
						</tr>
						<tr>
							<td>Records lacking DOIs?</td>
							<td>
								<form action="missingDOIReport.html" method="post">
									<table align="right" style="width:100%;">										
									    <tr>
									    <td></td>
									    <td align="right"><input type="submit" value="   Submit   " /></td></tr>
								    </table>
								</form>
							</td>
						</tr>
					
				</tbody>
			</table>
</body>
</html>