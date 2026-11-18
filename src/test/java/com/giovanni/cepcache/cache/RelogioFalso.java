// RelogioFalso.java  →  um Clock que só anda quando o teste mandar
package com.giovanni.cepcache.cache;

import java.time.*;

class RelogioFalso extends Clock {

    private Instant agora = Instant.parse("2026-01-01T00:00:00Z");

    void avancar(Duration tempo) {
        agora = agora.plus(tempo);
    }

    @Override public ZoneId getZone() { return ZoneOffset.UTC; }
    @Override public Clock withZone(ZoneId zone) { return this; }
    @Override public Instant instant() { return agora; }
}