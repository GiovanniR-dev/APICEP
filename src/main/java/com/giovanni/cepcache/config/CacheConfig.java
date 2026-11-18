// config/CacheConfig.java
package com.giovanni.cepcache.config;

import com.giovanni.cepcache.cache.CacheEmMemoria;
import com.giovanni.cepcache.dto.Endereco;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.Duration;

@Configuration
public class CacheConfig {

    @Bean
    public Clock relogio() {
        return Clock.systemUTC();
    }

    @Bean
    public CacheEmMemoria<String, Endereco> cacheDeCep(
            @Value("${cep.cache.ttl:24h}") Duration ttl,   // lê do properties; padrão 24h
            Clock relogio) {
        return new CacheEmMemoria<>(ttl, relogio);
    }
}