package cr.tec.bd.crv.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Adoption row shown in the adoption history table.
 */
public class AdoptionRecord {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final long id;
    private final String adopterName;
    private final String petName;
    private final LocalDate adoptionDate;
    private final String rating;
    private final String followUpNotes;

    public AdoptionRecord(
            long id,
            String adopterName,
            String petName,
            LocalDate adoptionDate,
            String rating,
            String followUpNotes
    ) {
        this.id = id;
        this.adopterName = adopterName;
        this.petName = petName;
        this.adoptionDate = adoptionDate;
        this.rating = rating;
        this.followUpNotes = followUpNotes;
    }

    public long getId() {
        return id;
    }

    public String getAdopterName() {
        return adopterName;
    }

    public String getPetName() {
        return petName;
    }

    public LocalDate getAdoptionDate() {
        return adoptionDate;
    }

    public String getAdoptionDateText() {
        return adoptionDate == null ? "" : adoptionDate.format(DATE_FORMATTER);
    }

    public String getRating() {
        return rating;
    }

    public String getFollowUpNotes() {
        return followUpNotes;
    }
}
