package cr.tec.bd.crv.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Donation row shown in the donations table.
 *
 * <p>It includes both raw values and formatted text so the table can show money
 * and dates cleanly.</p>
 */
public class DonationRecord {

    private final long id;
    private final String donorName;
    private final String associationName;
    private final BigDecimal amount;
    private final String currency;
    private final LocalDate donationDate;

    public DonationRecord(
            long id,
            String donorName,
            String associationName,
            BigDecimal amount,
            String currency,
            LocalDate donationDate
    ) {
        this.id = id;
        this.donorName = donorName;
        this.associationName = associationName;
        this.amount = amount;
        this.currency = currency;
        this.donationDate = donationDate;
    }

    public long getId() {
        return id;
    }

    public String getDonorName() {
        return donorName;
    }

    public String getAssociationName() {
        return associationName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public LocalDate getDonationDate() {
        return donationDate;
    }

    public String getAmountText() {
        if (amount == null) {
            return "";
        }
        String currencyText = currency == null ? "" : currency + " ";
        return currencyText + amount.toPlainString();
    }

    public String getDonationDateText() {
        return donationDate == null ? "" : donationDate.toString();
    }
}
