package cr.tec.bd.crv.util;

/**
 * Stores minimal session state for navigation decisions.
 */
public final class SessionContext {

    private static String currentRole;

    private SessionContext() {
    }

    public static void setRole(String role) {
        currentRole = role;
    }

    public static boolean isAdmin() {
        return "ADMIN".equals(currentRole);
    }

    public static void clear() {
        currentRole = null;
    }
}
