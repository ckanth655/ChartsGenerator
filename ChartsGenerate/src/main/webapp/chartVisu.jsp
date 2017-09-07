
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.5.0/Chart.min.js"></script>
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"
	type="text/javascript"></script>
<title>Insert title here</title>
</head>
<body>


<br>
<br>


  <div class="example-template" id="SelectBoxContainer">
   Index::<select class="index" id="index"  onchange=""></select>
   Field::<select class="field" id="field"></select>
   Chart Type::<select class="charttype" id="chartType"></select>
   <button class="del">Remove</button>
  </div>


<div class="edit-area">
  <div class="controls">
    <button class="add" id="btnAdd">Add</button>
    <button class="rem">Remove</button>
    <br>
    <br>
  </div>
</div>


<br>
<br>

<!-- onclick="dashBoards('value')" -->

<button id="btnGet" onclick="dashBoards('value')">Get Dashboards</button>

<div id="back"></div>

<table id="Table">
  
</table>

<!-- <label>kdie<span class="closebtn" onclick="this.parentElement.style.display='none';">&times;</span></label> -->


<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>

<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
<script
	src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/jquery-ui.min.js"></script>

<script src="resources/scriptFiles/dashboard.js"></script>	

</body>
</html>