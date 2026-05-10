package cr.tec.bd.crv.model;

/**
 * Generic row used by administrative reports.
 *
 * <p>Reports do not all share the same meaning for each column, so the controller
 * changes the table headers while this model keeps five generic values.</p>
 */
public class ReportRow {

    private final String column1;
    private final String column2;
    private final String column3;
    private final String column4;
    private final String column5;

    public ReportRow(String column1, String column2, String column3, String column4, String column5) {
        this.column1 = column1;
        this.column2 = column2;
        this.column3 = column3;
        this.column4 = column4;
        this.column5 = column5;
    }

    public String getColumn1() {
        return column1;
    }

    public String getColumn2() {
        return column2;
    }

    public String getColumn3() {
        return column3;
    }

    public String getColumn4() {
        return column4;
    }

    public String getColumn5() {
        return column5;
    }
}
