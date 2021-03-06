package aion.util;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.KeyPairGenerator;
import net.i2p.crypto.eddsa.Utils;

import java.nio.ByteBuffer;
import java.security.KeyPair;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public class CryptoUtils {
    private static final String skEncodedPrefix = "302e020100300506032b657004220420";
    private static final String pkEncodedPrefix = "302a300506032b6570032100";

    /**
     * Generate pseudorandom key pair
     *
     * @return pseudorandom key pair
     */
    public static KeyPair generateKeyPair() {
        return new KeyPairGenerator().generateKeyPair();
    }

    /**
     * Create a 'private key' from KeyPair.
     *
     * Note: this isn't the private key used in the cryptogrpahic sense; it includes
     * the private key plus some extra information, but this is the 'private key'
     * that other Aion software asks for when they ask for private key (i.e. Aiwa and
     * Aion web3.js).
     *
     * @param pair key pair
     * @return private key
     */
    public static String privateKey(KeyPair pair) {
        EdDSAPrivateKey privateKey = (EdDSAPrivateKey) pair.getPrivate();
        EdDSAPublicKey publicKey = (EdDSAPublicKey)pair.getPublic();
        String privPart = Utils.bytesToHex(privateKey.getEncoded()).substring(32, 96);
        String pubPart = Utils.bytesToHex(publicKey.getEncoded()).substring(pkEncodedPrefix.length(), 88);
        return privPart + pubPart;
    }

    /**
     * Derive Aion address from KeyPair
     *
     * @param pair key pair
     * @return Aion address corresponding to the key pair
     * @throws InvalidKeySpecException if system missing crypto algorithm
     */
    public static String address(KeyPair pair) throws InvalidKeySpecException {
        return Utils.bytesToHex(deriveAddress(privKey(pair)));
    }

    private static byte[] privKey(KeyPair pair) {
        EdDSAPrivateKey privateKey = (EdDSAPrivateKey) pair.getPrivate();
        return Utils.hexToBytes(Utils.bytesToHex(privateKey.getEncoded()).substring(32, 96));
    }

    /**
     * Derive the corresponding aion address, given the private key bytes.
     */
    private static byte[] deriveAddress(byte[] privateKeyBytes) throws InvalidKeySpecException {
        if (privateKeyBytes == null) {
            throw new NullPointerException("private key cannot be null");
        }

        if (privateKeyBytes.length != 32){
            throw new IllegalArgumentException("private key mute be 32 bytes");
        }

        EdDSAPrivateKey privateKey = new EdDSAPrivateKey(new PKCS8EncodedKeySpec(addSkPrefix(Utils.bytesToHex(privateKeyBytes))));
        byte[] publicKeyBytes = privateKey.getAbyte();

        return computeA0Address(publicKeyBytes);
    }

    /**
     * Add encoding prefix for importing private key
     */
    private static byte[] addSkPrefix(String skString){
        String skEncoded = skEncodedPrefix + skString;
        return Utils.hexToBytes(skEncoded);
    }

    private static byte[] computeA0Address(byte[] publicKey) {
        byte A0_IDENTIFIER = (byte) 0xa0;
        ByteBuffer buf = ByteBuffer.allocate(32);
        buf.put(A0_IDENTIFIER);
        buf.put(blake256(publicKey), 1, 31);
        return buf.array();
    }

    private static byte[] blake256(byte[] input) {
        Blake2b digest = Blake2b.Digest.newInstance(32);
        digest.update(input);
        return digest.digest();
    }
}