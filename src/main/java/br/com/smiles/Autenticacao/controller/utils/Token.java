package br.com.smiles.Autenticacao.controller.utils;

import java.security.SecureRandom;
import java.util.Base64;

public class Token {

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder baseEncryptadora = Base64.getUrlEncoder();

    public static String generateNewToken() {
        byte[] bytes = new byte[24];
        secureRandom.nextBytes(bytes);
        return baseEncryptadora.encodeToString(bytes);
    }
}
