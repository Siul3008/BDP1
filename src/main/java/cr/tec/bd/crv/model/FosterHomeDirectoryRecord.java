package cr.tec.bd.crv.model;

/**
 * Public foster home row shown to users who need temporary help for a pet.
 */
public class FosterHomeDirectoryRecord {

    private final String name;
    private final String email;
    private final String phone;
    private final String acceptedTypes;
    private final String acceptedSizes;
    private final String foodDonation;
    private final String notes;

    public FosterHomeDirectoryRecord(
            String name,
            String email,
            String phone,
            String acceptedTypes,
            String acceptedSizes,
            String foodDonation,
            String notes
    ) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.acceptedTypes = acceptedTypes;
        this.acceptedSizes = acceptedSizes;
        this.foodDonation = foodDonation;
        this.notes = notes;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getContact() {
        if (email == null || email.isBlank()) {
            return phone == null ? "" : phone;
        }
        if (phone == null || phone.isBlank()) {
            return email;
        }
        return email + " / " + phone;
    }

    public String getAcceptedTypes() {
        return acceptedTypes;
    }

    public String getAcceptedSizes() {
        return acceptedSizes;
    }

    public String getFoodDonation() {
        return foodDonation;
    }

    public String getNotes() {
        return notes;
    }
}
