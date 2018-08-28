package com.baosight.message.rest.security;

import com.baosight.message.rest.persist.mapper.MessageSourceMapper;
import com.baosight.message.rest.persist.po.MessageSource;
import com.baosight.message.util.SHA256Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;

@Component
public class SourceAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    MessageSourceMapper messageSourceMapper;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        MessageSource messageSource = messageSourceMapper.getSource(name);
        if (ObjectUtils.isEmpty(messageSource)) {
            throw new BadCredentialsException("invalid sourceCode");
        }else{
            String encoded = SHA256Util.encode(password);
            if(messageSource.getPassword().equals(encoded)){
                ArrayList<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("USER"));
                Authentication auth = new UsernamePasswordAuthenticationToken(name, password, authorities);
                return auth;
            }else{
                throw new BadCredentialsException("invalid password");
            }
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
