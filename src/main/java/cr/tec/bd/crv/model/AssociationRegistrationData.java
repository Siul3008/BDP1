package cr.tec.bd.crv.model;

/**
 * Groups association registration values.
 *
 * <p>This class is kept as a simple container in case association registration
 * needs a dedicated form again.</p>
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
