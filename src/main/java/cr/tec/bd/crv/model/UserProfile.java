package cr.tec.bd.crv.model;

/**
 * Editable profile data for a logged-in user.
 */
public class UserProfile {

    private final long personId;
    private final String firstName;
    private final String secondName;
    private final String firstLastName;
    private final String secondLastName;
    private final String identification;
    private final String primaryEmail;
    private final String primaryPhone;

    public UserProfile(
            long personId,
            String firstName,
            String secondName,
            String firstLastName,
            String secondLastName,
            String identification,
            String primaryEmail,
            String primaryPhone
    ) {
        this.personId = personId;
        this.firstName = firstName;
        this.secondName = secondName;
        this.firstLastName = firstLastName;
        this.secondLastName = secondLastName;
        this.identification = identification;
        this.primaryEmail = primaryEmail;
        this.primaryPhone = primaryPhone;
    }

    public long getPersonId() {
        return personId;
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

    public String getPrimaryPhone() {
        return primaryPhone;
    }

    public String getDisplayName() {
        return (firstName + " " + firstLastName).trim();
    }
}
