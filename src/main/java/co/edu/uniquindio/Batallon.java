package co.edu.uniquindio;

import java.util.ArrayList;

public class Batallon {
    private String codigo;
    private String nombre;
    private String brigada;
    private String distrito;
    private int numero;
    private ArrayList<Vehiculo> listaVehiculos;
    private ArrayList<Mision> listaMision;

    public Batallon(String codigo, String nombre, String brigada, String distrito, int numero) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.brigada = brigada;
        this.distrito = distrito;
        this.numero = numero;
        this.listaVehiculos = new ArrayList<>();
        this.listaMision = new ArrayList<>();
    }

    public boolean registrarVehiculo(Vehiculo vehiculo) {
        boolean registrado = false;
        if (vehiculo != null && vehiculo.esValido() && buscarVehiculo(vehiculo.getId()) == null) {
            listaVehiculos.add(vehiculo);
            registrado = true;
        }
        return registrado;
    }

    public Vehiculo buscarVehiculo(String id) {
        Vehiculo encontrado = null;
        if (id != null) {
            for (Vehiculo vehiculo : listaVehiculos) {
                if (vehiculo.getId().equals(id.trim())) {
                    encontrado = vehiculo;
                }
            }
        }
        return encontrado;
    }

    public boolean eliminarVehiculo(String idVehiculo){
        Vehiculo encontrado = buscarVehiculo(idVehiculo);
        boolean elimando = false;
        if (encontrado != null && encontrado.getListaDetallesMision().isEmpty()){
            elimando = listaVehiculos.remove(encontrado);
        }
        return elimando;
    }


    public boolean registrarMision(Mision mision) {
        boolean registrada = false;
        if (mision != null && mision.esValida() && buscarMision(mision.getId()) == null
                && mision.getEstado() == EstadoMision.PLANIFICADA) {
            boolean vehiculosRegistrados = true;
            for (DetalleMision detalle : mision.getListaDetallesMision()) {
                if (buscarVehiculo(detalle.getVehiculo().getId()) != detalle.getVehiculo()) {
                    vehiculosRegistrados = false;
                }
            }
            if (vehiculosRegistrados) {
                listaMision.add(mision);
                registrada = true;
            }
        }
        return registrada;
    }

    public Mision buscarMision(String id) {
        Mision encontrada = null;
        if (id != null) {
            for (Mision mision : listaMision) {
                if (mision.getId().equals(id.trim())) {
                    encontrada = mision;
                }
            }
        }
        return encontrada;
    }

    public boolean asignarVehiculoMision(String idMision, String idVehiculo) {
        boolean asignado = false;
        Mision mision = buscarMision(idMision);
        Vehiculo vehiculo = buscarVehiculo(idVehiculo);
        if (mision != null && vehiculo != null) {
            asignado = mision.asignarVehiculo(vehiculo);
        }
        return asignado;
    }

    public boolean iniciarMision(String idMision) {
        boolean iniciada = false;
        Mision mision = buscarMision(idMision);
        if (mision != null) {
            iniciada = mision.iniciar();
        }
        return iniciada;
    }

    // Es la distancia total de ese vehículo en la misión, no su odómetro.
    public boolean registrarKilometrajeMision(String idMision, String idVehiculo, double kilometros) {
        boolean registrado = false;
        Mision mision = buscarMision(idMision);
        if (mision != null && idVehiculo != null) {
            registrado = mision.registrarKilometraje(idVehiculo.trim(), kilometros);
        }
        return registrado;
    }

    public boolean completarMision(String idMision) {
        boolean completada = false;
        Mision mision = buscarMision(idMision);
        if (mision != null) {
            completada = mision.completar();
        }
        return completada;
    }

    public ArrayList<Vehiculo> obtenerVehiculosDisponibles() {
        ArrayList<Vehiculo> disponibles = new ArrayList<>();
        for (Vehiculo vehiculo : listaVehiculos) {
            if (vehiculo.getEstadoOperativo() == EstadoOperativo.DISPONIBLE) {
                disponibles.add(vehiculo);
            }
        }
        return disponibles;
    }

    public ArrayList<Vehiculo> obtenerVehiculosConMasDe50Misiones() {
        ArrayList<Vehiculo> resultado = new ArrayList<>();
        for (Vehiculo vehiculo : listaVehiculos) {
            if (vehiculo.contarMisionesCompletadas() > 50) {
                resultado.add(vehiculo);
            }
        }
        return resultado;
    }

    public ArrayList<Vehiculo> getListaVehiculos() {
        return new ArrayList<>(listaVehiculos);
    }

    public ArrayList<Mision> getListaMision() {
        return new ArrayList<>(listaMision);
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getBrigada() {
        return brigada;
    }

    public void setBrigada(String brigada) {
        this.brigada = brigada;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    @Override
    public String toString() {
        return "Batallon{codigo='" + codigo + "', nombre='" + nombre + "', brigada='" + brigada
                + "', distrito='" + distrito + "', numero=" + numero
                + ", cantidadVehiculos=" + listaVehiculos.size()
                + ", cantidadMisiones=" + listaMision.size() + "}";
    }
}
