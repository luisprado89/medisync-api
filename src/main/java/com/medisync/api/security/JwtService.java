package com.medisync.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
/**
 * Servicio encargado de generar, validar y procesar los tokens JWT
 * utilizados durante la autenticación de la aplicación.
 *
 * Gestiona la creación de nuevos tokens, la extracción de información
 * contenida en ellos y la validación de su autenticidad y vigencia.
 */
@Service
public class JwtService {
    /**
     * Clave secreta utilizada para firmar y verificar los tokens JWT.
     */
    @Value("${jwt.secret}")
    private String secretKey;
    /**
     * Tiempo de validez del token JWT expresado en milisegundos.
     */
    @Value("${jwt.expiration}")
    private long jwtExpiration;
    /**
     * Extrae el nombre de usuario almacenado en un token JWT.
     *
     * @param token token JWT.
     * @return nombre de usuario contenido en el token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    /**
     * Extrae un dato concreto de los claims contenidos en un token JWT.
     *
     * @param token token JWT.
     * @param claimsResolver función utilizada para obtener el dato deseado.
     * @param <T> tipo del valor que se desea extraer.
     * @return valor obtenido del conjunto de claims.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    /**
     * Genera un nuevo token JWT para un usuario autenticado.
     *
     * El token incluye el nombre de usuario, el rol del usuario,
     * la fecha de emisión y la fecha de expiración, firmándose
     * mediante el algoritmo HS256.
     *
     * @param username nombre de usuario autenticado.
     * @param rol rol asignado al usuario.
     * @return token JWT generado.
     */
    public String generateToken(String username, String rol) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", rol);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    /**
     * Comprueba si un token JWT es válido para un usuario determinado.
     *
     * Un token se considera válido cuando pertenece al usuario indicado
     * y no ha expirado.
     *
     * @param token token JWT.
     * @param username nombre de usuario esperado.
     * @return {@code true} si el token es válido; en caso contrario,
     * {@code false}.
     */
    public boolean isTokenValid(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username)) && !isTokenExpired(token);
    }
    /**
     * Comprueba si un token JWT ha expirado.
     *
     * @param token token JWT.
     * @return {@code true} si el token ha expirado; en caso contrario,
     * {@code false}.
     */
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
    /**
     * Obtiene todos los claims contenidos en un token JWT.
     *
     * @param token token JWT.
     * @return conjunto de claims almacenados en el token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    /**
     * Obtiene la clave criptográfica utilizada para firmar y verificar
     * los tokens JWT.
     *
     * La clave se genera a partir de la cadena codificada en Base64
     * definida en la configuración de la aplicación.
     *
     * @return clave utilizada para la firma de los tokens.
     */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}