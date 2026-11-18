package com.giovanni.cepcache.dto;

import com.giovanni.cepcache.client.ViaCepResponse;

public record Endereco(String cep, String logradouro, String bairro, String cidade, String uf){



    public static Endereco de(ViaCepResponse r){
        return new Endereco(r.cep(), r.logradouro(), r.bairro(), r.localidade(), r.uf());
    }
}
