package co.edu.uniquindio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class MisionTest {

    private static final Logger LOG = Logger.getLogger(MisionTest.class.getName());

    private Batallon batallon;
    private TransporteTropas transporteTropas;
    private Blindado blindado;
    private Apoyo apoyo;

    @BeforeEach
    void setUp(){
        batallon = new Batallon("01","Aguilas","03","Norte",3);
        transporteTropas = new TransporteTropas("TT-01","Huntter H1",2026,2.000,EstadoOperativo.DISPONIBLE,8);
        blindado = new Blindado("BL-01","H0184",2026,3.000,EstadoOperativo.DISPONIBLE,10);
        apoyo = new Apoyo("AP-01","AP-21",2021,1.000,EstadoOperativo.DISPONIBLE,TipoFuncion.MEDICO);

        batallon.registrarVehiculo(transporteTropas);
        batallon.registrarVehiculo(blindado);
        batallon.registrarVehiculo(apoyo);
    }

    // Completa una mision de principio a fin para un vehiculo ya registrado en el batallon.
    private void completarMisionParaVehiculo(String idMision, Vehiculo vehiculo){
        Mision mision = new Mision(idMision, LocalDate.of(2026,10,22), "Zona Operativa");
        mision.agregarPersonal("Soldado Base");
        batallon.registrarMision(mision);
        batallon.asignarVehiculoMision(idMision, vehiculo.getId());
        batallon.iniciarMision(idMision);
        batallon.registrarKilometrajeMision(idMision, vehiculo.getId(), 10.0);
        batallon.completarMision(idMision);
    }

    @DisplayName("7. Registrar una mision con personal y vehiculos asignados")
    @Test
    void testRegistrarMisionConPersonalYVehiculosAsignados(){
        LOG.info("Iniciando test registrar mision con personal y vehiculos...");

        Mision mision = new Mision("M-01", LocalDate.of(2026,10,22), "Zona Norte");
        assertTrue(mision.agregarPersonal("Juanita Perez"));
        assertTrue(mision.agregarPersonal("Carlos Ramirez"));

        boolean registrada = batallon.registrarMision(mision);
        assertTrue(registrada);

        assertTrue(batallon.asignarVehiculoMision("M-01","TT-01"));
        assertTrue(batallon.asignarVehiculoMision("M-01","BL-01"));

        Mision encontrada = batallon.buscarMision("M-01");

        assertNotNull(encontrada);
        assertEquals(2, encontrada.getPersonalAsignado().size());
        assertTrue(encontrada.getPersonalAsignado().contains("Juanita Perez"));
        assertTrue(encontrada.getPersonalAsignado().contains("Carlos Ramirez"));

        ArrayList<DetalleMision> detalles = encontrada.getListaDetallesMision();
        assertEquals(2, detalles.size());
        assertTrue(detalles.stream().anyMatch(detalle -> detalle.getVehiculo() == transporteTropas));
        assertTrue(detalles.stream().anyMatch(detalle -> detalle.getVehiculo() == blindado));

        LOG.info("Finalizando test registrar mision con personal y vehiculos...");
    }

    @DisplayName("8. Contar unicamente las misiones completadas de un vehiculo")
    @Test
    void testContarSoloMisionesCompletadas(){
        LOG.info("Iniciando test contar solo misiones completadas...");

        completarMisionParaVehiculo("M-COMPLETADA", apoyo);

        Mision pendiente = new Mision("M-PENDIENTE", LocalDate.of(2026,10,22), "Zona Sur");
        pendiente.agregarPersonal("Soldado Uno");
        batallon.registrarMision(pendiente);
        batallon.asignarVehiculoMision("M-PENDIENTE","AP-01");

        Mision enCurso = new Mision("M-EN-CURSO", LocalDate.of(2026,10,22), "Zona Este");
        enCurso.agregarPersonal("Soldado Dos");
        batallon.registrarMision(enCurso);
        batallon.asignarVehiculoMision("M-EN-CURSO","AP-01");
        batallon.iniciarMision("M-EN-CURSO");

        assertEquals(EstadoMision.PLANIFICADA, batallon.buscarMision("M-PENDIENTE").getEstado());
        assertEquals(EstadoMision.EN_CURSO, batallon.buscarMision("M-EN-CURSO").getEstado());
        assertEquals(1, apoyo.contarMisionesCompletadas());

        LOG.info("Finalizando test contar solo misiones completadas...");
    }

    @DisplayName("9. Vehiculo con exactamente 50 misiones completadas no supera el limite")
    @Test
    void testVehiculoCon50MisionesCompletadasNoApareceEnListado(){
        LOG.info("Iniciando test vehiculo con 50 misiones completadas...");

        for (int i = 1; i <= 50; i++) {
            completarMisionParaVehiculo("M-50-" + i, blindado);
        }

        assertEquals(50, blindado.contarMisionesCompletadas());

        ArrayList<Vehiculo> conMasDe50 = batallon.obtenerVehiculosConMasDe50Misiones();

        assertFalse(conMasDe50.contains(blindado));
        assertTrue(conMasDe50.isEmpty());

        LOG.info("Finalizando test vehiculo con 50 misiones completadas...");
    }

    @DisplayName("10. Obtener vehiculos con mas de 50 misiones completadas")
    @Test
    void testObtenerVehiculosConMasDe50MisionesCompletadas(){
        LOG.info("Iniciando test vehiculo con 51 misiones completadas...");

        for (int i = 1; i <= 51; i++) {
            completarMisionParaVehiculo("M-51-" + i, blindado);
        }

        assertEquals(51, blindado.contarMisionesCompletadas());

        ArrayList<Vehiculo> conMasDe50 = batallon.obtenerVehiculosConMasDe50Misiones();

        assertTrue(conMasDe50.contains(blindado));
        assertEquals(1, conMasDe50.size());

        LOG.info("Finalizando test vehiculo con 51 misiones completadas...");
    }

}
