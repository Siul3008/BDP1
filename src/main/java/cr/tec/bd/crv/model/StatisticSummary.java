package cr.tec.bd.crv.model;

import java.util.List;

/**
 * Aggregated numbers displayed in the statistics dashboard.
 *
 * <p>The repository calculates these values once, then the controller uses this
 * object to fill cards and charts.</p>
 */
public class StatisticSummary {

    private final int totalPets;
    private final int adoptedPets;
    private final String adoptionRateText;
    private final String donationTotalText;
    private final int activeFosterHomes;
    private final List<ChartDataPoint> petsByStatus;
    private final List<ChartDataPoint> petsByType;
    private final List<ChartDataPoint> donationsByAssociation;
    private final List<ChartDataPoint> adoptionOutcome;
    private final List<ChartDataPoint> petsByAgeRange;
    private final List<ChartDataPoint> criticalAdoptionPetsByType;

    public StatisticSummary(
            int totalPets,
            int adoptedPets,
            String adoptionRateText,
            String donationTotalText,
            int activeFosterHomes,
            List<ChartDataPoint> petsByStatus,
            List<ChartDataPoint> petsByType,
            List<ChartDataPoint> donationsByAssociation,
            List<ChartDataPoint> adoptionOutcome,
            List<ChartDataPoint> petsByAgeRange,
            List<ChartDataPoint> criticalAdoptionPetsByType
    ) {
        this.totalPets = totalPets;
        this.adoptedPets = adoptedPets;
        this.adoptionRateText = adoptionRateText;
        this.donationTotalText = donationTotalText;
        this.activeFosterHomes = activeFosterHomes;
        this.petsByStatus = petsByStatus;
        this.petsByType = petsByType;
        this.donationsByAssociation = donationsByAssociation;
        this.adoptionOutcome = adoptionOutcome;
        this.petsByAgeRange = petsByAgeRange;
        this.criticalAdoptionPetsByType = criticalAdoptionPetsByType;
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

    public List<ChartDataPoint> getDonationsByAssociation() {
        return donationsByAssociation;
    }

    public List<ChartDataPoint> getAdoptionOutcome() {
        return adoptionOutcome;
    }

    public List<ChartDataPoint> getPetsByAgeRange() {
        return petsByAgeRange;
    }

    public List<ChartDataPoint> getCriticalAdoptionPetsByType() {
        return criticalAdoptionPetsByType;
    }
}
