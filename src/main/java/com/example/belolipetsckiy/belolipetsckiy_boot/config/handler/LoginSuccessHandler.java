package com.example.belolipetsckiy.belolipetsckiy_boot.config.handler;

import com.example.belolipetsckiy.belolipetsckiy_boot.models.Provider;
import com.example.belolipetsckiy.belolipetsckiy_boot.models.Role;
import com.example.belolipetsckiy.belolipetsckiy_boot.models.User;

import com.example.belolipetsckiy.belolipetsckiy_boot.oauth2.CustomOAuth2User;
import com.example.belolipetsckiy.belolipetsckiy_boot.service.RoleService;
import com.example.belolipetsckiy.belolipetsckiy_boot.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final Logger LOGGER = Logger.getLogger("<-------- LOG: LoginSuccessHandler -------->");
    private final UserService userService;
    private final RoleService roleService;

    public LoginSuccessHandler(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                      Authentication authentication) throws IOException, ServletException {
    try{
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        LOGGER.info("GOOGLE authority: " + oAuth2User.getAuthorities());
        if(!userService.existsByEmail(oAuth2User.getEmail())) {
            newUserGoogle(oAuth2User);
        } else {
            LOGGER.info("Welcome back " + oAuth2User.getName());
        }
    } catch(ClassCastException e) {
        e.printStackTrace();
    }

    Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (roles.contains("ROLE_ADMIN")) {
            httpServletResponse.sendRedirect("/user");
        } else if (roles.contains("ROLE_USER")) {
            httpServletResponse.sendRedirect("/client");
        } else
            httpServletResponse.sendRedirect("/login");
        LOGGER.info("For entering as registered user from Google use EMAIL and default PASSWORD \"123\"");
    }

    public void newUserGoogle(CustomOAuth2User oAuth2User){
        User user = new User();
        user.setUsername(oAuth2User.getAttribute("given_name"));
        user.setSurname(oAuth2User.getAttribute("family_name"));
        user.setEmail(oAuth2User.getEmail());
        user.setProvider(Provider.GOOGLE);
        user.setPassword("123");
        user.setAge(0);

        Set<String> role = AuthorityUtils.authorityListToSet(oAuth2User.getAuthorities());
        Set<Role> roles = new HashSet<>();
        if(role.contains("ROLE_ADMIN")){
            roles.add(roleService.getRoleByName("ROLE_ADMIN"));
        } else {
            roles.add(roleService.getRoleByName("ROLE_USER"));
        }
        user.setRoles(roles);
        userService.save(user);
        LOGGER.info("User " + user.getUsername() + " has been successfully added into DB!");
    }
}


