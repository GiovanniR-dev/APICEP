// CacheEmMemoriaTest.java
package com.giovanni.cepcache.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class CacheEmMemoriaTest {

    private RelogioFalso relogio;
    private CacheEmMemoria<String, String> cache;

    @BeforeEach   // roda antes de CADA teste → cada teste começa do zero
    void preparar() {
        relogio = new RelogioFalso();
        cache = new CacheEmMemoria<>(Duration.ofHours(24), relogio);
    }

    @Test
    void chaveInexistenteRetornaVazio() {
        assertTrue(cache.buscar("00000000").isEmpty());
    }

    @Test
    void retornaValorGuardado() {
        cache.guardar("01001000", "Praça da Sé");
        assertEquals("Praça da Sé", cache.buscar("01001000").orElseThrow());
    }

    @Test
    void aindaValidoUmSegundoAntesDoTtl() {
        cache.guardar("01001000", "Praça da Sé");
        relogio.avancar(Duration.ofHours(24).minusSeconds(1));
        assertTrue(cache.buscar("01001000").isPresent());
    }

    @Test
    void expiraExatamenteNoTtlERemoveDoMapa() {
        cache.guardar("01001000", "Praça da Sé");
        relogio.avancar(Duration.ofHours(24));
        assertTrue(cache.buscar("01001000").isEmpty());
        assertEquals(0, cache.tamanho());
    }

    @Test
    void rejeitaTtlZero() {
        assertThrows(IllegalArgumentException.class,
                () -> new CacheEmMemoria<>(Duration.ZERO, relogio));
    }
}
