package com.baosight.message.rest.security;

import lombok.Data;

@Data
public class AccountCredentials {

    String sourceCode;

    String password;
}
