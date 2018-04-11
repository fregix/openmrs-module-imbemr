HtmlForms should be kept version controlled within this directory or a subdirectory.

This will allow live re-loading of these htmlforms during development, and also will enable us to configure the system to 
ensure these forms are up-to-date in the deployed application.

To enable changes to edited forms to be immediately available during development:

1. Put your forms into this directory (or a sub-directory).  These file names should be URL friendly (eg. no spaces, use camel case)
2. Enable development mode on this module
  * Using the SDK, assuming a serverId = "rwink":
    * Go into the root directory for this module code (eg. ~/code/imbemr)
    * From this directory, run "mvn openmrs-sdk:watch -DserverId=rwink"
3. Start up the server
  * Using the SDK, assuming a serverId = "rwink":
    * mvn openmrs-sdk:run -DserverId=rwink
4. Load the htmlform using the htmlformentryui module's URL
  * For example, for the form named "test.xml" that is included here, choose a specific patient uuid that exists, and go to:
    * http://localhost:8080/openmrs/htmlformentryui/htmlform/enterHtmlFormWithStandardUi.page?patientId={{patient.uuid}}&definitionUiResource=imbemr:htmlforms/test.xml
5. Verify that changing the form will allow you to hot-reload changes:
  * Make some sort of a change to "test.xml"
  * Re-load the page above and confirm that the change is there
  
