package com.restaurant.enums;

import lombok.Getter;

@Getter
public enum UserRole {
    CUSTOMER(0, "顾客", new String[]{"/api/dish/list", "/api/category/list", "/api/orders", "/api/orders/**", "/api/member/**", "/api/coupon/**", "/api/dish/member-price/**"}),
    ADMIN(1, "管理员", new String[]{"/api/**"}),
    CASHIER(2, "收银员", new String[]{"/api/dish/**", "/api/category/**", "/api/orders/**", "/api/restaurant/info"}),
    KITCHEN(3, "后厨", new String[]{"/api/orders/**"});

    private final Integer code;
    private final String name;
    private final String[] permissions;

    UserRole(Integer code, String name, String[] permissions) {
        this.code = code;
        this.name = name;
        this.permissions = permissions;
    }

    public static UserRole getByCode(Integer code) {
        for (UserRole role : values()) {
            if (role.getCode().equals(code)) {
                return role;
            }
        }
        return CUSTOMER;
    }
}
