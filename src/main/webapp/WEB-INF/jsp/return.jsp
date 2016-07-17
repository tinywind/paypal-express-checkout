<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>
<%--@elvariable id="request" type="org.tinywind.paypalexpresscheckout.model.CheckoutRequest"--%>
<%--@elvariable id="response" type="org.tinywind.paypalexpresscheckout.model.CheckoutResponse"--%>

<page:layout>
    <div class="col-xs-12">
        <c:choose>
            <c:when test="${byCreditCard}">
                <h4>${response.creditCardFirstName} ${response.creditCardLastName}, Thank you for your Order </h4>
                <h4>Shipping Details: </h4>
                <p>${request.firstName} ${request.lastName}</p>
                <p>${request.address1}</p>
                <p>${request.address2}</p>
                <p>${request.city}</p>
                <p>${request.state}-${request.zipCode}</p>
                <p>Transaction ID: ${response.transactionId}</p>
                <p>Payment Total Amount: ${response.paymentTotalAmount}</p>
                <p>Currency Code: ${response.currencyCode}</p>
                <h3>Click <a href='index.jsp'>here </a> to return to Home Page</h3>
            </c:when>
            <c:otherwise>
                <h4>${request.firstName} ${request.lastName}, Thank you for your Order </h4>
                <h4>Shipping Details: </h4>
                <p>${response.shipToName}</p>
                <p>${request.address1}</p>
                <p>${request.address2}</p>
                <p>${request.city}</p>
                <p>${request.state}-${request.zipCode}</p>
                <p>Transaction ID: ${response.transactionId2}</p>
                <p>Transaction Type: ${response.transactionType}</p>
                <p>Payment Total Amount: ${response.paymentTotalAmount2}</p>
                <p>Currency Code: ${response.currencyCode2}</p>
                <p>Payment Status: ${response.paymentStatus}</p>
                <p>Payment Type: ${response.paymentType}</p>
                <h3>Click <a href='index.jsp'>here </a> to return to Home Page</h3>
            </c:otherwise>
        </c:choose>
    </div>
</page:layout>