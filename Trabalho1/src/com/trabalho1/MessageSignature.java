package com.trabalho1;

import java.security.*;

public class MessageSignature {
    public static byte[] sign(String message, PrivateKey privateKey) {
        Signature rsa;
        try {
            rsa = Signature.getInstance("SHA1withRSA");
            rsa.initSign(privateKey);
            rsa.update(message.getBytes());
            return rsa.sign();
        } catch (NoSuchAlgorithmException e) {
            System.out.print("[ MessageSignature ] Nao foi possivel carregar o algoritmo de assinatura digital ");
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            System.out.print("[ MessageSignature ] Nao foi possivel inicializar o algoritmo de assinatura digital ");
            e.printStackTrace();
        } catch (SignatureException e) {
            System.out.print("[ MessageSignature ] Nao foi possivel assinar a mensagem ");
            e.printStackTrace();
        }
        return null;
    }
    public static boolean verify(String message, byte[] signature, PublicKey publicKey) {
        Signature sig;
        try {
            sig = Signature.getInstance("SHA1withRSA");
            sig.initVerify(publicKey);
            sig.update(message.getBytes());
            return sig.verify(signature);
        } catch (NoSuchAlgorithmException e) {
            System.out.print("[ MessageSignature ] Nao foi possivel carregar o algoritmo de assinatura digital ");
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            System.out.print("[ MessageSignature ] Nao foi possivel inicializar o algoritmo de assinatura digital ");
            e.printStackTrace();
        } catch (SignatureException e) {
            System.out.print("[ MessageSignature ] Nao foi possivel verificar a mensagem ");
            e.printStackTrace();
        }
        return false;
    }
}
