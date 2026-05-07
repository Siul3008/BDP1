package cr.tec.bd.crv.model;

/**
 * Small label/value pair used by charts and summaries.
 */
public class ChartDataPoint {

    private final String label;
    private final int value;

    public ChartDataPoint(String label, int value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public int getValue() {
        return value;
    }
}
