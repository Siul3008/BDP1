package cr.tec.bd.crv.model;

import java.time.LocalDate;

/**
 * Search filters for pet lists.
 *
 * <p>Any field may be null, which means that filter is not being used.</p>
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
