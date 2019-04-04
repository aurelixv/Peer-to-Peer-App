package com.trabalho1;

import java.security.*;

/**
 *
 */
public class SignMessage {

    private KeyPair pair;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    /*
        Cria um par de chaves com o algoritmo RSA de 1024 bits, para depois extrair as
        respectivas chaves privada e publica.
     */
    public SignMessage() {
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

    public void sign() {
        Signature rsa;
        try {
            rsa = Signature.getInstance("SHA1withRSA");
            rsa.initSign(privateKey);
        } catch (NoSuchAlgorithmException e) {
            System.out.print("Nao foi possivel carregar o algoritmo de assinatura digital ");
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            System.out.print("Nao foi possivel inicializar o algoritmo de assinatura digital ");
            e.printStackTrace();
        }
    }

    public boolean verifySignature() {

        return false;
    }
}
