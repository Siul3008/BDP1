package cr.tec.bd.crv.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Converts passwords into a stored hash.
 *
 * <p>The application never needs to keep the real password text after the user
 * types it. Instead, it stores a SHA-256 hash and later compares hashes during
 * login. This is why the database table contains long hash strings instead of
 * readable passwords.</p>
 */
public final class PasswordUtil {

    private PasswordUtil() {
    }

    /**
     * Returns the SHA-256 hash for the received password text.
     */
    public static String hash(String rawPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder();
            for (byte hashByte : hashBytes) {
                // Manual hex conversion avoids IDE/JDK issues seen with String.format("%02x", byte).
                String hex = Integer.toHexString(hashByte & 0xff);
                if (hex.length() == 1) {
                    builder.append('0');
                }
                builder.append(hex);
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is not available", e);
        }
    }
}
