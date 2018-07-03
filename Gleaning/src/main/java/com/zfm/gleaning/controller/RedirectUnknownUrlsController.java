package com.zfm.gleaning.controller;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller  
public class RedirectUnknownUrlsController implements ErrorController{  
    @GetMapping("/error")  
    public String redirectNonExistentUrlsToErrorHtml(Model model)  {  
//        model.addAttribute("","");  
        return "error/404";  
    }  
    @Override  
    public String getErrorPath() {  
        return "/error";  
    }  
}  