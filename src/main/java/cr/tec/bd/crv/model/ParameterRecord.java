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

    public ParameterRecord(long id, String name, String extra) {
        this.id = id;
        this.name = name;
        this.extra = extra;
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
}
