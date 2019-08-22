# AionAccountMaker

Generates random private keys and provides the corresponding Aion address.

## Usage

If you haven't already, clone this repo: `git clone https://github.com/aion-kelvin/AionAccountMaker.git`

Then, from the directory where you cloned it, run: `./gradlew run`

Example output:

```
Private key: 0xb87d92ac1642bf1fbb1ba7848ca73987be659dbfcb456140b4deb7674c07f8995ba92260c35f277f8f5d10f710e9da35cdbb845648f069113766f90b3879190a
Address: 0xa01091874ecffe9f864c58d1ef795e1f8fa623a074b07c5a5e973f8521403e50
```

The private key is suitable for use for importing into Aiwa or Aion web3.js.

If you would like to call this programmatically, you can build this library using `./gradlew build`.  The jar file will be in `build/libs`.  The following Java code can be called to create private key and address:

```java
// generates random keypair
KeyPair kp = CryptoUtils.generateKeyPair();

// private key for the keypair (hex, without leading 0x)
String privateKey = CryptoUtils.privateKey(kp));

// Aion address corresponding to keypair (hex, without leading 0x)
String address = CryptoUtils.address(kp));
```
