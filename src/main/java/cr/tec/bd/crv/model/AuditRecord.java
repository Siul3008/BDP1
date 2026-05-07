package cr.tec.bd.crv.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Audit or journal row shown in the admin log screen.
 */
public class AuditRecord {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final long id;
    private final String moduleName;
    private final String fieldName;
    private final String previousValue;
    private final String currentValue;
    private final String changedBy;
    private final LocalDate changeDate;

    public AuditRecord(
            long id,
            String moduleName,
            String fieldName,
            String previousValue,
            String currentValue,
            String changedBy,
            LocalDate changeDate
    ) {
        this.id = id;
        this.moduleName = moduleName;
        this.fieldName = fieldName;
        this.previousValue = previousValue;
        this.currentValue = currentValue;
        this.changedBy = changedBy;
        this.changeDate = changeDate;
    }

    public long getId() {
        return id;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getPreviousValue() {
        return previousValue;
    }

    public String getCurrentValue() {
        return currentValue;
    }

    public String getChangedBy() {
        return changedBy;
    }

    public LocalDate getChangeDate() {
        return changeDate;
    }

    public String getChangeDateText() {
        return changeDate == null ? "" : changeDate.format(DATE_FORMATTER);
    }
}
