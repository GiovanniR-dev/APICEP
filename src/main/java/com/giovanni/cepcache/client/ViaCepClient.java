package com.giovanni.cepcache.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component

public class ViaCepClient {

    private final RestClient restClient;

    public ViaCepClient(RestClient.Builder builder){
        this.restClient = builder.baseUrl("https://viacep.com.br/ws").build();
    }

    public ViaCepResponse buscar(String cep){
        return restClient.get()
                .uri("/{cep}/json/", cep)
                .retrieve()
                .body(ViaCepResponse.class);
    }


}
