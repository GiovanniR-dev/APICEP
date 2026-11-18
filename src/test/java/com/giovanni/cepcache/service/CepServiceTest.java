package com.giovanni.cepcache.service;

import com.giovanni.cepcache.exception.CepInvalidoException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CepServiceTest {

    @Test
    void deveRemoverHifenPontoEEspacos() {
        assertEquals("01001000", CepService.normalizar(" 01.001-000 "));
    }

    @Test
    void deveRejeitarCepComMenosDe8Digitos() {
        assertThrows(CepInvalidoException.class, () -> CepService.normalizar("123"));
    }

    @Test
    void deveRejeitarNull() {
        assertThrows(CepInvalidoException.class, () -> CepService.normalizar(null));
    }
}