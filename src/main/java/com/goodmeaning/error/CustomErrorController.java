package com.goodmeaning.error;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;


import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class CustomErrorController implements ErrorController{
	
    @GetMapping("/error")
    public String handleError(HttpServletRequest request, Model model,Exception ex) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        
        if(status != null){
            int statusCode = Integer.valueOf(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {

        		return "errors/404error";
        		 
            } else {
    
        		model.addAttribute("msg", "처리중 에러 발생!!!");
                
            }
        }

        return "errors/error";
    }
}
