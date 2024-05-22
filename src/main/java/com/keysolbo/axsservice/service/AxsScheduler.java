package com.keysolbo.axsservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AxsScheduler {
    @Value("${axs.service.croneveryhour}")
    private String cronEveryHour;
    @Value("${axs.service.croneveryday}")
    private String cronEveryDay;
    @Autowired
    private IntegrationService integrationService;

    // Cron: Segundo,Minuto,Hora,Dia del Mes, Mes, Dia de la Semana, Anio (primer
    // dia del mes)
    // @Scheduled(cron = "0 41 16 * * ?") Current day at this hour
    // vapismsgateway.blacklist.cron=0 0 1 ? * SAT Every Saturday 01:00:00
    @Scheduled(cron = "${axs.service.croneveryhour}")
    public void scheduleHouryTask() {
        log.info("El cron se esta ejecutando cada 4 horas");
        try {
            integrationService.procesoReclamosComerciales();
        } catch (Exception ex) {
            log.error(" integrationService.procesoReclamosComerciales(): {}", ex.getMessage());
        }
        waitFor(30);
        try {
            integrationService.procesoServicoAlClientePresencial();
        } catch (Exception ex) {
            log.error(" integrationService.procesoServicoAlClientePresencial(): {}", ex.getMessage());
        }
        try {
            waitFor(30);
            integrationService.executeReclamosTecnicos();
        } catch (Exception ex) {
            log.error(" integrationService.executeReclamosTecnicos(): {}", ex.getMessage());
        }
        try {
            waitFor(30);
            integrationService.executeReclamosTecnicosCc();
        } catch (Exception ex) {
            log.error(" integrationService.executeReclamosTecnicosCc(): {}", ex.getMessage());
        }
        try {
            waitFor(30);
            integrationService.procesoServicoAlClienteRemotoCc();
        } catch (Exception ex) {
            log.error(" integrationService.procesoServicoAlClienteRemotoCc(): {}", ex.getMessage());
        }

    }

    @Scheduled(cron = "${axs.service.croneveryday}")
    public void scheduleDayTask() {
        log.info("El cron se esta ejecutando a las 04:00");
        try {
            //integrationService.procesoInstalacion();
            integrationService.procesoInstalacionVenta();
        } catch (Exception ex) {
            log.error(" integrationService.procesoInstalacionVenta(): {}", ex.getMessage());
        }
    }

    @PostConstruct
    public void init() {
        log.info("+++++++++++CRON AXS SERVICE INICIO+++++++++++++");
    }

    public void setCronEveryDay(String cronEveryDay) {
        this.cronEveryDay = cronEveryDay;
    }

    public void setCronEveryHour(String cronEveryHour) {
        this.cronEveryHour = cronEveryHour;
    }

    private void waitFor(int seconds) {
        try {
            Thread.sleep(seconds * 1000); // Convertimos segundos a milisegundos
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
