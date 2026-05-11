package cr.tec.bd.crv.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Reads image files before they are stored in the database.
 *
 * <p>The interface lets the user choose a local image, but other users cannot
 * see that local file. This helper turns the selected file into bytes so the
 * repository can store those bytes in Oracle BLOB columns.</p>
 */
public final class PhotoStorageUtil {

    private PhotoStorageUtil() {
    }

    /**
     * Builds a database-ready photo object from a path chosen in the interface.
     */
    public static StoredPhoto fromPath(String photoPath, boolean requireReadableFile) {
        String normalizedPath = normalize(photoPath);
        if (normalizedPath == null) {
            return null;
        }

        Path path = Path.of(normalizedPath);
        if (!Files.isRegularFile(path)) {
            if (requireReadableFile) {
                throw new IllegalArgumentException("Select a valid image file before saving the photo.");
            }
            return new StoredPhoto(normalizedPath, normalizedPath, null, null);
        }

        try {
            String fileName = path.getFileName().toString();
            String mimeType = Files.probeContentType(path);
            if (mimeType == null || mimeType.isBlank()) {
                mimeType = "application/octet-stream";
            }
            return new StoredPhoto(normalizedPath, fileName, mimeType, Files.readAllBytes(path));
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not read the selected image file: " + e.getMessage(), e);
        }
    }

    /**
     * Reads bytes from an old path-only photo when no BLOB was saved yet.
     */
    public static byte[] readBytesIfFileExists(String photoPath) {
        String normalizedPath = normalize(photoPath);
        if (normalizedPath == null) {
            return null;
        }

        Path path = Path.of(normalizedPath);
        if (!Files.isRegularFile(path)) {
            return null;
        }

        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            return null;
        }
    }

    private static String normalize(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return value.trim();
    }

    public record StoredPhoto(String storedPath, String fileName, String mimeType, byte[] data) {
    }
}
