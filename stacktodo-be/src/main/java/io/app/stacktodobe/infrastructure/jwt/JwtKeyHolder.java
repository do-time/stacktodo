package io.app.stacktodobe.infrastructure.jwt;


import javax.crypto.SecretKey;

public record JwtKeyHolder(SecretKey secretKey) {

}
