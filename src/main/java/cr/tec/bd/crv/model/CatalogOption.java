package cr.tec.bd.crv.model;

/**
 * Represents one selectable option loaded from a catalog table.
 *
 * <p>The id is what the database needs; the label is what the user sees in the
 * ComboBox.</p>
 */
public class CatalogOption {

    private final long id;
    private final String label;

    public CatalogOption(long id, String label) {
        this.id = id;
        this.label = label;
    }

    public long getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
