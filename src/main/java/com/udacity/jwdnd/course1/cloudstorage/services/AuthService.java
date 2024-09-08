package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.entites.User;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthService implements AuthenticationProvider {

    final UserMapper userMapper;
    final HashService hashService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getName();
        String password = authentication.getCredentials().toString();

        User userInfo = userMapper.getUserByName(userName);
        if (userInfo != null) {
            String passEncode = hashService.getHashedValue(password, userInfo.getSalt());
            if (userInfo.getPassword().equals(passEncode)) {
                return new UsernamePasswordAuthenticationToken(userName,password, new ArrayList<>());
            }
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
