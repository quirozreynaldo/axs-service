package com.keysolbo.axsservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.keysolbo.axsservice.service.IntegrationService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/integration")
@RestController
public class IntegrationController {
    @Autowired
    private IntegrationService integrationService;

    @GetMapping("/procesoInstalacionVenta")
    public String procesoInstalacionVenta() {
        try {
            integrationService.procesoInstalacionVenta();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "procesoInstalacionVenta DONE";
    }

    @GetMapping("/procesoReclamosComerciales")
    public String procesoReclamosComerciales() {
        try {
            integrationService.procesoReclamosComerciales();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "procesoReclamosComerciales DONE";
    }

    @GetMapping("/procesoServicoAlClientePresencial")
    public String procesoServicoAlClientePresencial() {
        try {
            integrationService.procesoServicoAlClientePresencial();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "procesoServicoAlClientePresencial DONE";
    }

    @GetMapping("/procesoReclamosTecnicos")
    public String procesoReclamosTecnicos() {
        try {
            integrationService.procesoReclamosTecnicos();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "procesoReclamosTecnicos DONE";
    }

    @GetMapping("/procesoReclamosTecnicosCc")
    public String procesoReclamosTecnicosCc() {
        try {
            integrationService.procesoReclamosTecnicosCc();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "procesoReclamosTecnicosCc DONE";
    }

    @GetMapping("/procesoServicoAlClienteRemotoCc")
    public String procesoServicoAlClienteRemotoCc() {
        try {
             integrationService.procesoServicoAlClienteRemotoCc();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "procesoServicoAlClienteRemotoCc DONE";
    }

}
