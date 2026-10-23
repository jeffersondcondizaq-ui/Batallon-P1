package co.edu.uniquindio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class VehiculoTest {

    private static final Logger LOG = Logger.getLogger(VehiculoTest.class.getName());

    private Batallon batllon;
    private TransporteTropas transporteTropas;
    private Blindado blindado;
    private Apoyo apoyo;

    @BeforeEach
    void setUp(){
        batllon = new Batallon("01","Agilas", "03","Norte",3);
        transporteTropas = new TransporteTropas("TT-01","Huntter H1",2026,2.000,EstadoOperativo.DISPONIBLE,8);
        blindado = new Blindado("BL-01","H0184",2026,3.000,EstadoOperativo.DISPONIBLE,10);
        apoyo = new Apoyo("AP-01","AP-21",2021,1.000,EstadoOperativo.DISPONIBLE,TipoFuncion.MEDICO);
    }

    @DisplayName("1. Validar la creación de los diferentes tipos de vehículos")
    @Test
    void testCreacionDiferentesTiposVehiculos(){
        LOG.info("Iniciando test creacion de diferentes tipos de vehiculos...");

        assertEquals("TT-01", transporteTropas.getId());
        assertEquals("Huntter H1", transporteTropas.getModelo());
        assertEquals(2026, transporteTropas.getAnioFabricacion());
        assertEquals(2.000, transporteTropas.getKilometraje());
        assertEquals(EstadoOperativo.DISPONIBLE, transporteTropas.getEstadoOperativo());
        assertEquals(8, transporteTropas.getCapacidadSoldados());
        assertTrue(transporteTropas.esValido());

        assertEquals("BL-01", blindado.getId());
        assertEquals("H0184", blindado.getModelo());
        assertEquals(10, blindado.getNivelBlindaje());
        assertTrue(blindado.esValido());

        assertEquals("AP-01", apoyo.getId());
        assertEquals("AP-21", apoyo.getModelo());
        assertEquals(TipoFuncion.MEDICO, apoyo.getTipoFuncion());
        assertTrue(apoyo.esValido());

        LOG.info("Finalizando test creacion de diferentes tipos de vehiculos...");
    }

    @DisplayName("Agregar veyhiculo por ID")
    @Test
    void testAgregarVheiculoCorrectamente(){
        LOG.info("Iniciando prueba agregar vehiculo......");

        boolean resultado = batllon.registrarVehiculo(transporteTropas);

        assertTrue(resultado);
        assertEquals(1,batllon.getListaVehiculos().size());
        assertTrue(batllon.getListaVehiculos().contains(transporteTropas));
        assertSame(transporteTropas, batllon.buscarVehiculo("TT-01"));

        LOG.info(batllon.toString());
        LOG.info("Finalizacion de prueba de ingreso de vehiculo");
    }


    @DisplayName("Agregar varios vehiculosa")
    @Test
    void testAgregarVariosVehiculos(){
        LOG.info("Iniciando prueba agregar vehiculos.....");

        assertTrue(batllon.registrarVehiculo(transporteTropas));
        assertTrue(batllon.registrarVehiculo(blindado));
        assertTrue(batllon.registrarVehiculo(apoyo));

        assertEquals(3,batllon.getListaVehiculos().size());
        LOG.info("Finalizacion de prueba de ingreso de vehiculos");
    }


    @DisplayName("No agregar vehiculos con ID  repetidos")
    @Test
    void testAgregarVheiculoConIdRepetido(){
        LOG.info("Iniciando prueba agregar vehiculo ID repetido......");

        assertTrue(batllon.registrarVehiculo(transporteTropas));

        TransporteTropas repetido = new TransporteTropas("TT-01","Huntter H1",2026,2.000,EstadoOperativo.DISPONIBLE,8);

        boolean resultado = batllon.registrarVehiculo(transporteTropas);

        assertFalse(resultado);
        assertEquals(1,batllon.getListaVehiculos().size());
        assertSame(transporteTropas,batllon.buscarVehiculo("TT-01"));
        assertFalse(batllon.getListaVehiculos().contains(repetido));

        LOG.info("Finalizacion de prueba de ingreso de vehiculo con ID repetido");
    }

    @DisplayName("4. Buscar vehículos existentes e inexistentes")
    @Test
    void testBuscarVehiculosExistentes(){
        LOG.info("Iniciando test buscar vehiculo existente...");
        batllon.registrarVehiculo(transporteTropas);
        batllon.registrarVehiculo(blindado);

        Vehiculo encontrado = batllon.buscarVehiculo("BL-01");

        assertNotNull(encontrado);
        assertSame(blindado,encontrado);
        assertEquals("H0184",encontrado.getModelo());

        LOG.info("Finalizando test buscar vehiculo existente...");
    }
    @DisplayName("4.1 Buscar vehiculo inexistentes")
    @Test
    void testBuscarVehiculosInexistentes(){
        LOG.info("Iniciando test buscar vehiculo inexistente...");

        batllon.registrarVehiculo(apoyo);
        Vehiculo encontrado = batllon.buscarVehiculo("no existe");

        assertNull(encontrado);

        LOG.info("Iniciando test buscar vehiculo inexistente...");
    }

    @DisplayName("5. Eliminar un vehículo registrado")
    @Test
    void testEliminarVehiculoRegistrado(){
        LOG.info("Iniciando test eliminar vehiculo registrado...");

        batllon.registrarVehiculo(apoyo);
        batllon.registrarVehiculo(transporteTropas);

        boolean eliminado = batllon.eliminarVehiculo("AP-01");

        assertTrue(eliminado);
        assertNull(batllon.buscarVehiculo("AP-01"));
        assertFalse(batllon.getListaVehiculos().contains(apoyo));
        assertEquals(1, batllon.getListaVehiculos().size());

        LOG.info("Finalizando test eliminar vehiculo registrado...");
    }

    @DisplayName("5.1 No eliminar un vehículo con mision ")
    @Test
    void testNoEliminarVehiculoMisionRegistrada(){
        LOG.info("Iniciando test de no eliminar vehiculo con mision registrada...");

        batllon.registrarVehiculo(apoyo);
        Mision mision = new Mision("M-01",LocalDate.of(2026,10,22),"Norte");
        mision.agregarPersonal("Juanita Perez");
        batllon.registrarMision(mision);
        batllon.asignarVehiculoMision("M-01","AP-01");

        boolean eliminado = batllon.eliminarVehiculo("AP-01");

        assertFalse(eliminado);
        assertSame(apoyo,batllon.buscarVehiculo("AP-01"));

        LOG.info("Finalizando test de no eliminar vehiculo con mision registrada...");
    }

    @DisplayName("6. Actualizar correctamente la información de un vehículo")
    @Test
    void testActualizarKilometrajeEstadoOperativo(){
        LOG.info("Iniciando test actualizar kilometraje y estado operativo...");

        batllon.registrarVehiculo(apoyo);
        Vehiculo vehiculo = batllon.buscarVehiculo("AP-01");

        assertTrue(vehiculo.setKilometraje(150000.0));
        assertTrue(vehiculo.setEstadoOperativo(EstadoOperativo.EN_MANTENIMIENTO));

        Vehiculo encontrado = batllon.buscarVehiculo("AP-01");
        assertEquals(150000.0,encontrado.getKilometraje());
        assertEquals(EstadoOperativo.EN_MANTENIMIENTO,encontrado.getEstadoOperativo());

        LOG.info("Iniciando test actualizar kilometraje y estado operativo...");
    }

}