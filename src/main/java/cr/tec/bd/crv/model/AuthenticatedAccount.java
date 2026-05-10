package cr.tec.bd.crv.model;

/**
 * Account data returned after a successful login.
 *
 * <p>The controller uses this object to decide whether to open the admin menu
 * or the normal user menu.</p>
 */
public class AuthenticatedAccount {

    private final String accountType;
    private final String loginEmail;
    private final Long personId;

    public AuthenticatedAccount(String accountType, String loginEmail, Long personId) {
        this.accountType = accountType;
        this.loginEmail = loginEmail;
        this.personId = personId;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getLoginEmail() {
        return loginEmail;
    }

    public Long getPersonId() {
        return personId;
    }

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(accountType);
    }

    public boolean isUser() {
        return "USER".equalsIgnoreCase(accountType);
    }
}
