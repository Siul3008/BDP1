package cr.tec.bd.crv.model;

/**
 * Generic row used by the admin parameter/catalog maintenance screen.
 *
 * <p>The extra value means different things depending on the catalog, such as
 * pet type for breeds or acronym for currencies.</p>
 */
public class ParameterRecord {

    private final long id;
    private final String name;
    private final String extra;
    private final String description;

    public ParameterRecord(long id, String name, String extra) {
        this(id, name, extra, "");
    }

    public ParameterRecord(long id, String name, String extra, String description) {
        this.id = id;
        this.name = name;
        this.extra = extra;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getExtra() {
        return extra;
    }

    public String getDescription() {
        return description;
    }
}
