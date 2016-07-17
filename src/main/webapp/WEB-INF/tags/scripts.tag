<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--@elvariable id="tagExtender" type="kr.tinywind.blog.util.TagExtender"--%>

<%@attribute name="method" required="false" type="java.lang.String" %>

<jsp:doBody var="TAG_SCRIPTS" scope="request"/>

${tagExtender.stackBody("TAG_SCRIPTS", "TAG_SCRIPTS_LIST")}

<c:choose>
    <c:when test="${method eq 'pop'}">
        <c:forEach items="${tagExtender.getBody('TAG_SCRIPTS_LIST')}" var="i">
            <c:out value="${i}" escapeXml="false"/>
        </c:forEach>
    </c:when>
</c:choose>
