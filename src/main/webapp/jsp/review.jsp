<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>

<page:layout>
    <div class="span4"></div>
    <div class="span5">
        <table>
            <tbody>
            <tr>
                <td><h4>Shipping Address</h4></td>
                <td><h4>Billing Address</h4></td>
            </tr>
            <% HashMap result = (HashMap) request.getAttribute("result"); %>
            <tr>
                <td><%=result.get("PAYMENTREQUEST_0_SHIPTONAME")%>
                </td>
                <td><%=result.get("PAYMENTREQUEST_0_SHIPTONAME")%>
                </td>
            </tr>
            <tr>
                <td><%=result.get("PAYMENTREQUEST_0_SHIPTOSTREET")%>
                </td>
                <td><%=result.get("PAYMENTREQUEST_0_SHIPTOSTREET")%>
                </td>
            </tr>
            <tr>
                <td><%=result.get("PAYMENTREQUEST_0_SHIPTOCITY")%>
                </td>
                <td><%=result.get("PAYMENTREQUEST_0_SHIPTOCITY")%>
                </td>
            </tr>
            <tr>
                <td><%=result.get("PAYMENTREQUEST_0_SHIPTOSTATE")%>
                </td>
                <td><%=result.get("PAYMENTREQUEST_0_SHIPTOSTATE")%>
                </td>
            </tr>
            <tr>
                <td><%=result.get("PAYMENTREQUEST_0_SHIPTOCOUNTRYCODE")%>
                </td>
                <td><%=result.get("PAYMENTREQUEST_0_SHIPTOCOUNTRYCODE")%>
                </td>
            </tr>
            <tr>
                <td><%=result.get("PAYMENTREQUEST_0_SHIPTOZIP")%>
                </td>
                <td><%=result.get("PAYMENTREQUEST_0_SHIPTOZIP")%>
                </td>
            </tr>
            <tr>
                <td colspan="2">&nbsp;</td>
            </tr>
            <tr>
                <td colspan="2">&nbsp;</td>
            </tr>

            <tr>
                <td>Total Amount:</td>
                <td id='amount'><%=result.get("PAYMENTREQUEST_0_AMT")%>
                </td>
            </tr>
            <tr>
                <td>Currency Code:</td>
                <td><%=result.get("CURRENCYCODE")%>
                </td>
            </tr>
            <tr>
                <td>&nbsp;</td>
            </tr>
            <tr>
                <td><h3>Shipping Method</h3></td>
            </tr>
            <form action="Return?page=return" name="order_confirm" method="POST">
                <table>
                    <tbody>
                    <tr>
                        <td>Shipping methods:</td>
                        <td>
                            <select onchange="updateAmount();" name="shipping_method" id="shipping_method"
                                    style="width: 250px;" class="required-entry">
                                <optgroup label="United Parcel Service" style="font-style:normal;">
                                    <option value="2.00">
                                        Worldwide Expedited - $2.00
                                    </option>
                                    <option value="3.00">
                                        Worldwide Express Saver - $3.00
                                    </option>
                                </optgroup>
                                <optgroup label="Flat Rate" style="font-style:normal;">
                                    <option selected value="0.00">
                                        Fixed - $0.00
                                    </option>
                                </optgroup>
                            </select><br></td>
                    </tr>
                    <tr>
                        <td><input type="Submit" name="confirm" alt="Check out with PayPal"
                                   class="btn btn-primary btn-large" value="Confirm Order"></td>
                    </tr>
                    </tbody>
                </table>
            </form>
            </tbody>
        </table>
    </div>
    <div class="span3">
    </div>
    <script>
        var origAmt =<%=result.get("PAYMENTREQUEST_0_AMT")%>;
        var oldshipAmt =<%=result.get("PAYMENTREQUEST_0_SHIPPINGAMT")%>;
        function updateAmount() {
            var e = document.getElementById("shipping_method");
            var shipAmt = parseInt(e.options[e.selectedIndex].value);
            var newAmt = shipAmt + origAmt - oldshipAmt;
            document.getElementById("amount").innerHTML = newAmt + '.00';
        }
        $(document).ready(function () {
            // any code goes here
            updateAmount();
        });
    </script>
</page:layout>