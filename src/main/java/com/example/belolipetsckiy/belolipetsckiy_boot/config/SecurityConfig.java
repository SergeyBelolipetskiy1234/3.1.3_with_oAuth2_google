package com.example.belolipetsckiy.belolipetsckiy_boot.config;

import com.example.belolipetsckiy.belolipetsckiy_boot.config.handler.LoginSuccessHandler;
import com.example.belolipetsckiy.belolipetsckiy_boot.oauth2.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService; // сервис, с помощью которого тащим пользователя
    private final LoginSuccessHandler loginSuccessHandler; // класс, в котором описана логика перенаправления пользователей по ролям
    private final CustomOAuth2UserService oAuth2UserService;
 //   private final PasswordEncoder passwordEncoder;


    public SecurityConfig(@Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService, LoginSuccessHandler loginSuccessHandler, CustomOAuth2UserService oAuth2UserService) {
        this.userDetailsService = userDetailsService;
        this.loginSuccessHandler = loginSuccessHandler;
        this.oAuth2UserService = oAuth2UserService;
      //  this.passwordEncoder = passwordEncoder;
    }
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userDetailsService);
        return authenticationProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/login", "/logout", "/oauth/**").permitAll() // доступность всем
                .antMatchers("/user/**").access("hasAnyRole('ROLE_ADMIN')") // разрешаем входить на /user пользователям с ролью User, Admin
                .and()
               // .formLogin()
                .oauth2Login()

                // Spring сам подставит свою логин форму
               .successHandler(loginSuccessHandler)
                .and()// подключаем наш SuccessHandler для перенеправления по ролям
                .formLogin().successHandler(loginSuccessHandler)
                .and()
                .logout();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

}
