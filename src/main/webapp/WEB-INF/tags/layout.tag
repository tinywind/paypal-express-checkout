<%@ tag pageEncoding="utf-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>

<page:html>
    <page:head/>
    <page:body>
        <div class="container">
            <div class="well hidden-xs">
                <h2 class="text-center">Checkout with PayPal Demo</h2>
            </div>
            <div class="row">
                <jsp:doBody/>
            </div>
        </div>
        <page:js/>
        <page:scripts method="pop"/>
    </page:body>
</page:html>