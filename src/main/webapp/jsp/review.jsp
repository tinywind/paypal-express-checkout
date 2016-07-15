<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>
<%--@elvariable id="request" type="org.tinywind.paypalexpresscheckout.model.CheckoutRequest"--%>
<%--@elvariable id="response" type="org.tinywind.paypalexpresscheckout.model.CheckoutResponse"--%>

<page:layout>
    <div class="span4"></div>
    <div class="span5">
        <table>
            <tbody>
            <tr>
                <td><h4>Shipping Address</h4></td>
                <td><h4>Billing Address</h4></td>
            </tr>
            <tr>
                <td>${response.shipToName}</td>
                <td>${response.shipToName}</td>
            </tr>
            <tr>
                <td>${request.address1}</td>
                <td>${response.shipToStreet}</td>
            </tr>
            <tr>
                <td>${request.city}</td>
                <td>${response.shipToCity}</td>
            </tr>
            <tr>
                <td>${request.state}</td>
                <td>${response.shipToState}</td>
            </tr>
            <tr>
                <td>${response.shipToCountryCode}</td>
                <td>${response.shipToCountryCode}</td>
            </tr>
            <tr>
                <td>${request.zipCode}</td>
                <td>${response.shipToZip}</td>
            </tr>
            <tr>
                <td colspan="2">&nbsp;</td>
            </tr>
            <tr>
                <td colspan="2">&nbsp;</td>
            </tr>

            <tr>
                <td>Total Amount:</td>
                <td id='amount'>${response.totalAmount}</td>
            </tr>
            <tr>
                <td>Currency Code:</td>
                <td>${response.currencyCode}</td>
            </tr>
            <tr>
                <td colspan="2">&nbsp;</td>
            </tr>
            <tr>
                <td colspan="2"><h3>Shipping Method</h3></td>
            </tr>
            <tr>
                <td colspan="2">
                    <form action="/return?page=return" method="post">
                        <table>
                            <tbody>
                            <tr>
                                <td>Shipping methods:</td>
                                <td>
                                    <select onchange="updateAmount();" name="shippingAmount" id="shippingAmount"
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
                                    </select>
                                    <br>
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    <button type="submit" class="btn btn-primary btn-large">
                                        Confirm Order
                                    </button>
                                    <input type="hidden" name="confirm" value="true"/>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </form>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
    <div class="span3"></div>
    <script>
        var requestTotalAmount =${request.totalAmount};
        var requestShippingAmount =${request.shippingAmount};
        function updateAmount() {
            var e = document.getElementById("shippingAmount");
            var shipAmt = parseInt(e.options[e.selectedIndex].value);
            var newAmt = shipAmt + requestTotalAmount - requestShippingAmount;
            document.getElementById("amount").innerHTML = newAmt + '.00';
        }
        $(document).ready(function () {
            updateAmount();
        });
    </script>
</page:layout>