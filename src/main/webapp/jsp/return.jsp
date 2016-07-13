<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>

<page:layout>
    <c:choose>
        <c:when test="${showResult == 1}">
            <div class="span4"></div>
            <div class="span5">
                <div class="hero-unit">
                    <!-- Display the Transaction Details-->
                    <h4> ${result.get("credit_card_first_name")}
                            ${result.get("credit_card_last_name")} , Thank you for your Order </h4>

                    <h4> Shipping Details: </h4>
                        ${result.get("L_PAYMENTREQUEST_FIRSTNAME")} ${result.get("L_PAYMENTREQUEST_LASTNAME")}<br>
                        ${result.get("PAYMENTREQUEST_0_SHIPTOSTREET")}<br>
                        ${result.get("PAYMENTREQUEST_0_SHIPTOCITY")}<br>
                    <p>${result.get("PAYMENTREQUEST_0_SHIPTOSTATE")}-${result.get("PAYMENTREQUEST_0_SHIPTOZIP")}</p>
                    <p>Transaction ID: ${result.get("TRANSACTIONID")}</p>
                    <p>Payment Total Amount: ${result.get("AMT")}</p>
                    <p>Currency Code: ${result.get("CURRENCYCODE")}</p>
                    <h3> Click <a href='index.jsp'>here </a> to return to Home Page</h3>

                </div>
            </div>
            <div class="span3"></div>
        </c:when>
        <c:when test="${showResult == 2}">
            <div class="span4"></div>
            <div class="span5">
                <div class="hero-unit">
                    <!-- Display the Transaction Details-->
                    <h4> ${result.get("FIRSTNAME")}
                            ${result.get("LASTNAME")} , Thank you for your Order </h4>

                    <h4> Shipping Details: </h4>
                        ${result.get("PAYMENTREQUEST_0_SHIPTONAME")}<br>
                        ${result.get("PAYMENTREQUEST_0_SHIPTOSTREET")}<br>
                        ${result.get("PAYMENTREQUEST_0_SHIPTOCITY")}<br>

                    <p>${result.get("PAYMENTREQUEST_0_SHIPTOSTATE")}- ${result.get("PAYMENTREQUEST_0_SHIPTOZIP")}</p>
                    <p>Transaction ID: ${result.get("PAYMENTINFO_0_TRANSACTIONID")} </p>
                    <p>Transaction Type: ${result.get("PAYMENTINFO_0_TRANSACTIONTYPE")} </p>
                    <p>Payment Total Amount: ${result.get("PAYMENTINFO_0_AMT")} </p>
                    <p>Currency Code: ${result.get("PAYMENTINFO_0_CURRENCYCODE")} </p>
                    <p>Payment Status: ${result.get("PAYMENTINFO_0_PAYMENTSTATUS")} </p>
                    <p>Payment Type: ${result.get("PAYMENTINFO_0_PAYMENTTYPE")} </p>
                    <h3> Click <a href='index.jsp'>here </a> to return to Home Page</h3>
                </div>
            </div>
            <div class="span3"></div>
        </c:when>
        <c:otherwise>
            <div class="hero-unit">
                <!-- Display the Transaction Details-->
                <h4> There is a Funding Failure in your account. You can modify your funding sources to fix it and make
                    purchase later. </h4>
                Payment Status: ${result.get("PAYMENTINFO_0_PAYMENTSTATUS")}
                <h3> Click <a href='https://www.sandbox.paypal.com/'>here </a> to go to PayPal site.</h3>
                <!--Change to live PayPal site for production-->
            </div>
        </c:otherwise>
    </c:choose>

</page:layout>