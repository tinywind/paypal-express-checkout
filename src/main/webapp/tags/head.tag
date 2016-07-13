<%@ tag pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--@elvariable id="meta" type="kr.tinywind.blog.model.BlogMeta"--%>

<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge, chrome=1">
    <link href="http://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link type="text/css" rel="stylesheet"
          href="<c:url value="/webjars/bootstrap/3.3.6/dist/css/bootstrap.min.css"/>"
          media="screen,projection"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link href="<c:url value="/css/bootstrap-responsive.min.css"/>" type="text/css" rel="stylesheet" media="screen,projection"/>
    <script type="text/javascript" src="<c:url value="/webjars/jquery/2.0.0/jquery.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/webjars/bootstrap/3.3.6/dist/js/bootstrap.min.js"/>"></script>
    <jsp:doBody/>
</head>
