<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="petclinic" tagdir="/WEB-INF/tags" %>

<petclinic:layout pageName="vaccinations">
    <jsp:attribute name="customScript">
        <script>
            $(function () {
                $("#date").datepicker({dateFormat: 'yy/mm/dd'});
            });
        </script>
    </jsp:attribute>
    <jsp:body>
    <h2>
        <c:if test="${vaccination['new']}">New </c:if> Vaccination
    </h2>
    <form:form modelAttribute="vaccination" class="form-horizontal" id="add-vaccination-form">
        <div class="form-group has-feedback">
            <petclinic:inputField label="Date" name="date"/>
            <div class="control-group">
                <petclinic:selectField name="vaccine" label="Vaccine " names="${vaccines}" size="5"/>
            </div>
            <div class="control-group">
                <petclinic:selectField name="vaccinatedPet" label="Pet" names="${pets}" size="5"/>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <c:choose>
                    <c:when test="${vaccination['new']}">
                        <button class="btn btn-default" type="submit">Add Vaccination</button>
                    </c:when>
                    <c:otherwise>
                        <button class="btn btn-default" type="submit">Update Vaccination</button>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
    </jsp:body>
</petclinic:layout>
