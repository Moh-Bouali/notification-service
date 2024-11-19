package com.individual_s7.notification_service.interceptor;

import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.net.URI;
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

        String query = uri.getQuery();
        if (query != null && query.contains("access_token")) {
            String[] params = query.split("&");
            for (String param : params) {
                if (param.startsWith("access_token=")) {
                    String jwtToken = param.split("=")[1];
                    try {
                        jwtDecoder.decode(jwtToken);  // Validate JWT token
                        return true;  // Allow the handshake
                    } catch (JwtException e) {
                        response.setStatusCode(HttpStatus.UNAUTHORIZED);
                        return false;  // Deny handshake if JWT is invalid
                    }
                }
            }
        }
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return false;  // Deny handshake if no valid token is found
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
    }
}