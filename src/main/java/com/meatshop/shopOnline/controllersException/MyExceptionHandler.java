package com.meatshop.shopOnline.controllersException;


import com.meatshop.shopOnline.models.ApiError;
import com.meatshop.shopOnline.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import javax.persistence.EntityNotFoundException;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Iterator;
import java.util.Objects;


/**
 @author Zhurenko Evgeniy 8.06.21
 */

//@SuppressWarnings({"unchecked", "deprecated","rawtypes"})
@ControllerAdvice
public class MyExceptionHandler extends ResponseEntityExceptionHandler {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @ExceptionHandler(Exception.class)
    public final ModelAndView handleAllExceptions(Exception ex, WebRequest request) {

        System.out.println(request.getDescription(true));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ApiError error = new ApiError();
        error.setMessage("Ошибка : " + HttpStatus.INTERNAL_SERVER_ERROR.value() + " " + HttpStatus.INTERNAL_SERVER_ERROR.name());
        error.setDebugMessage(ex.getMessage());

        return getModelAndView(error.getMessage() + ".\n " + error.getDebugMessage(), auth);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public final ModelAndView handleUserNotFoundException(UsernameNotFoundException ex, WebRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ApiError error = new ApiError();

        error.setMessage("Ошибка Username : " + HttpStatus.NOT_FOUND.value() + " " + HttpStatus.NOT_FOUND.name());
        error.setDebugMessage(ex.getMessage());

        return getModelAndView(error.getMessage() + ".\n " + error.getDebugMessage(), auth);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public final ModelAndView handleUserNotFoundException(EntityNotFoundException ex, HttpServletRequest request, WebRequest webRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ApiError error = new ApiError();
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute("javax.servlet.error.message");
        error.setMessage("Ошибка : " +  ex.getMessage());
        error.setDebugMessage(Integer.valueOf(status.toString()) + " " + message.toString());
        return getModelAndView(error.getMessage() + " " + error.getDebugMessage(), auth);
    }

    @ExceptionHandler(UserPrincipalNotFoundException.class)
    public final ModelAndView handleUserNotFoundException(UserPrincipalNotFoundException ex, WebRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ApiError error = new ApiError();

        error.setMessage("Ошибка Principal: " + HttpStatus.NOT_FOUND.value() + " " + HttpStatus.NOT_FOUND.name());
        error.setDebugMessage(ex.getMessage());

        return getModelAndView(error.getMessage() + ".\n " + error.getDebugMessage(), auth);
    }

    private ModelAndView getModelAndView(String message, Authentication authentication) {
        String username = authentication.getName();
        ModelAndView mav = new ModelAndView();
        if(authentication.getPrincipal().toString().equalsIgnoreCase("anonymousUser"))
            mav.addObject("guest",true);
        else if (username != "admin"){
            mav.addObject("user",true);
            mav.addObject("user_person", userService.getUserByUsername(authentication.getName()));
        } else {
            mav.addObject("admin",true);
            mav.addObject("admin_person", userService.getUserById(1L));
        }
        mav.addObject("account", message);
        mav.setViewName("error");
        return mav;
    }
}
