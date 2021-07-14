package com.meatshop.shopOnline.controllers;

import com.meatshop.shopOnline.models.Product;
import com.meatshop.shopOnline.models.TypeProduct;
import com.meatshop.shopOnline.models.User;
import com.meatshop.shopOnline.services.ProductService;
import com.meatshop.shopOnline.services.TypeProductService;
import com.meatshop.shopOnline.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import java.util.ArrayList;
import java.util.List;

/**
 @author Zhurenko Evgeniy 8.06.21
 */

@Controller
@RequestMapping("/")
public class HeaderController {

    private ProductService productService;
    private TypeProductService typeProductService;
    private UserService userService;

    @Value("${spring.mail.username}")
    private String email;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setTypeProductService(TypeProductService typeProductService) {
        this.typeProductService = typeProductService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("sign_page")
    public String openLoginHtml(Model model,
                                @RequestParam(name = "style", required = false) boolean style){
        if(style){
            model.addAttribute("style", "display:block;");
            model.addAttribute("unStyle", "display:none;");
            model.addAttribute("guest", true);
            return "redirect:/login_page";
        } else {
            model.addAttribute("style", "display:none;");
            model.addAttribute("unStyle", "display:block;");
            model.addAttribute("guest", true);
            return "redirect:/login_page";
        }
    }

    @GetMapping(value = {"guest/mail_page","user/mail_page/{id}", "admin/mail_page/{id}"})
    public ModelAndView openMailHtml(ModelAndView modelAndView,
                                     @PathVariable(name = "id", required = false) Long id){
        modelAndView.addObject("mail", true);
        modelAndView.addObject("email", email);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth.getPrincipal().toString().equalsIgnoreCase("anonymousUser")) {
            modelAndView.addObject("guest", true);
        } else if(auth.getAuthorities().stream().anyMatch(role -> role.toString()
                .equalsIgnoreCase("ROLE_USER"))){
            User userDB = userService.getUserById(id);
            modelAndView.addObject("user", true);
            modelAndView.addObject("user_person", userDB );
        } else if(auth.getAuthorities().stream().anyMatch(role -> role.toString()
                .equalsIgnoreCase("ROLE_ADMIN"))){
            User admin = userService.getUserById(id);
            modelAndView.addObject("admin", true);
            modelAndView.addObject("admin_person", admin );
        }
        modelAndView.setViewName("mail");
        return modelAndView;
    }

    @GetMapping(value = {"guest/page_discount_prop","user/page_discount_prop/{id}", "admin/page_discount_prop/{id}"})
    public String getDiscountProducts(@PathVariable(name = "id", required = false) Long id,
                                      Model model){
        List<Product> prodDiscountList = productService.findDiscountProducts();
        List<TypeProduct> typeProductList = typeProductService.getListOfTypeProduct();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!prodDiscountList.isEmpty()) {
            model.addAttribute("discount", true);
            model.addAttribute("typeProductList", typeProductList);
            model.addAttribute("prodDiscountList", prodDiscountList);

        } else {
            model.addAttribute("discount", false);
            model.addAttribute("prodDiscountMessage", "Пока что скидок нет !\n Зайдите завтра .");
        }
        defineGuestUserAdmin(model, auth);
        if(id != null){
            addUserOrAdminForPage(model, id);
        }
        return "page_discount_prop";
    }

    @PostMapping(value = {"guest/search", "user/search/{id}", "admin/search/{id}"})
    public String search(@RequestParam(name = "search", required = false) String search,
                         @PathVariable(name = "id", required = false) Long id,
                         Model model){
        String[] strings = search.split("[ \\,\\.\\;\\:\\-?!\\\"]+");
        List<Product> productList = new ArrayList<>();
        for (String string : strings){
            productList = productService.findProdBySearch(string);
        }
        if(productList.isEmpty()){
            model.addAttribute("message", "Ничего похожего не найдено");
        }
        model.addAttribute("title", "Результат поиска" );
        model.addAttribute("search", true);
        model.addAttribute("product_list", productList);
        model.addAttribute("type_list", productService.getTypeProdByListOfProd(productList));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        defineGuestUserAdmin(model, auth);
        if(id != null)
            addUserOrAdminForPage(model, id);
        return "page_products";
    }


    public void defineGuestUserAdmin(Model model, Authentication auth){
        if(auth.getPrincipal().toString().equalsIgnoreCase("anonymousUser")) {
            model.addAttribute("guest", true);
        }
        else if(auth.getAuthorities().stream().anyMatch(role -> role.toString()
                .equalsIgnoreCase("ROLE_USER"))) {
            model.addAttribute("user", true);
        } else if(auth.getAuthorities().stream().anyMatch(role -> role.toString()
                .equalsIgnoreCase("ROLE_ADMIN"))){
            model.addAttribute("admin", true);
        }
    }

    private void addUserOrAdminForPage(Model model, Long id){
        model.addAttribute("user_person", userService.getUserById(id));
    }
}
