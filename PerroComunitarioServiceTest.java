package com.tarea.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.tarea.mock.entidades.Perro;
import com.tarea.mock.excepciones.PerroNoEncontradoException;
import com.tarea.mock.repositorios.PerroRepository;
import com.tarea.mock.servicios.PerroComunitarioService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class PerroComunitarioServiceTest {

    PerroRepository mockRepository;
    PerroComunitarioService service;

    @BeforeEach
    public void inicializarPrueba() {
        // Mock del repositorio
        mockRepository = Mockito.mock(PerroRepository.class);
        // Servicio a probar
        service = new PerroComunitarioService(mockRepository);
    }

    @Test
    public void deberiaDevolverPerroCuandoElPerroExiste() {
        // Simular el comportamiento del repositorio
        Perro perroMock = new Perro("Fido", 4);
        when(mockRepository.buscarPorNombre("Fido")).thenReturn(perroMock);

        // Llamada al método y verificación
        Perro resultado = service.obtenerPerroPorNombre("Fido");
        assertEquals("Fido", resultado.getNombre());
        assertEquals(4, resultado.getEdad());

        // Verificar que el repositorio se consultó una vez
        verify(mockRepository, times(1)).buscarPorNombre("Fido");
    }

    @Test
    public void deberiaLanzarPerroNoEncontradoExceptionCuandoElPerroNoExiste() {
        // Configurar el mock para lanzar la excepción
        when(mockRepository.buscarPorNombre("Rex"))
            .thenThrow(new PerroNoEncontradoException("El perro no fue encontrado"));

        // Verificar que se lanza la excepción
        PerroNoEncontradoException excepcion = assertThrows(
            PerroNoEncontradoException.class,
            () -> service.obtenerPerroPorNombre("Rex")
        );

        assertEquals("El perro no fue encontrado", excepcion.getMessage());

        // Verificar que el método del repositorio fue llamado una vez
        verify(mockRepository, times(1)).buscarPorNombre("Rex");
    }

    @Test
    public void deberiaLanzarIllegalArgumentExceptionCuandoElNombreEsNull() {
        // Verificar que se lanza la excepción
        IllegalArgumentException excepcion = assertThrows(
            IllegalArgumentException.class,
            () -> service.obtenerPerroPorNombre(null)
        );

        assertEquals("El nombre no puede ser nulo o vacío", excepcion.getMessage());
    }

    @Test
    public void deberiaLanzarIllegalArgumentExceptionCuandoElNombreEsVacio() {
        // Verificar que se lanza la excepción
        IllegalArgumentException excepcion = assertThrows(
            IllegalArgumentException.class,
            () -> service.obtenerPerroPorNombre("")
        );

        assertEquals("El nombre no puede ser nulo o vacío", excepcion.getMessage());
    }

    @Test
    public void deberiaConsultarRepositorioUnaSolaVezCuandoElPerroExiste() {
        // Simular el comportamiento del repositorio
        Perro perroMock = new Perro("Fido", 4);
        when(mockRepository.buscarPorNombre("Fido")).thenReturn(perroMock);

        // Llamada al método
        service.obtenerPerroPorNombre("Fido");

        // Verificar que el repositorio fue consultado una sola vez
        verify(mockRepository, times(1)).buscarPorNombre("Fido");
    }
}
