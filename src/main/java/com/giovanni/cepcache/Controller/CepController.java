// controller/CepController.java
package com.giovanni.cepcache.controller;

import com.giovanni.cepcache.dto.Endereco;
import com.giovanni.cepcache.service.CepService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cep")
public class CepController {

    private final CepService cepService;

    public CepController(CepService cepService) {
        this.cepService = cepService;
    }

    @GetMapping("/{cep}")
    public Endereco buscar(@PathVariable String cep) {
        return cepService.buscar(cep);
    }
}