package cr.tec.bd.crv.model;

public class Mascota {

    private int id;
    private String nombre;
    private String tipo;
    private String raza;
    private String color;
    private String estado;

    public Mascota(int id, String nombre, String tipo, String raza, String color, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.raza = raza;
        this.color = color;
        this.estado = estado;
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
}