import java.util.Scanner;

public class rsa {
    private long p, q;
    private long n;
    private long phi;
    private long e; // public exponent
    private long d; // private exponent

    private static long seed = System.currentTimeMillis();

    public rsa(long p, long q) {
        if (!isPrimeMillerRabin(p, 5) || !isPrimeMillerRabin(q, 5) || p == q) {
            throw new IllegalArgumentException("p and q must be distinct prime numbers");
        }
        this.p = p;
        this.q = q;
        this.n = p * q;
        this.phi = (p - 1) * (q - 1);
        this.e = generateE(phi);
        this.d = modInverse(e, phi);
    }

    private long generateE(long phi) {
        long e = 2;
        while (e < phi && gcd(e, phi) != 1) {
            e++;
        }
        return e;
    }

    public long getPublicKeyE() {
        return e;
    }

    public long getPublicKeyN() {
        return n;
    }

    public long getPrivateKeyD() {
        return d;
    }



    // Square-and-Multiply
    public static long powerMod(long base, long exp, long mod) {
        long result = 1 % mod;
        base %= mod;
        while (exp > 0) {
            if ((exp & 1) == 1)
                result = (result * base) % mod;
            base = (base * base) % mod;
            exp >>= 1;
        }
        return result;
    }

    public static long gcd(long a, long b) {
        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    public static long modInverse(long a, long m) {
        long m0 = m, t, q;
        long x0 = 0, x1 = 1;

        if (m == 1) return 0;

        while (a > 1) {
            q = a / m;
            t = m;

            m = a % m;
            a = t;

            t = x0;
            x0 = x1 - q * x0;
            x1 = t;
        }

        if (x1 < 0)
            x1 += m0;

        return x1;
    }

    public static long simpleRand(long min, long max) {
        seed = (seed * 1103515245 + 12345) & 0x7FFFFFFF;
        return min + (seed % (max - min + 1));
    }

    public static boolean millerTest(long d, long n, long a) {
        long x = powerMod(a, d, n);
        if (x == 1 || x == n - 1)
            return true;

        while (d != n - 1)
        {
            x = (x * x) % n;
            d <<= 1;

            if (x == 1) return false;
            if (x == n - 1) return true;
        }
        return false;
    }

    public static boolean isPrimeMillerRabin(long n, int s)
    {
        if (n <= 1 || n == 4) return false;
        if (n <= 3) return true;

        long d = n - 1;
        while ((d & 1) == 0)
            d >>= 1;

        for (int i = 0; i < s; i++) {
            long a = simpleRand(2, n - 2);
            if (!millerTest(d, n, a))
                return false;
        }
        return true;
    }

    public static long CRTExponentiation(long x, long d, long p, long q) {
        long n = p * q;

        long x_p = x % p;
        long x_q = x % q;

        long d_p = d % (p - 1);
        long d_q = d % (q - 1);

        long y_p = powerMod(x_p, d_p, p);
        long y_q = powerMod(x_q, d_q, q);

        long c_p = modInverse(q, p);
        long c_q = modInverse(p, q);

        long result = ((q * c_p % n) * y_p % n + (p * c_q % n) * y_q % n) % n;

        return result;
    }
    // Encrypt a single character
    public long encrypt(long m) {
        return powerMod(m, e, n);
    }

    // Decrypt a single encrypted value using CRT optimization
    public long decrypt(long c) {
        return CRTExponentiation(c, d, p, q);
    }

}


class main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        long p, q;
        do {
            System.out.print("Enter a prime number p: ");
            p = sc.nextLong();
            if (!rsa.isPrimeMillerRabin(p, 5))
                System.out.println("p is not prime. Please enter a prime number.");
        } while (!rsa.isPrimeMillerRabin(p, 5));

        do {
            System.out.print("Enter a prime number q (different from p): ");
            q = sc.nextLong();
            if (q == p)
                System.out.println("q must be different from p.");
            else if (!rsa.isPrimeMillerRabin(q, 5))
                System.out.println("q is not prime. Please enter a prime number.");
        } while (q == p || !rsa.isPrimeMillerRabin(q, 5));

        rsa rsa = new rsa(p, q);

        System.out.println("\nPublic Key (e, n): (" + rsa.getPublicKeyE() + ", " + rsa.getPublicKeyN() + ")");
        System.out.println("Private Key (d, n): (" + rsa.getPrivateKeyD() + ", " + rsa.getPublicKeyN() + ")");

        sc.nextLine(); // consume newline

        System.out.print("\nEnter a message to encrypt: ");
        String message = sc.nextLine();

        long[] encrypted = new long[message.length()];
        System.out.println("\nEncrypting each character using c = m^e mod n:");
        for (int i = 0; i < message.length(); i++) {
            int mChar = (int) message.charAt(i);
            long enc = rsa.encrypt(mChar);
            encrypted[i] = enc;
            System.out.println("Char '" + message.charAt(i) + "' -> ASCII " + mChar + " -> Encrypted: " + enc);
        }

        StringBuilder decrypted = new StringBuilder();
        System.out.println("\nDecrypting each number using CRT optimization:");
        for (long val : encrypted) {
            long dec = rsa.decrypt(val);
            decrypted.append((char) dec);
            System.out.println("Encrypted: " + val + " -> Decrypted ASCII: " + dec + " -> Char: '" + (char) dec + "'");
        }

        System.out.println("\nFinal decrypted message: " + decrypted.toString());

        sc.close();
    }
}







