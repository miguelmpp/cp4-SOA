package br.com.fiap3espa.auto_escola_3espa.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health-check")
public class HealthCheckController {

    @GetMapping
    public String healthCheck() {
        return "Verificação de integridade Auto Escola 3ESPA!";
    }
}