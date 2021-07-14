package com.meatshop.shopOnline.config;

import com.meatshop.shopOnline.security.CustomAccessDeniedHandler;
import com.meatshop.shopOnline.security.CustomAuthenticationFailureHandler;
import com.meatshop.shopOnline.security.CustomLogoutSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.access.DelegatingAccessDeniedHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.security.web.csrf.MissingCsrfTokenException;

import javax.sql.DataSource;
import java.util.LinkedHashMap;

/**
 @author Zhurenko Evgeniy 8.06.21
 */

@Configuration
public class WebSecurityBeanConfig {

    @Bean
public PasswordEncoder bCryptPasswordEncoder(){
    return new BCryptPasswordEncoder();
}

@Bean
    public LogoutSuccessHandler logoutSuccessHandler(){
    return new CustomLogoutSuccessHandler();
}

@Bean
    public AuthenticationEntryPoint authenticationEntryPoint(){
    LoginUrlAuthenticationEntryPoint authenticationEntryPoint = new LoginUrlAuthenticationEntryPoint("/sign_page?style=true");
    authenticationEntryPoint.setUseForward(true);
    return authenticationEntryPoint;
}

    @Bean
    CustomAuthenticationFailureHandler authenticationFailureHandler(){
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    CustomAccessDeniedHandler customAccessDeniedHandler (){
        return new CustomAccessDeniedHandler();
    }
}
