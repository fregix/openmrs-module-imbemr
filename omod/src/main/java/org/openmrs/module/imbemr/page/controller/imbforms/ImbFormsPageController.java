package org.openmrs.module.imbemr.page.controller.imbforms;

import org.openmrs.module.appframework.domain.Extension;
import org.openmrs.module.appframework.service.AppFrameworkService;
import org.openmrs.module.appui.UiSessionContext;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;

import java.util.Collections;
import java.util.List;

/**
 * Created by cosmin on 2/2/15.
 */
public class ImbFormsPageController {

    public static final String IMB_FORMS_EXTENSION_POINT = "imbForms.apps";

    public void controller(PageModel model, UiSessionContext emrContext,
                           @SpringBean("appFrameworkService") AppFrameworkService appFrameworkService) {

        emrContext.requireAuthentication();

        List<Extension> extensions = appFrameworkService.getExtensionsForCurrentUser(IMB_FORMS_EXTENSION_POINT);

        Collections.sort(extensions);
        model.addAttribute("extensions", extensions);
    }
}
