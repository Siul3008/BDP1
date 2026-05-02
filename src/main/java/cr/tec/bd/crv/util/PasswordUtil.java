package cr.tec.bd.crv.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Password helper for storing login credentials safely enough for this course project.
 */
public final class PasswordUtil {

    private PasswordUtil() {
    }

    // Stores passwords as SHA-256 hashes to avoid saving plain text credentials.
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
