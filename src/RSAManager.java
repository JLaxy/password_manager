import java.math.BigInteger;

class RSAManager {

    // Keys
    private int p = 77, q = 41, e = 29, r, d, n;

    public RSAManager() {
        // Checks if inputsa re valid
        areInputsValid();
    }

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

    // Returns true if all inputs are valid
    private boolean areInputsValid() {
        try {
            // Verify if P and Q are valid
            if (!(isPrime(p) && isPrime(q) && isPrime(e)))
                throw new Exception("P, Q and E are not prime");
            else if (p * q < e)
                throw new Exception("E should not be larger than (P*Q)");
            else if (areCoPrime(e, n))
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
        this.r = (p - 1) * (q - 1);
        this.d = 0;
        while (true) {
            if ((e * d) % r == 1)
                break;
            ++d;
        }

        return d;
    }

    // Returns message encrypted in RSA
    public String encryptMessage(String message) {
        String encrpytedMessage = "";

        // Getting value of n
        this.n = p * q;
        // Iterating through each character in message
        for (char charac : message.toCharArray()) {
            // Getting ASCII value of character
            int asciiValue = (int) charac;

            // Add space if space character
            if (asciiValue == 32) {
                encrpytedMessage += " ";
                continue;
            }
            // Calculated ciphered value then add to encrypted message, separated by dot
            encrpytedMessage += (raiseToPower(asciiValue, e).mod(BigInteger.valueOf(n))).intValue() + ".";
        }
        // Return encrypted message
        return encrpytedMessage;
    }

    // Returns message encrypted in RSA
    public String decryptMessage(String message) {
        // Turning message into array
        String[] encryptedCharacters = message.split("\\.");
        // Calculating private key
        this.d = getPrivateKey();
        String decrpytedMessage = "";

        // Iterating through each character in message
        for (String encrypCharac : encryptedCharacters) {
            int asciiValue;
            boolean hasSpaceBefore = false;

            // Trying to catch number with space
            try {
                // Getting ASCII value of character
                asciiValue = Integer.valueOf(encrypCharac);

                // If has space before
            } catch (Exception e) {
                // Remove space
                asciiValue = Integer.valueOf(encrypCharac.replace(" ", ""));
                hasSpaceBefore = true;
            }

            // Add space if next character had space
            if (hasSpaceBefore) {
                decrpytedMessage += " " + (char) (raiseToPower(asciiValue, d).mod(BigInteger.valueOf(n))).intValue();
                continue;
            }

            // Add to encrypted message
            decrpytedMessage += (char) (raiseToPower(asciiValue, d).mod(BigInteger.valueOf(n))).intValue();
        }

        // Return encrypted message
        return decrpytedMessage;
    }

    // Custom Power Function; can handle huge numbers
    private BigInteger raiseToPower(int base, int power) {
        return BigInteger.valueOf(base).pow(power);
    }

    public static void main(String[] args) {
        RSAManager encryptor = new RSAManager();
        String encrypted = encryptor.encryptMessage("I miss u kix");
        String decrypted = encryptor.decryptMessage(encrypted);
        System.out.printf("Encrypted: %s\n", encrypted);
        System.out.printf("Decrypted: %s\n", decrypted);
    }
}