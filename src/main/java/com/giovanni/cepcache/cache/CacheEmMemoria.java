// cache/CacheEmMemoria.java
package com.giovanni.cepcache.cache;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class CacheEmMemoria<K, V> {

    // Record interno: o valor + quando ele vence
    private record Entrada<T>(T valor, Instant expiraEm) {
        boolean expirouEm(Instant agora) {
            return !agora.isBefore(expiraEm);   // agora >= expiraEm
        }
    }

    private final Map<K, Entrada<V>> mapa = new ConcurrentHashMap<>();
    private final Duration ttl;
    private final Clock relogio;

    public CacheEmMemoria(Duration ttl, Clock relogio) {
        if (ttl == null || ttl.isNegative() || ttl.isZero()) {
            throw new IllegalArgumentException("TTL deve ser positivo");
        }
        this.ttl = ttl;
        this.relogio = relogio;
    }

    public Optional<V> buscar(K chave) {
        Entrada<V> entrada = mapa.get(chave);
        if (entrada == null) {
            return Optional.empty();
        }
        if (entrada.expirouEm(relogio.instant())) {
            // Remove SÓ se ainda for esta entrada (outra thread pode ter gravado uma nova)
            mapa.remove(chave, entrada);
            return Optional.empty();
        }
        return Optional.of(entrada.valor());
    }

    public void guardar(K chave, V valor) {
        mapa.put(chave, new Entrada<>(valor, relogio.instant().plus(ttl)));
    }

    public int tamanho() {
        return mapa.size();
    }
}