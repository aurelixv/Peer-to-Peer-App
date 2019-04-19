package com.trabalho1;

import java.security.*;

/**
 *
 */
public class KeyPair {

    private java.security.KeyPair pair;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    /*
        Cria um par de chaves com o algoritmo RSA de 1024 bits, para depois extrair as
        respectivas chaves privada e publica.
     */
    public KeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            // Gera um aleatorio por meio de um algoritmo seguro
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            keyGen.initialize(1024, random);
            pair = keyGen.generateKeyPair();
            privateKey = pair.getPrivate();
            publicKey = pair.getPublic();
        } catch (Exception e) {
            System.err.println("Nao foi possivel criar par de chaves " + e.toString());
        }
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }
    public PrivateKey getPrivateKey() { return this.privateKey; }
    public byte[] sign(String broadcastMessage) {
        return MessageSignature.sign(broadcastMessage.getBroadcastMessage(), this.privateKey);
    }
}
