package com.greenorine.innovationsurfuapi.security.jwt;

import com.greenorine.innovationsurfuapi.model.Status;
import com.greenorine.innovationsurfuapi.model.User;

public final class JwtUserFactory {
    public JwtUserFactory() {
    }

    public static JwtUser create (User user) {
        return new JwtUser(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getStatus().equals(Status.ACTIVE)
        );
    }
}
