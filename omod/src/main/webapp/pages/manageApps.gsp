<%
    ui.decorateWith("appui", "standardEmrPage", [ title: ui.message("imbemr.app.manageApps.title") ])

    ui.includeJavascript("imbemr", "manageApps.js");

    ui.includeCss("imbemr", "manageApps.css");

%>

<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.message("coreapps.app.systemAdministration.label")}",
          link: "${ui.pageLink("coreapps", "systemadministration/systemAdministration")}"
        },
        { label: "${ ui.message("imbemr.app.manageApps.title")}"}
    ];
</script>

<% apps.each { app -> %>
    ${ui.includeFragment("imbemr", "deleteUserApp", [appId: app.id])}
<% } %>

<h2>${ ui.message("imbemr.app.manageApps.heading")}</h2>

<button class="confirm" onclick="location.href='${ ui.pageLink("imbemr, "userApp", [action: "add"]) }'">
    ${ ui.message("imbemr.app.addAppDefinition") }
</button>
</br></br>

<table>
    <thead>
    <tr>
        <th>${ ui.message("imbemr.app.appId.label")}</th>
        <th>${ ui.message("imbemr.app.status.label")}</th>
        <th>${ ui.message("imbemr.app.type.label")}</th>
        <th>${ ui.message("imbemr.app.actions.label")}</th>
    </tr>
    </thead>
    <% apps.each { app -> %>
    <tbody>
    <tr>
        <td>${app.id}</td>
        <td>
            <% if(app.enabled) { %>
            ${ui.message("imbemr.app.status.enabled")}
            <% } else { %>
            ${ui.message("imbemr.app.status.disabled")}
            <% } %>
        </td>
        <td>
            <% if(app.builtIn) { %>
            ${ui.message("imbemr.app.type.builtIn")}
            <% } else { %>
            ${ui.message("imbemr.app.type.implementationDefined")}
            <% } %>
        </td>
        <td>
            <form id="form-${app.id}" method="POST">
            <% if(app.enabled) { %>
                <i class="icon-stop stop-action imbemr-action"
                   title="${ ui.message("imbemr.app.action.disable") }"></i>
                <input type="hidden" name="id" value="${app.id}"/>
                <input type="hidden" name="action" value="disable" />
            <% } else { %>
                <i class="icon-play play-action imbemr-action"
                   title="${ ui.message("imbemr.app.action.enable") }"></i>
                <input type="hidden" name="id" value="${app.id}"/>
                <input type="hidden" name="action" value="enable" />
            <% } %>
            <% if(!app.builtIn) { %>
                <i class="icon-pencil edit-action" title="${ ui.message("general.edit") }"
                   onclick="location.href='${ui.pageLink("imbemr", "userApp", [appId: app.id, action: "edit"])}';"></i>
                <i class="icon-remove delete-action" title="${ ui.message("general.delete") }"
                   onclick="showDeleteUserAppDialog('${app.id}')"></i>
            <% } %>
            </form>
        </td>
    </tr>
    </tbody>
    <% } %>
</table>