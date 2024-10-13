package com.individual_s7.notification_service.interceptor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtDecoder jwtDecoder;

    public JwtHandshakeInterceptor(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        URI uri = request.getURI();
        System.out.println("Attempting to handle handshake for URI: " + uri);

        String query = uri.getQuery();
        if (query != null && query.contains("access_token")) {
            System.out.println("Query contains access_token");
            String[] params = query.split("&");
            for (String param : params) {
                if (param.startsWith("access_token=")) {
                    String jwtToken = param.split("=")[1];
                    System.out.println("Extracted JWT token: " + jwtToken);
                    try {
                        jwtDecoder.decode(jwtToken);  // Validate JWT token
                        System.out.println("JWT is valid. Handshake allowed.");
                        return true;  // Allow the handshake
                    } catch (JwtException e) {
                        System.out.println("Invalid JWT: " + e.getMessage());
                        response.setStatusCode(HttpStatus.UNAUTHORIZED);
                        return false;  // Deny handshake if JWT is invalid
                    }
                }
            }
        }
        System.out.println("No valid token found in query. Handshake denied.");
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return false;  // Deny handshake if no valid token is found
    }


    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        // No specific logic needed after the handshake
    }
}