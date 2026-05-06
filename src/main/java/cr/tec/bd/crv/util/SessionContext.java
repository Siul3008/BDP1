package cr.tec.bd.crv.util;

/**
 * Stores minimal session state for navigation decisions.
 */
public final class SessionContext {

    private static String currentRole;
    private static Long currentPersonId;
    private static String currentEmail;

    private SessionContext() {
    }

    public static void setRole(String role) {
        currentRole = role;
    }

    public static void setUserSession(long personId, String email) {
        currentRole = "USER";
        currentPersonId = personId;
        currentEmail = email;
    }

    public static void setAdminSession(String email) {
        currentRole = "ADMIN";
        currentPersonId = null;
        currentEmail = email;
    }

    public static boolean isAdmin() {
        return "ADMIN".equals(currentRole);
    }

    public static Long getCurrentPersonId() {
        return currentPersonId;
    }

    public static String getCurrentEmail() {
        return currentEmail;
    }

    public static void updateCurrentEmail(String email) {
        currentEmail = email;
    }

    public static void clear() {
        currentRole = null;
        currentPersonId = null;
        currentEmail = null;
    }
}
