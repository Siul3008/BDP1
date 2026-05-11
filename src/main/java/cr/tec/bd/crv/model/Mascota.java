package cr.tec.bd.crv.model;

/**
 * Pet row shown in pet tables.
 *
 * <p>The field names are in Spanish because the JavaFX table columns use these
 * getter names directly when painting the visible table.</p>
 */
public class Mascota {

    // Field names match the TableView PropertyValueFactory keys.
    private int id;
    private String nombre;
    private String tipo;
    private String raza;
    private String color;
    private String estado;
    private Integer edad;
    private String lugar;
    private String fechaEvento;

    public Mascota(int id, String nombre, String tipo, String raza, String color, String estado) {
        this(id, nombre, tipo, raza, color, estado, null, "", "");
    }

    public Mascota(
            int id,
            String nombre,
            String tipo,
            String raza,
            String color,
            String estado,
            Integer edad,
            String lugar,
            String fechaEvento
    ) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.raza = raza;
        this.color = color;
        this.estado = estado;
        this.edad = edad;
        this.lugar = lugar;
        this.fechaEvento = fechaEvento;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public String getRaza() {
        return raza;
    }

    public String getColor() {
        return color;
    }

    public String getEstado() {
        return estado;
    }

    public Integer getEdad() {
        return edad;
    }

    public String getLugar() {
        return lugar;
    }

    public String getFechaEvento() {
        return fechaEvento;
    }
}
