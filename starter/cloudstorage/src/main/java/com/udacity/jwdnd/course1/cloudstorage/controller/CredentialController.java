package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.dto.CredForm;
import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("cred")
public class CredentialController {

    private UserService userService;
    private CredentialService credService;

    public CredentialController(UserService userService, CredentialService credService) {
        this.userService = userService;
        this.credService = credService;
    }

    @RequestMapping(value = "/creds/add", method = RequestMethod.POST)
    public String postNewNote(CredForm cred, Model model,
                              Authentication authentication){
        boolean credSaved = false;
        String errorMsg = "Unable to Save Creds";
        int success = 0;

        User user = this.userService.getSignedInUser(authentication);
        if(cred.getCredentialId() == null){
            success = this.credService.saveCredential(cred, user.getUserId());
        }else{
            success = this.credService.updateCredential(cred, user.getUserId());
        }
        if(success > 0) credSaved = true;

        model.addAttribute("success", credSaved);
        model.addAttribute("errResponse", errorMsg);
        model.addAttribute("name", "/home#nav-credentials");
        return "result";
    }

    @RequestMapping(value="/creds/delete", method= RequestMethod.GET)
    public String deleteNote(@Param(value="id") Integer id,
                             Model model) {
        this.credService.removeCredential(id);
        model.addAttribute("success", true);
        model.addAttribute("name", "/home#nav-credentials");
        return "result";
    }
}