package cr.tec.bd.crv.model;

import java.util.List;

/**
 * Aggregated numbers displayed in the statistics dashboard.
 */
public class StatisticSummary {

    private final int totalPets;
    private final int adoptedPets;
    private final String adoptionRateText;
    private final String donationTotalText;
    private final int activeFosterHomes;
    private final List<ChartDataPoint> petsByStatus;
    private final List<ChartDataPoint> petsByType;

    public StatisticSummary(
            int totalPets,
            int adoptedPets,
            String adoptionRateText,
            String donationTotalText,
            int activeFosterHomes,
            List<ChartDataPoint> petsByStatus,
            List<ChartDataPoint> petsByType
    ) {
        this.totalPets = totalPets;
        this.adoptedPets = adoptedPets;
        this.adoptionRateText = adoptionRateText;
        this.donationTotalText = donationTotalText;
        this.activeFosterHomes = activeFosterHomes;
        this.petsByStatus = petsByStatus;
        this.petsByType = petsByType;
    }

    public int getTotalPets() {
        return totalPets;
    }

    public int getAdoptedPets() {
        return adoptedPets;
    }

    public String getAdoptionRateText() {
        return adoptionRateText;
    }

    public String getDonationTotalText() {
        return donationTotalText;
    }

    public int getActiveFosterHomes() {
        return activeFosterHomes;
    }

    public List<ChartDataPoint> getPetsByStatus() {
        return petsByStatus;
    }

    public List<ChartDataPoint> getPetsByType() {
        return petsByType;
    }
}
