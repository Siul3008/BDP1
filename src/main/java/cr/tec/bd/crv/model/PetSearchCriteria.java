package cr.tec.bd.crv.model;

import java.time.LocalDate;

/**
 * Optional filters used by the pet search and list screens.
 */
public class PetSearchCriteria {

    private final String text;
    private final Long statusId;
    private final LocalDate fromDate;
    private final LocalDate toDate;

    public PetSearchCriteria(String text, Long statusId, LocalDate fromDate, LocalDate toDate) {
        this.text = text;
        this.statusId = statusId;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public String getText() {
        return text;
    }

    public Long getStatusId() {
        return statusId;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }
}
