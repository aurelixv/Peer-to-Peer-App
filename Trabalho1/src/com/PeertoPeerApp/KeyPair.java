package com.PeertoPeerApp;

import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

/**
 *
 */
class KeyPair {

    private PrivateKey privateKey;
    private PublicKey publicKey;

    /*
        Cria um par de chaves com o algoritmo RSA de 1024 bits, para depois extrair as
        respectivas chaves privada e publica.
     */
    KeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            // Gera um aleatorio por meio de um algoritmo seguro
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            keyGen.initialize(1024, random);
            java.security.KeyPair pair = keyGen.generateKeyPair();
            privateKey = pair.getPrivate();
            publicKey = pair.getPublic();
        } catch (Exception e) {
            System.err.println("[ KeyPair ] Nao foi possivel criar par de chaves " + e.toString());
        }
    }

    PublicKey getPublicKey() {
        return this.publicKey;
    }
    byte[] sign(String message) {
        return MessageSignature.sign(message, this.privateKey);
    }
}
