<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Welcome Page</title>	
</head>
<body>
	
	<p> The Data Management Rollout at Oxford (DaMaRO) Project is creating a research data management policy for the University and 
					the infrastructure to enable researchers to comply with it.</p>
					
					
					<p>We will be taking the outputs of the various research data management project that the University has been engaged in over the 
					last few years and combining them into a better-integrated suite of tools and discovery mechanisms that will support researchers throughout 
					the data life-cycle, from planning to re-use.</p>

					
					
					<p>Of particular note is the "DataFinder" tool that DaMaRO will be developing. This will enable the discovery of data hosted 
					in various places around the University and beyond, including the Bodleian Libraries' 'DataBank' (developed through the Admiral and DataFlow projects), 
					the Database as a Service (DaaS) system (created during the Sudamih and VIDaaS Projects), departmental and other local data stores, the Web 2 research 
					management network "Colwiz", and hopefully the 'LabTrove' system developed by the University of Southampton. It will also connect this data with research 
					papers and publications held in the Oxford University Research Archive (ORA).</p>
					
					

					<p>Behind the scenes, DataFinder will be able to automatically gather metadata from each of the tools and repositories it connects to, 
					assign DOIs where they are not already assigned, and ensure that the metadata complies with the national DataCite standards. Furthermore, DataFinder
					 will make the metadata it gathers available as linked data, and also map it to the CERIF standard, so that alerts can be issued to compatible research 
					 management systems when new project outputs are made available.</p>
					 
					 

					<p>DataFinder will be designed so as to be implementable as a hierarchical structure, so that an institutional instance can harvest data 
					from departmental instances and, ultimately, a national instance could harvest the data from institutional instances, forming a UK data discovery tool.</p>
</body>
</html>