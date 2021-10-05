package org.openmrs.module.imbpathologyrequest.web.controller;

import org.openmrs.User;
import org.openmrs.api.UserService;
import org.openmrs.api.context.Context;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class PathologyRequestController {
    @RequestMapping("/module/imbpathologyrequest/imbpathologyrequest")
    public void manageHtmlForms(Model model) {
        UserService userService= Context.getUserService();
        List<User> users = userService.getAllUsers();

        model.addAttribute("users",users);
    }

    @RequestMapping(value = "/module/imbpathologyrequest/linkToPathologyReport", method = RequestMethod.GET)
    public ModelAndView goToPathologyReport() {
        String projectUrl=Context.getAdministrationService().getGlobalProperty("imbpathologyrequest.ulrForPathologyReport");
        return new ModelAndView("redirect:" + projectUrl);
    }

}
