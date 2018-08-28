package com.baosight.message.rest.security;

import com.baosight.message.core.AbstractResponse;
import lombok.Data;

@Data
public class JWTResponse extends AbstractResponse {

    String token;

}
