package helpers;

import java.math.BigInteger;

public class RSAManager {

    // Keys
    private int p = 77, q = 41, e = 29, r, d, n;

    // Returns True if number is prime
    private boolean isPrime(int number) {
        if (number <= 1)
            return false;
        // Finding for other possible factors
        for (int i = 2; i < Math.sqrt(number); i++) {
            if (number % i == 0)
                return false;
        }
        return true;
    }

    // DEBUG FUNCTION
    // Returns true if all inputs are valid
    private boolean areInputsValid() {
        try {
            // Verify if P and Q are valid
            if (!(isPrime(this.p) && isPrime(this.q) && isPrime(this.e)))
                throw new Exception("P, Q and E are not prime");
            else if (this.p * this.q < this.e)
                throw new Exception("E should not be larger than (P*Q)");
            else if (areCoPrime(this.e, this.n))
                throw new Exception("E is not coprime with other number");
        } catch (Exception e) {
            System.out.println(e);
        }

        return true;
    }

    // Returns GCD of number
    private int gcd(int x, int y) {
        if (y == 0) {
            return x;
        }
        return gcd(y, x % y);
    }

    // Returns true if coprime
    private boolean areCoPrime(int x, int y) {
        return gcd(x, y) == 1;
    }

    // Calcualtes private key
    private int getPrivateKey() {
        this.r = (this.p - 1) * (this.q - 1);
        this.d = 0;
        while (true) {
            if ((this.e * this.d) % r == 1)
                break;
            ++this.d;
        }

        return this.d;
    }

    // Returns message encrypted in RSA
    public String encryptMessage(String message) {
        String encrpytedMessage = "";
        // Removing any whitespace from message
        message = message.replaceAll("\\s+", "");

        // Getting value of n
        this.n = this.p * this.q;
        // Iterating through each character in message
        for (char charac : message.toCharArray()) {
            // Getting ASCII value of character
            int asciiValue = (int) charac;

            // Calculated ciphered value then add to encrypted message, separated by dot
            encrpytedMessage += (raiseToPower(asciiValue, this.e).mod(BigInteger.valueOf(this.n))).intValue() + ".";
        }
        // Return encrypted message
        return encrpytedMessage;
    }

    // Returns message encrypted in RSA
    public String decryptMessage(String message) {
        // Getting value of n if not yet
        this.n = this.p * this.q;

        // Turning message into array
        String[] encryptedCharacters = message.split("\\.");
        // Calculating private key
        this.d = getPrivateKey();
        String decrpytedMessage = "";

        // Iterating through each character in message
        for (String encrypCharac : encryptedCharacters) {
            int asciiValue;

            // Getting ASCII value of character
            asciiValue = Integer.valueOf(encrypCharac);

            // Add to encrypted message
            decrpytedMessage += (char) (raiseToPower(asciiValue, this.d).mod(BigInteger.valueOf(this.n))).intValue();
        }

        // Return encrypted message
        return decrpytedMessage;
    }

    // Custom Power Function; can handle huge numbers
    private BigInteger raiseToPower(int base, int power) {
        return BigInteger.valueOf(base).pow(power);
    }

    // public static void main(String[] args) {
    // RSAManager encryptor = new RSAManager();
    // String encrypted = encryptor.encryptMessage("I miss u Kix");
    // String decrypted = encryptor
    // .decryptMessage("219.1651.1606.1307.464.2987.1180.576.1823.1307.189.1651.1606.");
    // System.out.printf("Encrypted: %s\n", encrypted);
    // System.out.printf("Decrypted: %s\n", decrypted);
    // }
}