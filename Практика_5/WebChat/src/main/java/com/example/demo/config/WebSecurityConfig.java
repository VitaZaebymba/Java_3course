package com.example.demo.config;

import com.example.demo.Service.UserServ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserServ userServ;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/reg").permitAll()
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .permitAll()
                .and()
                    .logout().deleteCookies("JSESSIONID").logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .permitAll()
                .and()
                    .rememberMe().key("uniqueAndSecret").tokenValiditySeconds(86400)
                .and()
                    .sessionManagement()
                            .maximumSessions(-1)
                            .sessionRegistry(sessionRegistry());
        http.csrf().disable();
    }
    @Override
    public void configure(WebSecurity web) { //подзагрузка css, картинки
        web.ignoring().antMatchers("/style/**","/error","/js/**","/img/**");
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userServ)
                .passwordEncoder(NoOpPasswordEncoder.getInstance());
    }
    @Bean
    SessionRegistry sessionRegistry(){
        return new SessionRegistryImpl();
    }
}
