package cr.tec.bd.crv.model;

/**
 * Immutable data transfer object for association registration form values.
 */
public class AssociationRegistrationData {

    private final String name;
    private final String identification;
    private final String primaryEmail;
    private final String password;

    public AssociationRegistrationData(String name, String identification, String primaryEmail, String password) {
        this.name = name;
        this.identification = identification;
        this.primaryEmail = primaryEmail;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getIdentification() {
        return identification;
    }

    public String getPrimaryEmail() {
        return primaryEmail;
    }

    public String getPassword() {
        return password;
    }
}
