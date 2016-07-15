<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>
<%--@elvariable id="paypal" type="org.tinywind.paypalexpresscheckout.config.PaypalConfig"--%>

<page:layout>
    <div class="span2">
    </div>
    <div class="span5">
        <form class="form" action="/checkout" method="post">
            <input type="hidden" name="lTotalAmount" value="${checkout.totalAmount}">
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
                            <td><input type="text" name="phone" value="" maxlength="12"/></td>
                        </tr>

                        <tr>
                            <td colspan="2"><p class="lead">Shipping Detail:</p></td>
                        </tr>

                        <tr>
                            <td colspan="2"><p class="lead">Payment Methods:</p></td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <input checked id="paypal_payment_option" value="PAYPAL_EXPRESS" type="radio"
                                       name="paymentMethod" title="PayPal Express Check out" class="radio">
                                <img src="https://fpdbs.paypal.com/dynamicimageweb?cmd=_dynamic-image&amp;buttontype=ecmark&amp;locale=en_US"
                                     alt="Acceptance Mark" class="v-middle">&nbsp;
                                <a href="https://www.paypal.com/us/cgi-bin/webscr?cmd=xpt/Marketing/popup/OLCWhatIsPayPal-outside"
                                   onclick="javascript:window.open('https://www.paypal.com/us/cgi-bin/webscr?cmd=xpt/Marketing/popup/OLCWhatIsPayPal-outside',
                                   'olcwhatispaypal',
                                   'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=yes, ,left=0, top=0, width=400, height=350');
                                    return false;">
                                    What is PayPal?</a>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2" valign="top">
                                <input readonly="" disabled value="CREDIT_CARD" type="radio" name="paymentMethod"
                                       title="PayPal Express Check out" class="radio">&nbsp;Credit Card
                            </td>
                        </tr>
                    </table>

                    <button type="submit" id="placeOrderBtn" class="btn btn-primary btn-large">
                        Place Order
                    </button>
                </div>
            </div>
        </form>
    </div>

    <page:script>
        <script type="text/javascript">
            window.paypalCheckoutReady = function () {
                paypal.checkout.setup('${paypal.gvApiUserName}', {
                    button: 'placeOrderBtn',
                    environment: '${paypal.environment}',
                    condition: function () {
                        return !!document.getElementById('paypal_payment_option').checked;
                    }
                });
            };
        </script>
        <script src="http://www.paypalobjects.com/api/checkout.js" async></script>
    </page:script>
</page:layout>