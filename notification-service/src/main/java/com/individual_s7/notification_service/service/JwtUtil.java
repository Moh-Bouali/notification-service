//package com.individual_s7.notification_service.service;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureException;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;
//import reactor.core.publisher.Mono;
//
//import java.net.URL;
//import java.security.KeyFactory;
//import java.security.PublicKey;
//import java.security.spec.X509EncodedKeySpec;
//import java.util.Base64;
//
//@Component
//public class JwtUtil {
//
//    private final RestTemplate restTemplate = new RestTemplate();
//    private final ObjectMapper objectMapper = new ObjectMapper();
//    private final String keycloakUrl = "http://localhost:8181/realms/spring-microservices-security-realm";  // Update with your Keycloak URL
//    private PublicKey cachedPublicKey = null;
//
//    private PublicKey getPublicKey() {
//        if (cachedPublicKey != null) {
//            return cachedPublicKey; // Use the cached key if available
//        }
//
//        try {
//            // Step 1: Fetch the OpenID configuration
//            String openidConfigUrl = keycloakUrl + "/.well-known/openid-configuration";
//            String openidConfigResponse = restTemplate.getForObject(openidConfigUrl, String.class);
//
//            // Parse the openid-configuration response to extract the jwks_uri
//            JsonNode openidConfigJson = objectMapper.readTree(openidConfigResponse);
//            String jwksUri = openidConfigJson.get("jwks_uri").asText();
//
//            // Step 2: Fetch the JWKS from the jwks_uri
//            String jwksResponse = restTemplate.getForObject(jwksUri, String.class);
//            JsonNode jwksJson = objectMapper.readTree(jwksResponse);
//            JsonNode keyJson = jwksJson.get("keys").get(0); // Assuming the first key is the correct one
//
//            // Step 3: Extract the public key from the JWKS response
//            String publicKeyPem = keyJson.get("x5c").get(0).asText(); // Get the x5c (certificate chain)
//
//            // Step 4: Convert the PEM-encoded key into a PublicKey object
//            byte[] decoded = Base64.getDecoder().decode(publicKeyPem);
//            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
//            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//            cachedPublicKey = keyFactory.generatePublic(keySpec);
//
//            return cachedPublicKey;
//        } catch (Exception e) {
//            throw new RuntimeException("Error fetching or parsing public key from Keycloak", e);
//        }
//    }
//
//    public Mono<Boolean> validateToken(String token) {
//        return Mono.create(sink -> {
//            try {
//                PublicKey publicKey = getPublicKey();
//                Jwts.parser()
//                        .setSigningKey(publicKey)
//                        .parseClaimsJws(token);  // Validate the token
//                sink.success(true);
//            } catch (SignatureException e) {
//                sink.success(false);  // Invalid token signature
//            } catch (Exception e) {
//                sink.error(e);  // Propagate other errors
//            }
//        });
//    }
//}
//
//
