<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>
<%--@elvariable id="request" type="org.tinywind.paypalexpresscheckout.model.CheckoutRequest"--%>
<%--@elvariable id="response" type="org.tinywind.paypalexpresscheckout.model.CheckoutResponse"--%>

<page:layout>
    <div class="col-xs-12">
        <p>${checkoutRequest.toString()}</p>
        <p>${checkoutResponse.toString()}</p>
        <h3>Click <a href='index.jsp'>here </a> to return to Home Page</h3>
    </div>
</page:layout>