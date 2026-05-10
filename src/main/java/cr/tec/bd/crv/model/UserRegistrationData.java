package cr.tec.bd.crv.model;

/**
 * Carries user registration values from the screen to the repository.
 *
 * <p>It does not save anything by itself. It simply groups the typed values so
 * the controller does not need to pass ten separate parameters.</p>
 */
public class UserRegistrationData {

    // Names and last names stay separated because the database model stores them separately.
    private final String firstName;
    private final String secondName;
    private final String firstLastName;
    private final String secondLastName;
    private final String identification;
    private final String primaryEmail;
    private final String secondaryEmail;
    private final String primaryPhone;
    private final String secondaryPhone;
    private final String password;

    public UserRegistrationData(
            String firstName,
            String secondName,
            String firstLastName,
            String secondLastName,
            String identification,
            String primaryEmail,
            String secondaryEmail,
            String primaryPhone,
            String secondaryPhone,
            String password
    ) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.firstLastName = firstLastName;
        this.secondLastName = secondLastName;
        this.identification = identification;
        this.primaryEmail = primaryEmail;
        this.secondaryEmail = secondaryEmail;
        this.primaryPhone = primaryPhone;
        this.secondaryPhone = secondaryPhone;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getFirstLastName() {
        return firstLastName;
    }

    public String getSecondLastName() {
        return secondLastName;
    }

    public String getIdentification() {
        return identification;
    }

    public String getPrimaryEmail() {
        return primaryEmail;
    }

    public String getSecondaryEmail() {
        return secondaryEmail;
    }

    public String getPrimaryPhone() {
        return primaryPhone;
    }

    public String getSecondaryPhone() {
        return secondaryPhone;
    }

    public String getPassword() {
        return password;
    }
}
