<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>

<page:layout>
    <div class="span5">
        <form class="form" action="/checkout" method="post">
            <div class="row-fluid">
                <div class="span6 inner-span">
                    <!--Demo Product details -->
                    <table>
                        <tr>
                            <td colspan="2"><h3> DIGITAL SLR CAMERA </h3></td>
                        </tr>
                        <tr>
                            <td colspan="2"><img src="/img/camera.jpg" width="300" height="250"/></td>
                        </tr>
                        <tr>
                            <td colspan="2"><p class="lead"> Buyer Credentials:</p></td>
                        </tr>
                        <tr>
                            <td>Email-id:</td>
                            <td><input type="text" id="buyer_email" name="buyer_email" readonly/></td>
                        </tr>
                        <tr>
                            <td>Password:</td>
                            <td><input type="text" id="buyer_password" name="buyer_password" readonly/></td>
                        </tr>
                    </table>
                </div>
                <div class="span6 inner-span">
                    <p class="lead"> Item Specifications:</p>
                    <input type="hidden" name="logoImage" value="/img/logo.jpg"/>
                    <table>
                        <tr>
                            <td>Item Name:</td>
                            <td><input type="text" name="productName" value="DSLR Camera"/></td>
                        </tr>
                        <tr>
                            <td>Item ID:</td>
                            <td><input type="text" name="orderNumber" value="A0123"/></td>
                        </tr>
                        <tr>
                            <td>Description:</td>
                            <td><input type="text" name="productDescription" value="Autofocus Camera"/></td>
                        </tr>
                        <tr>
                            <td>Quantity:</td>
                            <td><input type="text" name="quantity" value="1" readonly/></td>
                        </tr>
                        <tr>
                            <td>Price:</td>
                            <td><input type="text" name="itemAmount" value="10.00" readonly/></td>
                        </tr>
                        <tr>
                            <td>Tax:</td>
                            <td><input type="text" name="taxAmount" value="2.00" readonly/></td>
                        </tr>
                        <tr>
                            <td>Shipping Amount:</td>
                            <td><input type="text" name="shippingAmount" value="5.00" readonly/></td>
                        </tr>
                        <tr>
                            <td>Handling Amount:</td>
                            <td><input type="text" name="handlingAmount" value="1.00" readonly/></td>
                        </tr>
                        <tr>
                            <td>Shipping Discount:</td>
                            <td><input type="text" name="shippingDiscount" value="-3.00" readonly/></td>
                        </tr>
                        <tr>
                            <td>Insurance Amount:</td>
                            <td><input type="text" name="insuranceAmount" value="2.00" readonly/></td>
                        </tr>
                        <tr>
                            <td>Total Amount:</td>
                            <td><input type="text" name="totalAmount" value="17.00" readonly/></td>
                        </tr>
                        <tr>
                            <td>Currency Code:</td>
                            <td>
                                <select id="currencyCodeType" name="currencyCodeType">
                                    <c:forEach var="type" items="${checkout.currencyCodeTypeOptions}"
                                               varStatus="status">
                                        <option value="${type.value}" ${status.first ? "selected" : ""}>${type.key}</option>
                                    </c:forEach>
                                </select>
                                <br>
                            </td>
                        </tr>
                        <tr>
                            <td>Payment Type:</td>
                            <td>
                                <select name="paymentType">
                                    <c:forEach var="type" items="${checkout.paymentTypeOptions}">
                                        <option value="${type.value}">${type.key}</option>
                                    </c:forEach>
                                </select>
                                <br>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <br/><br/>
                                <div id="myContainer"></div>
                            </td>
                        </tr>
                        <tr>
                            <td> -- OR --</td>
                        </tr>
                        <tr>
                            <td>
                                <input type="Submit" alt="Proceed to Check out" class="btn btn-primary btn-large"
                                       value="Proceed to Check out" name="checkout"/>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
        </form>
    </div>
    <div class="span2"></div>
    <div class="span5"></div>
    <div class="span1"></div>

    <page:script>
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

            document.getElementById("buyer_email").value = buyerCredentials[randomBuyer].email;
            document.getElementById("buyer_password").value = buyerCredentials[randomBuyer].password;
        </script>

        <script type="text/javascript">
            window.paypalCheckoutReady = function () {
                paypal.checkout.setup('${gvApiUserName}', {
                    container: 'myContainer',
                    environment: '${environment}'
                });
            };
        </script>
        <script src="http://www.paypalobjects.com/api/checkout.js" async></script>
    </page:script>
</page:layout>