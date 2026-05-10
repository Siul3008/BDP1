package cr.tec.bd.crv.model;

/**
 * Small label/value pair used by charts and summaries.
 *
 * <p>For example, label could be "Dogs" and value could be the number of dog
 * records found.</p>
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
