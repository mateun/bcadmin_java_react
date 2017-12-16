<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
<link href="resources/css/starter-template.css" rel="stylesheet">
<title>Insert title here</title>
</head>
<body>
 

<div id="root">
</div>

<script src="//code.jquery.com/jquery-1.10.2.min.js"></script>
<script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>
<script src="resources/js/bundle.js"></script>
    
<script type="text/javascript">
console.log("hello");localStorage.setItem('foo', "mysupertoken");
var obj = localStorage.getItem('foo');
console.log("got obj back: " + obj);

</script>
</body>
</html>