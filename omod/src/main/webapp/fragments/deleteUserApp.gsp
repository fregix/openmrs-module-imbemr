<%
    config.require("appId");
%>

<div id="imbemr-delete-userApp-dialog-${config.appId}" class="dialog" style="display: none">
    <div class="dialog-header">
        <h3>${ ui.message("imbemr.app.deleteUserAppDefinition") }</h3>
    </div>
    <div class="dialog-content">
        <ul>
            <li class="info">
                ${ ui.message("imbemr.app.deleteUserApp.message", "<b>"+config.appId+"</b>") }
            </li>
        </ul>
        <form method="POST" action="manageApps.page">
            <input type="hidden" name="id" value="${config.appId}"/>
            <input type="hidden" name="action" value="delete"/>
            <button class="confirm right" type="submit">${ ui.message("general.yes") }</button>
            <button class="cancel">${ ui.message("general.no") }</button>
        </form>
    </div>
</div>