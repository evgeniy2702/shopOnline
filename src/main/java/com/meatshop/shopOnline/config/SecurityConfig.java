package com.meatshop.shopOnline.config;

import com.meatshop.shopOnline.security.CustomAuthenticationFailureHandler;
import com.meatshop.shopOnline.security.CustomLogoutSuccessHandler;
import com.meatshop.shopOnline.security.RefererRedirectionAuthenticationSuccessHandler;
import com.meatshop.shopOnline.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.sql.DataSource;

/**
 @author Zhurenko Evgeniy 8.06.21
 */

@Configuration
@EnableWebSecurity
@Import({WebSecurityBeanConfig.class})
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private UserService userService;
    private AuthenticationEntryPoint authenticationEntryPoint;
    private AccessDeniedHandler customAccessDeniedHandler;
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private CustomLogoutSuccessHandler customLogoutSuccessHandler;
    private DataSource dataSource;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setAuthenticationEntryPoint(AuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Autowired
    public void setCustomAccessDeniedHandler(AccessDeniedHandler customAccessDeniedHandler) {
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }

    @Autowired
    public void setCustomAuthenticationFailureHandler(CustomAuthenticationFailureHandler customAuthenticationFailureHandler) {
        this.customAuthenticationFailureHandler = customAuthenticationFailureHandler;
    }

    @Autowired
    public void setCustomLogoutSuccessHandler(CustomLogoutSuccessHandler customLogoutSuccessHandler) {
        this.customLogoutSuccessHandler = customLogoutSuccessHandler;
    }

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.debug(true);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests().antMatchers("/registration").not().fullyAuthenticated();
        http
                .authorizeRequests().antMatchers("/user/**").hasRole("USER");
        http
                .authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN");
        http
                .authorizeRequests().antMatchers("/guest/**").anonymous();
        http
                .authorizeRequests()
                .antMatchers("/accessDenied", "/**", "/**/*.jpg", "/**/*.css", "/**/*.js","/static/**")
                .permitAll();
        http
                .authorizeRequests().anyRequest().authenticated();
        http
                .authorizeRequests().and().exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler);
        http
                .authorizeRequests()
                .and()
                .formLogin()
                .failureHandler(customAuthenticationFailureHandler)
                .loginPage("/login_page.html")
                .loginProcessingUrl("/authentication")
                .usernameParameter("username")
                .successHandler(new RefererRedirectionAuthenticationSuccessHandler())

                .and()
                .logout(logout -> logout
                                .logoutSuccessHandler(customLogoutSuccessHandler)
                                .logoutUrl("/leave/authentication")
                                .deleteCookies("JSESSIONID")
                );
        http
                .authorizeRequests().and()
                .rememberMe().tokenRepository(this.persistentTokenRepository())
                .tokenValiditySeconds(1 * 24 * 60 * 60); // 24h

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(passwordEncoder);

    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
        db.setDataSource(dataSource);
        return db;
    }
}
