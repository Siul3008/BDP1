package cr.tec.bd.crv.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Groups all values from the pet form.
 *
 * <p>The pet form is large, so the controller creates one PetFormData object and
 * sends it to the repository. That makes saving and editing pets easier to read.</p>
 */
public class PetFormData {

    private final String name;
    private final Long petTypeId;
    private final Long breedId;
    private final Long petStatusId;
    private final Long trainingEaseId;
    private final Long districtId;
    private final Long currencyId;
    private final Long colorId;
    private final String chip;
    private final Long petSizeId;
    private final String needSpace;
    private final String energyLevel;
    private final String contactPhone;
    private final String contactEmail;
    private final BigDecimal rewardAmount;
    private final LocalDate eventDate;
    private final String photoBeforePath;
    private final String photoAfterPath;
    private final String description;
    private final String healthState;
    private final String healthDescription;
    private final Long diseaseId;
    private final Long treatmentId;
    private final Long medicineId;
    private final String medicineDose;
    private final Long veterinarianId;
    private final String veterinarianName;

    public PetFormData(
            String name,
            Long petTypeId,
            Long breedId,
            Long petStatusId,
            Long trainingEaseId,
            Long districtId,
            Long currencyId,
            Long colorId,
            String chip,
            Long petSizeId,
            String needSpace,
            String energyLevel,
            String contactPhone,
            String contactEmail,
            BigDecimal rewardAmount,
            LocalDate eventDate,
            String photoBeforePath,
            String photoAfterPath,
            String description,
            String healthState,
            String healthDescription,
            Long diseaseId,
            Long treatmentId,
            Long medicineId,
            String medicineDose,
            Long veterinarianId,
            String veterinarianName
    ) {
        this.name = name;
        this.petTypeId = petTypeId;
        this.breedId = breedId;
        this.petStatusId = petStatusId;
        this.trainingEaseId = trainingEaseId;
        this.districtId = districtId;
        this.currencyId = currencyId;
        this.colorId = colorId;
        this.chip = chip;
        this.petSizeId = petSizeId;
        this.needSpace = needSpace;
        this.energyLevel = energyLevel;
        this.contactPhone = contactPhone;
        this.contactEmail = contactEmail;
        this.rewardAmount = rewardAmount;
        this.eventDate = eventDate;
        this.photoBeforePath = photoBeforePath;
        this.photoAfterPath = photoAfterPath;
        this.description = description;
        this.healthState = healthState;
        this.healthDescription = healthDescription;
        this.diseaseId = diseaseId;
        this.treatmentId = treatmentId;
        this.medicineId = medicineId;
        this.medicineDose = medicineDose;
        this.veterinarianId = veterinarianId;
        this.veterinarianName = veterinarianName;
    }

    public String getName() {
        return name;
    }

    public Long getPetTypeId() {
        return petTypeId;
    }

    public Long getBreedId() {
        return breedId;
    }

    public Long getPetStatusId() {
        return petStatusId;
    }

    public Long getTrainingEaseId() {
        return trainingEaseId;
    }

    public Long getDistrictId() {
        return districtId;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public Long getColorId() {
        return colorId;
    }

    public String getChip() {
        return chip;
    }

    public Long getPetSizeId() {
        return petSizeId;
    }

    public String getNeedSpace() {
        return needSpace;
    }

    public String getEnergyLevel() {
        return energyLevel;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public BigDecimal getRewardAmount() {
        return rewardAmount;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public String getPhotoBeforePath() {
        return photoBeforePath;
    }

    public String getPhotoAfterPath() {
        return photoAfterPath;
    }

    public String getDescription() {
        return description;
    }

    public String getHealthState() {
        return healthState;
    }

    public String getHealthDescription() {
        return healthDescription;
    }

    public Long getDiseaseId() {
        return diseaseId;
    }

    public Long getTreatmentId() {
        return treatmentId;
    }

    public Long getMedicineId() {
        return medicineId;
    }

    public String getMedicineDose() {
        return medicineDose;
    }

    public Long getVeterinarianId() {
        return veterinarianId;
    }

    public String getVeterinarianName() {
        return veterinarianName;
    }
}
