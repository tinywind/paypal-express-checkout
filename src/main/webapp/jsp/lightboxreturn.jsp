<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" tagdir="/WEB-INF/tags" %>

<page:layout>
    <div class="container-fluid">
        <div class="row-fluid">
            <div class="span4"></div>
            <div class="span5">
                <div class="row text-center"><h3>Loading...</h3></div>
                <script type="text/javascript">
                    if (window != top)
                        top.location.href = '/return?page=return&${url}';
                    else
                        window.location.href = '/return?page=return&${url}'; //return from full page paypal flow
                </script>
            </div>
        </div>
    </div>
</page:layout>