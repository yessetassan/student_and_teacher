package com.example.student_and_teacher.security;


import com.example.student_and_teacher.impl.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PersonDetailsService personDetailsService;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(personDetailsService);
    }

    private final UserAuthenticationSuccess userAuthenticationSuccess;

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService, UserAuthenticationSuccess userAuthenticationSuccess) {
        this.personDetailsService = personDetailsService;
        this.userAuthenticationSuccess = userAuthenticationSuccess;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests()
                .antMatchers("/register_student","/register_teacher").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .successHandler(userAuthenticationSuccess)
                .permitAll()
                .and()
                .logout().logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .permitAll();
    }

}
