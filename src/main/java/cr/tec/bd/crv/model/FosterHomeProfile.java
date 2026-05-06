package cr.tec.bd.crv.model;

import java.util.List;

/**
 * Foster home configuration linked to the current user.
 */
public class FosterHomeProfile {

    private final boolean active;
    private final Long foodDonationId;
    private final String notes;
    private final List<Long> acceptedTypeIds;
    private final List<Long> acceptedSizeIds;

    public FosterHomeProfile(
            boolean active,
            Long foodDonationId,
            String notes,
            List<Long> acceptedTypeIds,
            List<Long> acceptedSizeIds
    ) {
        this.active = active;
        this.foodDonationId = foodDonationId;
        this.notes = notes;
        this.acceptedTypeIds = acceptedTypeIds;
        this.acceptedSizeIds = acceptedSizeIds;
    }

    public boolean isActive() {
        return active;
    }

    public Long getFoodDonationId() {
        return foodDonationId;
    }

    public String getNotes() {
        return notes;
    }

    public List<Long> getAcceptedTypeIds() {
        return acceptedTypeIds;
    }

    public List<Long> getAcceptedSizeIds() {
        return acceptedSizeIds;
    }
}
