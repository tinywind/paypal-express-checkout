<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>

<page:layout>
    <div class="span2">
    </div>
    <div class="span5">
        <form class="form" name="checkoutForm" action="/checkout" method="POST">
            <input type="hidden" name="L_PAYMENTREQUEST_0_AMT"
                   value="<c:out value="${fn:escapeXml(checkout.totalAmount)}" />">
            <div class="row-fluid">
                <div class="span6 inner-span">
                    <p class="lead">Shipping Address</p>
                    <table>
                        <tr>
                            <td width="30%">First Name:</td>
                            <td><input type="text" name="firstName" value="Alegra"/></td>
                        </tr>
                        <tr>
                            <td>Last Name:</td>
                            <td><input type="text" name="lastName" value="Valava"/></td>
                        </tr>
                        <tr>
                            <td>Address:</td>
                            <td><input type="text" name="address1" value="55 East 52nd Street"/>
                            </td>
                        </tr>
                        <tr>
                            <td>Address 1:</td>
                            <td><input type="text" name="address2" value="21st Floor"/></td>
                        </tr>
                        <tr>
                            <td>City:</td>
                            <td><input type="text" name="city" value="New York"/></td>
                        </tr>
                        <tr>
                            <td>State:</td>
                            <td><input type="text" name="state" value="NY"/></td>
                        </tr>
                        <tr>
                            <td>Postal Code:</td>
                            <td><input type="text" name="zipCode" value="10022"/></td>
                        </tr>
                        <tr>
                            <td>Country:</td>
                            <td>
                                <select name="country">
                                    <c:forEach var="type" items="${checkout.countryTypeOptions}">
                                        <option value="${type.value}">${type.key}</option>
                                    </c:forEach>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td>Telephone:</td>
                            <td><input type="text" name="PAYMENTREQUEST_0_SHIPTOPHONENUM" value="" maxlength="12"/></td>
                        </tr>

                        <tr>
                            <td colspan="2"><p class="lead">Shipping Detail:</p></td>
                        </tr>
                        <tr>
                            <td>Shipping Type:</td>
                            <td>
                                <select name="shippingAmount" id="shippingAmount" style="width: 250px;"
                                        class="required-entry">
                                    <option value="">Please select a shipping method...</option>
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
                            <td colspan="2"><p class="lead">Payment Methods:</p></td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <input checked id="paypal_payment_option" value="paypal_express" type="radio"
                                       name="payment_method" title="PayPal Express Check out" class="radio">
                                <img src="https://fpdbs.paypal.com/dynamicimageweb?cmd=_dynamic-image&amp;buttontype=ecmark&amp;locale=en_US"
                                     alt="Acceptance Mark" class="v-middle">&nbsp;
                                <a href="https://www.paypal.com/us/cgi-bin/webscr?cmd=xpt/Marketing/popup/OLCWhatIsPayPal-outside"
                                   onclick="javascript:window.open('https://www.paypal.com/us/cgi-bin/webscr?cmd=xpt/Marketing/popup/OLCWhatIsPayPal-outside','olcwhatispaypal','toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=yes, ,left=0, top=0, width=400, height=350'); return false;">What
                                    is PayPal?</a>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2" valign="top">
                                <input readonly="" disabled id="p_method_paypal_express" value="credit_card"
                                       type="radio" name="payment_method" title="PayPal Express Check out"
                                       class="radio">&nbsp;Credit Card
                            </td>
                            <td>
                            </td>
                        </tr>
                    </table>
                    <input type="submit" id="placeOrderBtn" class="btn btn-primary btn-large" name="PlaceOrder"
                           value="Place Order"/>
                </div>
            </div>

        </form>
    </div>
    <script type="text/javascript">
        window.paypalCheckoutReady = function () {
            paypal.checkout.setup('${gvApiUserName}', {
                button: 'placeOrderBtn',
                environment: '${environment}',
                condition: function () {
                    return !!document.getElementById('paypal_payment_option').checked;
                }
            });
        };
    </script>
    <script src="http://www.paypalobjects.com/api/checkout.js" async></script>
</page:layout>