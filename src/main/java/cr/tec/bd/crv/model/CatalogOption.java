package cr.tec.bd.crv.model;

/**
 * Represents one database catalog row shown inside a ComboBox.
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
