<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>
<%--@elvariable id="paypal" type="org.tinywind.paypalexpresscheckout.config.PaypalConfig"--%>

<page:layout>
    <form class="form-horizontal col-xs-12" action="/checkout" method="post">
        <div class="row">
            <div class="col-xs-12 col-sm-6">
                <table class="table table-middle table-striped" style="width: 100%;">
                    <tr>
                        <td colspan="2"><h3> DIGITAL SLR CAMERA </h3></td>
                    </tr>
                    <tr>
                        <td colspan="2"><img src="/img/camera.jpg" width="300"/></td>
                    </tr>
                    <tr>
                        <td colspan="2"><p class="lead"> Buyer Credentials:</p></td>
                    </tr>
                    <tr>
                        <td>Email-id:</td>
                        <td><input class="form-control" type="text" id="buyerEmail" name="buyerEmail" readonly/></td>
                    </tr>
                    <tr>
                        <td>Password:</td>
                        <td><input class="form-control" type="text" id="buyerPassword" name="buyerPassword" readonly/>
                        </td>
                    </tr>
                </table>
            </div>
            <div class="col-xs-12 col-sm-6">
                <p class="lead"> Item Specifications:</p>
                <table class="table table-middle table-striped" style="width: 100%;">
                    <tr>
                        <td>Item Name:</td>
                        <td><input class="form-control" type="text" name="productName" value="DSLR Camera"/></td>
                    </tr>
                    <tr>
                        <td>Item ID:</td>
                        <td><input class="form-control" type="text" name="orderNumber" value="A0123"/></td>
                    </tr>
                    <tr>
                        <td>Description:</td>
                        <td><input class="form-control" type="text" name="productDescription" value="Autofocus Camera"/>
                        </td>
                    </tr>
                    <tr>
                        <td>Quantity:</td>
                        <td><input class="form-control" type="text" name="quantity" value="1" readonly/></td>
                    </tr>
                    <tr>
                        <td>Price:</td>
                        <td><input class="form-control" type="text" name="itemAmount" value="10.00" readonly/></td>
                    </tr>
                    <tr>
                        <td>Tax:</td>
                        <td><input class="form-control" type="text" name="taxAmount" value="2.00" readonly/></td>
                    </tr>
                    <tr>
                        <td>Shipping Amount:</td>
                        <td><input class="form-control" type="text" name="shippingAmount" value="5.00" readonly/></td>
                    </tr>
                    <tr>
                        <td>Handling Amount:</td>
                        <td><input class="form-control" type="text" name="handlingAmount" value="1.00" readonly/></td>
                    </tr>
                    <tr>
                        <td>Shipping Discount:</td>
                        <td><input class="form-control" type="text" name="shippingDiscount" value="-3.00" readonly/>
                        </td>
                    </tr>
                    <tr>
                        <td>Insurance Amount:</td>
                        <td><input class="form-control" type="text" name="insuranceAmount" value="2.00" readonly/></td>
                    </tr>
                    <tr>
                        <td>Currency Code:</td>
                        <td>
                            <select class="form-control" id="currencyCode" name="currencyCode">
                                <c:forEach var="type" items="${checkout.currencyCodeOptions}">
                                    <option value="${type.value}"
                                        ${type.value == "USD" ? "selected" : ""}>${type.key}</option>
                                </c:forEach>
                            </select>
                            <br>
                        </td>
                    </tr>
                    <tr>
                        <td>Payment Type:</td>
                        <td>
                            <select class="form-control" name="paymentType">
                                <c:forEach var="type" items="${checkout.paymentTypeOptions}">
                                    <option value="${type.value}">${type.key}</option>
                                </c:forEach>
                            </select>
                            <br>
                        </td>
                    </tr>
                </table>
            </div>
            <div class="col-xs-12">
                <input type="hidden" name="shortcut" value="true"/>
                <button type="button" id="myContainer"></button>
                <button type="button" class="btn btn-primary btn-large"
                        onclick="$('[name=shortcut]').val('false'); $(this).closest('form').submit();">
                    Proceed to Check out
                </button>
            </div>
        </div>
    </form>

    <page:scripts>
        <script type="text/javascript">
            function getRandomNumberInRange(min, max) {
                return Math.floor(Math.random() * (max - min) + min);
            }

            var buyerCredentials = [{"email": "ron@hogwarts.com", "password": "qwer1234"},
                {"email": "sallyjones1234@gmail.com", "password": "p@ssword1234"},
                {"email": "joe@boe.com", "password": "123456789"},
                {"email": "hermione@hogwarts.com", "password": "123456789"},
                {"email": "lunalovegood@hogwarts.com", "password": "123456789"},
                {"email": "ginnyweasley@hogwarts.com", "password": "123456789"},
                {"email": "bellaswan@awesome.com", "password": "qwer1234"},
                {"email": "edwardcullen@gmail.com", "password": "qwer1234"}];
            var randomBuyer = getRandomNumberInRange(0, buyerCredentials.length);

            document.getElementById("buyerEmail").value = buyerCredentials[randomBuyer].email;
            document.getElementById("buyerPassword").value = buyerCredentials[randomBuyer].password;

            window.paypalCheckoutReady = function () {
                paypal.checkout.setup('${paypalConfig.gvApiUserName}', {
                    container: 'myContainer',
                    environment: '${paypalConfig.environment}'
                });
            };
        </script>

        <script src="http://www.paypalobjects.com/api/checkout.js" async></script>
    </page:scripts>
</page:layout>