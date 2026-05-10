package cr.tec.bd.crv.util;

/**
 * Keeps the small amount of information needed about the signed-in account.
 *
 * <p>JavaFX controllers are recreated when screens change, so they cannot rely
 * on normal controller fields to remember who is using the app. This class acts
 * like a lightweight session: it remembers whether the current account is an
 * admin or a user, which person is active, and which pet is being edited.</p>
 */
public final class SessionContext {

    private static String currentRole;
    private static Long currentPersonId;
    private static String currentEmail;
    private static Long editingPetId;

    private SessionContext() {
    }

    /**
     * Stores a generic role when only the menu type is needed.
     */
    public static void setRole(String role) {
        currentRole = role;
    }

    /**
     * Starts a normal user session and remembers the linked person id.
     */
    public static void setUserSession(long personId, String email) {
        currentRole = "USER";
        currentPersonId = personId;
        currentEmail = email;
    }

    /**
     * Starts an administrator session. Admin accounts are not tied to one person row.
     */
    public static void setAdminSession(String email) {
        currentRole = "ADMIN";
        currentPersonId = null;
        currentEmail = email;
    }

    /**
     * Returns true when the current account should see administrative options.
     */
    public static boolean isAdmin() {
        return "ADMIN".equals(currentRole);
    }

    public static Long getCurrentPersonId() {
        return currentPersonId;
    }

    public static String getCurrentEmail() {
        return currentEmail;
    }

    /**
     * Temporarily stores a pet id before opening the edit screen.
     */
    public static void setEditingPetId(Long petId) {
        editingPetId = petId;
    }

    /**
     * Reads the pending pet id once and clears it so future registrations start clean.
     */
    public static Long consumeEditingPetId() {
        Long petId = editingPetId;
        editingPetId = null;
        return petId;
    }

    /**
     * Updates the session email after the user edits their profile contact email.
     */
    public static void updateCurrentEmail(String email) {
        currentEmail = email;
    }

    /**
     * Clears the session when the user logs out or changes accounts.
     */
    public static void clear() {
        currentRole = null;
        currentPersonId = null;
        currentEmail = null;
        editingPetId = null;
    }
}
