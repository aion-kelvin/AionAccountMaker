package aion.util;

import java.security.KeyPair;

public class AccountMaker {
    public static void main(String[] args) throws Exception {
        KeyPair kp = CryptoUtils.generateKeyPair();
        System.out.println("Private key: 0x" + CryptoUtils.privateKey(kp));
        System.out.println("Address: 0x" + CryptoUtils.address(kp));
    }
}
