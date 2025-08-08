package ru.advisio.core.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
@Slf4j
public class UserUtil {

    public static Set<String> getOidcUserCompanyes(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) authentication;
            List<String> dirtyGroups =  jwtAuth.getToken().getClaim("groups");
            return dirtyGroups.stream()
                    .filter(str -> str.contains("/"))
                    .map(str -> str.split("/")[1])
                    .collect(Collectors.toSet());
        }

        throw new AccessDeniedException("Unsupported authentication type");
    }

}
