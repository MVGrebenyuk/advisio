package ru.advisio.core.aop;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.security.access.AccessDeniedException;
import ru.advisio.core.enums.EnType;
import ru.advisio.core.exceptions.AdvisioEntityNotFound;
import ru.advisio.core.repository.CompanyRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Aspect
@Component
@RequiredArgsConstructor
public class GroupManagementAspect {

    private final CompanyRepository companyRepository;

    @Around("@annotation(CompanyAdmin)")
    public Object checkCompanyAdminAccess(ProceedingJoinPoint joinPoint) throws Throwable {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        var companyName = extractCompanyName(joinPoint, "cname");

        if (authentication == null) {
            throw new AccessDeniedException("User not authenticated");
        }

        if(!companyRepository.existsCompanyByCname(companyName)){
            throw new AdvisioEntityNotFound(EnType.COMPANY, companyName);
        }

        List<String> userGroups = extractUserGroups(authentication);

        boolean isAdmin = userGroups.contains(String.format("/%s/%s", companyName, Roles.ADMIN.getName()));

        if (!isAdmin) {
            throw new AccessDeniedException("User is not a Company Admin");
        }

        return joinPoint.proceed();
    }

    @Around("@annotation(CompanyManager)")
    public Object checkCompanyManagerAccess(ProceedingJoinPoint joinPoint) throws Throwable {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        var companyName = extractCompanyName(joinPoint, "cname");

        if (authentication == null) {
            throw new AccessDeniedException("User not authenticated");
        }

        if(!companyRepository.existsCompanyByCname(companyName)){
            throw new AdvisioEntityNotFound(EnType.COMPANY, companyName);
        }

        List<String> userGroups = extractUserGroups(authentication);

        boolean isManager = userGroups.contains(String.format("/%s/%s", companyName, Roles.MANAGER.getName()));
        boolean isAdmin = userGroups.contains(String.format("/%s/%s", companyName, Roles.ADMIN.getName()));

        if (!isManager && !isAdmin) {
            throw new AccessDeniedException("User is not a Company Manager");
        }

        return joinPoint.proceed();
    }

    @Around("@annotation(CompanyObserver)")
    public Object checkCompanyObserverAccess(ProceedingJoinPoint joinPoint) throws Throwable {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        var companyName = extractCompanyName(joinPoint, "cname");

        if (authentication == null) {
            throw new AccessDeniedException("User not authenticated");
        }

        if(!companyRepository.existsCompanyByCname(companyName)){
            throw new AdvisioEntityNotFound(EnType.COMPANY, companyName);
        }

        List<String> userGroups = extractUserGroups(authentication);

        boolean isManager = userGroups.contains(String.format("/%s/%s", companyName, Roles.MANAGER.getName()));
        boolean isAdmin = userGroups.contains(String.format("/%s/%s", companyName, Roles.ADMIN.getName()));
        boolean isObserver = userGroups.contains(String.format("/%s/%s", companyName, Roles.OBSERVER.getName()));

        if (!isManager && !isAdmin && !isObserver) {
            throw new AccessDeniedException("User to not belongs for no one company");
        }

        return joinPoint.proceed();
    }

    private List<String> extractUserGroups(Authentication authentication) {
        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) authentication;
            return jwtAuth.getToken().getClaim("groups");
        } else if (authentication.getPrincipal() instanceof OidcUser) {
            OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
            return oidcUser.getAttribute("groups");
        } else {
            throw new AccessDeniedException("Unsupported authentication type");
        }
    }

    private String extractCompanyName(ProceedingJoinPoint joinPoint, String paramName) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        Map<String, Object> params = IntStream.range(0, parameterNames.length)
                .boxed()
                .collect(Collectors.toMap(
                        i -> parameterNames[i],
                        i -> args[i]
                ));

        if (!params.containsKey(paramName)) {
            throw new IllegalArgumentException("Method parameter '" + paramName + "' not found");
        }

        Object value = params.get(paramName);
        if (!(value instanceof String)) {
            throw new IllegalArgumentException("Parameter '" + paramName + "' must be of type String");
        }

        return (String) value;
    }

    @AllArgsConstructor
    @Getter
    enum Roles {

        ADMIN("admin"),
        MANAGER("manager"),
        OBSERVER("observer");

        private final String name;
    }
}
