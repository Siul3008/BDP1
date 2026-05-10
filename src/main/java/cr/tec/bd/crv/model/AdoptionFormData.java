package cr.tec.bd.crv.model;

import java.time.LocalDate;

/**
 * Groups values entered in the adoption registration form.
 *
 * <p>The repository uses this data to create the application, rating, adoption
 * record, follow-up notes, and optional photos.</p>
 */
public class AdoptionFormData {

    private final Long petId;
    private final String adopterEmail;
    private final LocalDate adoptionDate;
    private final String yard;
    private final String exerciseTime;
    private final String housingType;
    private final String otherPets;
    private final String answers;
    private final String rating;
    private final String adopterNotes;
    private final String followUpNotes;
    private final String adoptionPhotoPath;
    private final String followUpPhotoPath;

    public AdoptionFormData(
            Long petId,
            String adopterEmail,
            LocalDate adoptionDate,
            String yard,
            String exerciseTime,
            String housingType,
            String otherPets,
            String answers,
            String rating,
            String adopterNotes,
            String followUpNotes,
            String adoptionPhotoPath,
            String followUpPhotoPath
    ) {
        this.petId = petId;
        this.adopterEmail = adopterEmail;
        this.adoptionDate = adoptionDate;
        this.yard = yard;
        this.exerciseTime = exerciseTime;
        this.housingType = housingType;
        this.otherPets = otherPets;
        this.answers = answers;
        this.rating = rating;
        this.adopterNotes = adopterNotes;
        this.followUpNotes = followUpNotes;
        this.adoptionPhotoPath = adoptionPhotoPath;
        this.followUpPhotoPath = followUpPhotoPath;
    }

    public Long getPetId() {
        return petId;
    }

    public String getAdopterEmail() {
        return adopterEmail;
    }

    public LocalDate getAdoptionDate() {
        return adoptionDate;
    }

    public String getYard() {
        return yard;
    }

    public String getExerciseTime() {
        return exerciseTime;
    }

    public String getHousingType() {
        return housingType;
    }

    public String getOtherPets() {
        return otherPets;
    }

    public String getAnswers() {
        return answers;
    }

    public String getRating() {
        return rating;
    }

    public String getAdopterNotes() {
        return adopterNotes;
    }

    public String getFollowUpNotes() {
        return followUpNotes;
    }

    public String getAdoptionPhotoPath() {
        return adoptionPhotoPath;
    }

    public String getFollowUpPhotoPath() {
        return followUpPhotoPath;
    }
}
