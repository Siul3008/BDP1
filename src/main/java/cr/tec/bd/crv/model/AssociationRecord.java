package cr.tec.bd.crv.model;

/**
 * Association row shown in the admin association table.
 */
public class AssociationRecord {

    private final long id;
    private final String name;
    private final int donationCount;
    private final String totalDonated;

    public AssociationRecord(long id, String name, int donationCount, String totalDonated) {
        this.id = id;
        this.name = name;
        this.donationCount = donationCount;
        this.totalDonated = totalDonated;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDonationCount() {
        return donationCount;
    }

    public String getTotalDonated() {
        return totalDonated;
    }
}
