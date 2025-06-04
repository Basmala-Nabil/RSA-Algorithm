🔐 RSA Algorithm - Overview
RSA (Rivest–Shamir–Adleman) is one of the earliest and most widely used public-key cryptosystems. It is mainly used for secure data transmission and is based on the mathematical difficulty of factoring large prime numbers.

📌 How RSA Works

RSA is an asymmetric encryption algorithm, meaning it uses two different keys:

A public key to encrypt data

A private key to decrypt data

🔑 Key Generation Steps

Choose two large distinct prime numbers, p and q.

Compute n = p × q.

→ n is used as the modulus for both the public and private keys.

Compute Euler's totient function: φ(n) = (p - 1) × (q - 1)

Choose an integer e such that 1 < e < φ(n) and gcd(e, φ(n)) = 1

Compute d such that:

d ≡ e⁻¹ mod φ(n) (modular inverse of e)

Public key = (e, n)

Private key = (d, n)

🔐 Encryption

To encrypt a message M (as an integer):


C = M^e mod n

🔓 Decryption

To decrypt the ciphertext C:

M = C^d mod n

✅ Example

Let’s say:

p = 61, q = 53

n = p × q = 3233

φ(n) = 3120

Choose e = 17

Calculate d = 2753

Then:

Message M = 123

Encrypted C = 123^17 mod 3233 = 855

Decrypted M = 855^2753 mod 3233 = 123

🛡️ Applications

SSL/TLS encryption

Secure emails (PGP, GPG)

Digital signatures

Cryptocurrency wallet security

Key exchange protocols

📚 References

RSA Cryptography Standard (NIST)

William Stallings, Cryptography and Network Security, 7th Edition
