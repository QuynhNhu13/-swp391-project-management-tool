package com.qnhu.swp391projectmanagementtool.services.implement;

import com.qnhu.swp391projectmanagementtool.entities.User;
import com.qnhu.swp391projectmanagementtool.repositories.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOauth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oauth2User = super.loadUser(userRequest);

        Map<String, Object> attributes = oauth2User.getAttributes();
        String email = (String) attributes.get("email");
        String name = (String) attributes.getOrDefault("name", email);

        // create or update local User
        User user = userRepository.findByEmail(email).orElseGet(User::new);
        user.setEmail(email);
        user.setUsername(name);

        // determine role according to your rules
        String role = determineRoleByEmail(email);
        user.setRole(role);

        userRepository.save(user);
        return oauth2User;
    }

    private String determineRoleByEmail(String email) {
        if (email == null) return "ROLE_MEMBER";

        // admin fixed
        if ("wynnhu1311@gmail.com".equalsIgnoreCase(email)) {
            return "ROLE_ADMIN";
        }

        // lecturer by domain
        if (email.toLowerCase().endsWith("@fe.edu.vn")) {
            return "ROLE_LECTURER";
        }

        // fallback lecturer fixed email (mình giữ theo bạn)
        if ("nguyennuquynhnhu13@gmail.com".equalsIgnoreCase(email) ||
                "nguyennuquynhnhu13@gmail.".equalsIgnoreCase(email)) {
            return "ROLE_LECTURER";
        }

        // student: simple mapping: @fpt.edu.vn -> team leader, @gmail.com -> member
        if (email.toLowerCase().endsWith("@fpt.edu.vn")) {
            return "ROLE_TEAM_LEADER";
        }
        if (email.toLowerCase().endsWith("@gmail.com")) {
            return "ROLE_MEMBER";
        }

        return "ROLE_MEMBER";
    }
}