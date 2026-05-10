package cr.tec.bd.crv.model;

import java.time.LocalDate;

/**
 * Row shown in the blacklist module.
 *
 * <p>It includes report details plus the average rating for the reported person.</p>
 */
public class BlacklistRecord {

    private final long id;
    private final String reporterName;
    private final String reporteeName;
    private final String rating;
    private final String averageRating;
    private final String reason;
    private final String active;
    private final LocalDate reportDate;

    public BlacklistRecord(
            long id,
            String reporterName,
            String reporteeName,
            String rating,
            String averageRating,
            String reason,
            String active,
            LocalDate reportDate
    ) {
        this.id = id;
        this.reporterName = reporterName;
        this.reporteeName = reporteeName;
        this.rating = rating;
        this.averageRating = averageRating;
        this.reason = reason;
        this.active = active;
        this.reportDate = reportDate;
    }

    public long getId() {
        return id;
    }

    public String getReporterName() {
        return reporterName;
    }

    public String getReporteeName() {
        return reporteeName;
    }

    public String getRating() {
        return rating;
    }

    public String getAverageRating() {
        return averageRating;
    }

    public String getReason() {
        return reason;
    }

    public String getActive() {
        return active;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }
}
