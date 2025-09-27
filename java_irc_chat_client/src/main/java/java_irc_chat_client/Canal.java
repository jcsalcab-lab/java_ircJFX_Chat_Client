package java_irc_chat_client;

import javafx.beans.property.*;

public class Canal {

    private final StringProperty nombre = new SimpleStringProperty();
    private final IntegerProperty numUsuarios = new SimpleIntegerProperty();
    private final StringProperty permisos = new SimpleStringProperty();
    private final StringProperty descripcion = new SimpleStringProperty();

    public Canal(String nombre, int numUsuarios, String permisos, String descripcion) {
        this.nombre.set(nombre);
        this.numUsuarios.set(numUsuarios);
        this.permisos.set(permisos);
        this.descripcion.set(descripcion);
    }

    public String getNombre() { return nombre.get(); }
    public void setNombre(String nombre) { this.nombre.set(nombre); }
    public StringProperty nombreProperty() { return nombre; }

    public int getNumUsuarios() { return numUsuarios.get(); }
    public void setNumUsuarios(int num) { this.numUsuarios.set(num); }
    public IntegerProperty numUsuariosProperty() { return numUsuarios; }

    public String getPermisos() { return permisos.get(); }
    public void setPermisos(String permisos) { this.permisos.set(permisos); }
    public StringProperty permisosProperty() { return permisos; }

    public String getDescripcion() { return descripcion.get(); }
    public void setDescripcion(String descripcion) { this.descripcion.set(descripcion); }
    public StringProperty descripcionProperty() { return descripcion; }
}
