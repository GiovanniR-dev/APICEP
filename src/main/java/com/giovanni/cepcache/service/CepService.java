package com.giovanni.cepcache.service;

import com.giovanni.cepcache.cache.CacheEmMemoria;
import com.giovanni.cepcache.client.ViaCepClient;
import com.giovanni.cepcache.client.ViaCepResponse;
import com.giovanni.cepcache.dto.Endereco;
import com.giovanni.cepcache.exception.CepInvalidoException;
import com.giovanni.cepcache.exception.CepNaoEncontradoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CepService {

    private static final Logger log = LoggerFactory.getLogger(CepService.class);
    private final ViaCepClient viaCepClient;
    private final CacheEmMemoria<String, Endereco> cache;

    public CepService(ViaCepClient viaCepClient, CacheEmMemoria<String, Endereco> cache) {
        this.viaCepClient = viaCepClient;
        this.cache = cache;
    }

    public Endereco buscar(String cepBruto) {
        String cep = normalizar(cepBruto);
        long inicio = System.nanoTime();

        // 1. Tenta o cache primeiro (cache-aside)
        Optional<Endereco> emCache = cache.buscar(cep);
        if (emCache.isPresent()) {
            long us = (System.nanoTime() - inicio) / 1_000;
            log.info("HIT  {} em {} us (cache)", cep, us);
            return emCache.get();
        }

        // 2. Miss: vai na fonte
        ViaCepResponse resposta = viaCepClient.buscar(cep);
        if (resposta == null || Boolean.TRUE.equals(resposta.erro())) {
            throw new CepNaoEncontradoException(cep);
        }

        // 3. Guarda para a próxima vez
        Endereco endereco = Endereco.de(resposta);
        cache.guardar(cep, endereco);

        long ms = (System.nanoTime() - inicio) / 1_000_000;
        log.info("MISS {} em {} ms (ViaCEP) | itens no cache: {}", cep, ms, cache.tamanho());
        return endereco;
    }

    // "01001-000", " 01001000 " e "01.001-000" viram a MESMA chave: "01001000"
    static String normalizar(String cep) {
        if (cep == null) throw new CepInvalidoException("null");
        String digitos = cep.replaceAll("\\D", "");   // \D = tudo que NÃO é dígito
        if (digitos.length() != 8) throw new CepInvalidoException(cep);
        return digitos;
    }
}
