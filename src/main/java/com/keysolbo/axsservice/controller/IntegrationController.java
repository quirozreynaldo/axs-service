package com.keysolbo.axsservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.keysolbo.axsservice.service.IntegrationService;
import com.keysolbo.axsservice.service.SurveyMonkeyService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/integration")
@RestController
public class IntegrationController {
    @Autowired
    private IntegrationService integrationService;
    @Autowired
    private SurveyMonkeyService surveyMonkeyService;
    @GetMapping("/reclamoscomerciales")
    public String procesoReclamosComerciales() {
        try {
            integrationService.procesoInstalacionVenta();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
          // integrationService.procesoReclamosComerciales();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       try {
          //  integrationService.procesoServicoAlClientePresencial();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
           // integrationService.procesoReclamosTecnicos();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
           // integrationService.procesoReclamosTecnicosCc();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
           // integrationService.procesoServicoAlClienteRemotoCc();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
           // integrationService.procesoInstalacion();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        


        return "PROCESSED";
    }
}
