package com.english.api_gateway.service;

import org.springframework.stereotype.Service;
import com.english.api_gateway.dto.IntrospectRequest;
import com.english.api_gateway.dto.IntrospectResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IdentityService {
    IdentityClient identityClient;
    public Mono<IntrospectResponse> validateToken(String token){
        IntrospectRequest introspectRequest = new IntrospectRequest(token);
        return identityClient.introspect(introspectRequest);
    }
}
