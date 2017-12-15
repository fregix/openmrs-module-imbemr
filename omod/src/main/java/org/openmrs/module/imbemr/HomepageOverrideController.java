package org.openmrs.module.imbemr;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Spring MVC controller that takes over /index.htm and /login.htm so users would be able to
 * select a location for the session
 */

@Controller
public class HomepageOverrideController {

    @RequestMapping("/login.htm")
    public String showLoginHomepage() {
        return "forward:/imbemr/login.page";
    }

}
