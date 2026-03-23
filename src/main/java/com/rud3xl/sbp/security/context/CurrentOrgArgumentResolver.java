package com.rud3xl.sbp.security.context;

import com.rud3xl.sbp.domain.Organization;
import com.rud3xl.sbp.exception.ResourceNotFoundException;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class CurrentOrgArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentOrg.class) && parameter.getParameterType().isAssignableFrom(Organization.class);
    }

    @Override
    public @Nullable Object resolveArgument(@NonNull MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer, @NonNull NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {
        Organization organization = OrganizationContext.getOrganization();
        if (organization == null) {
            throw new ResourceNotFoundException("Organization context not found. Please provide a valid 'X-Org-Slug' header");
        }
        return organization;
    }
}
