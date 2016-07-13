<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>

<page:layout>
    <span class="span4"></span>
    <span class="span5"><div class="hero-unit">You cancelled the order.</div></span>
    <span class="span3"></span>
</page:layout>

<% HttpSession nsession = request.getSession(false);
    if (nsession != null)
        session.invalidate();
%>