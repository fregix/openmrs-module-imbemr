<%
    ui.decorateWith("appui", "standardEmrPage", [ title: ui.message("imbemr.app.userApp."+param.action[0]) ])

    ui.includeJavascript("imbemr", "userApp.js");
%>

<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message("coreapps.app.systemAdministration.label")}",
            link: "${ui.pageLink("coreapps", "systemadministration/systemAdministration")}"
        },
        { label: "${ ui.message("imbemr.app.manageApps.title")}",
            link: "${ui.pageLink("imbemr", "manageApps")}"
        },
        { label: "${ ui.message("imbemr.app.userApp."+param.action[0])}"}
    ];

    jq(function(){
        setAction('${param.action[0]}');
    });
</script>

<h2>${ ui.message("imbemr.app.userApp."+param.action[0])}</h2>

<form class="simple-form-ui" method="POST" action="userApp.page">
    <span id="errorMsg" class="field-error" style="display: none">
        ${ui.message("imbemr.app.errors.invalidJson")}
    </span>
    <span id="server-error-msg" class="field-error" style="display: none">
        ${ui.message("imbemr.app.errors.serverError")}
    </span>
    <input type="hidden" name="action" value="${param.action[0]}" />
    <p>
        <%if(param.action[0] == 'edit'){%>
        <span class="title">
        ${ui.message("imbemr.app.appId.label")}:
        </span>&nbsp;${ui.escapeHtml(userApp.appId)}
        <input id="appId-field-hidden" type="hidden" name="appId" value="${userApp.appId ? userApp.appId : ""}" />
        <%} else{%>
        <label for="appId-field">
            <span class="title">
                ${ui.message("imbemr.app.appId.label")} (${ ui.message("emr.formValidation.messages.requiredField.label") })
            </span>
        </label>
        <input id="appId-field" type="text" class="required" name="appId" value="${userApp.appId ? ui.escapeJs(ui.escapeHtml(userApp.appId)) : ""}" size="80" placeholder="${ ui.message("imbemr.app.definition.placeholder") }" />
        <%}%>
    </p>
    <p>
        <label for="json-field">
            <span class="title">
            ${ui.message("imbemr.app.definition.label")} (${ ui.message("emr.formValidation.messages.requiredField.label") })
            </span>
        </label>
        <textarea id="json-field" class="required" name="json" rows="15" cols="80">${userApp.json ? userApp.json : ""}</textarea>
    </p>

    <input type="button" class="cancel" value="${ ui.message("general.cancel") }" onclick="javascript:window.location='/${ contextPath }/imbemr/manageApps.page'" />
    <input type="submit" class="confirm right" id="save-button" value="${ ui.message("general.save") }" disabled="disabled" />
</form>