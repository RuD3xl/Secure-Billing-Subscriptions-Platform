package com.rud3xl.sbp.security.context;


import com.rud3xl.sbp.domain.Organization;

public class OrganizationContext {
    private static final ThreadLocal<Organization> ORGANIZATION = new ThreadLocal<>();
    public static Organization getOrganization() {
        return ORGANIZATION.get();
    }
    public static void setOrganization(Organization organization) {
        ORGANIZATION.set(organization);
    }
    public static void clearOrganization() {
        ORGANIZATION.remove();
    }
}
