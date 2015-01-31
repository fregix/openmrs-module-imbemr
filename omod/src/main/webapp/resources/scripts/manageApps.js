var jq = jQuery;

jq(function(){
    jq('.imbemr-action').click(function(){
        jq(this).parent().submit();
    });
});

function showDeleteUserAppDialog(appId){
    var deleteUserAppDialog = emr.setupConfirmationDialog({
        selector: "#imbemr-delete-userApp-dialog-"+appId,
        actions: {
            cancel: function() {
                deleteUserAppDialog.close();
            }
        }
    });

    deleteUserAppDialog.show();
}