<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>
<%--@elvariable id="paypal" type="org.tinywind.paypalexpresscheckout.config.PaypalConfig"--%>

<page:layout>
    <form class="form-horizontal col-xs-12" action="/checkout" method="post">
        <%--<input class="form-control" type="hidden" name="lTotalAmount" value="${checkoutRequest.totalAmount}">--%>
        <p class="lead">Shipping Address</p>
        <table class="table table-middle table-striped" style="width: 100%;">
            <tr>
                <td width="30%">First Name:</td>
                <td><input class="form-control" type="text" name="firstName" value="Alegra"/></td>
            </tr>
            <tr>
                <td>Last Name:</td>
                <td><input class="form-control" type="text" name="lastName" value="Valava"/></td>
            </tr>
            <tr>
                <td>Address:</td>
                <td><input class="form-control" type="text" name="address1" value="55 East 52nd Street"/>
                </td>
            </tr>
            <tr>
                <td>Address 1:</td>
                <td><input class="form-control" type="text" name="address2" value="21st Floor"/></td>
            </tr>
            <tr>
                <td>City:</td>
                <td><input class="form-control" type="text" name="city" value="New York"/></td>
            </tr>
            <tr>
                <td>State:</td>
                <td><input class="form-control" type="text" name="state" value="NY"/></td>
            </tr>
            <tr>
                <td>Postal Code:</td>
                <td><input class="form-control" type="text" name="zipCode" value="10022"/></td>
            </tr>
            <tr>
                <td>Country:</td>
                <td>
                    <select class="form-control" name="country">
                        <c:forEach var="type" items="${checkoutRequest.countryTypeOptions}">
                            <option value="${type.value}">${type.key}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td>Telephone:</td>
                <td><input class="form-control" type="text" name="phone" value="" maxlength="12"/></td>
            </tr>

            <tr>
                <td colspan="2">
                    <p class="lead">Shipping Detail:</p>
                </td>
            </tr>

            <tr>
                <td colspan="2">
                    <p class="lead">Payment Methods:</p>
                </td>
            </tr>
            <tr>
                <td class="2">
                    <div class="radio">
                        <label>
                            <input type="radio" id="paypal_payment_option" name="paymentMethod" value="PAYPAL_EXPRESS"
                                   checked>
                            <img src="https://fpdbs.paypal.com/dynamicimageweb?cmd=_dynamic-image&amp;buttontype=ecmark&amp;locale=en_US"
                                 alt="Acceptance Mark" class="v-middle">
                        </label>
                        <a href="https://www.paypal.com/us/cgi-bin/webscr?cmd=xpt/Marketing/popup/OLCWhatIsPayPal-outside"
                           onclick="javascript:window.open('https://www.paypal.com/us/cgi-bin/webscr?cmd=xpt/Marketing/popup/OLCWhatIsPayPal-outside',
                                   'olcwhatispaypal',
                                   'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=yes, resizable=yes, ,left=0, top=0, width=400, height=350');
                                    return false;">
                            What is PayPal?</a>
                    </div>
                </td>
                <td>
                    <div class="radio">
                        <label>
                            <input type="radio" name="paymentMethod" value="CREDIT_CARD">
                            Credit Card
                        </label>
                    </div>
                </td>
            </tr>
        </table>

        <button type="submit" id="placeOrderBtn" class="btn btn-primary btn-large">
            Place Order
        </button>
    </form>

    <page:scripts>
        <script type="text/javascript">
            window.paypalCheckoutReady = function () {
                paypal.checkout.setup('${paypalConfig.gvApiUserName}', {
                    button: 'placeOrderBtn',
                    environment: '${paypalConfig.environment}',
                    condition: function () {
                        return !!document.getElementById('paypal_payment_option').checked;
                    }
                });
            };
        </script>
        <script src="http://www.paypalobjects.com/api/checkout.js" async></script>
    </page:scripts>
</page:layout>