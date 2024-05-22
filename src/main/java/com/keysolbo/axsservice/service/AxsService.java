package com.keysolbo.axsservice.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keysolbo.axsservice.Util.ConstantDb;
import com.keysolbo.axsservice.Util.Utils;
import com.keysolbo.axsservice.database.ManageLog;
import com.keysolbo.axsservice.model.Contrato;
import com.keysolbo.axsservice.model.Ticket;
import com.keysolbo.axsservice.model.Token;
import com.keysolbo.axsservice.model.survey.TicketServicoCliente;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AxsService {
    @Value("${axs.service.instalacion}")
    private String instalacionUrl;
    @Value("${axs.service.fgetexperienciatiendadex}")
    private String experienciaTiendaDexUrl;
    @Value("${axs.service.fgetreclamoscomercialesdex}")
    private String reclamosComercialesDexUrl;
    @Value("${axs.service.fgetreclamostecnicosdex}")
    private String reclamosTecnicosDexUrl;
    @Value("${axs.service.fgetreclamostecnicosccdex}")
    private String reclamosTecnicosCcDexUrl;
    @Value("${axs.service.fgetexperienciacalldex}")
    private String experienceCallDexUrl;
    @Value("${axs.service.preventa}")
    private String preventaUrl;

    @Autowired
    private WebClient webClient;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private ManageLog manageLog;

    private List<Contrato> instalacion(String token) throws Exception {
        List<Contrato> contractList = new ArrayList<>();
        try {
            contractList = webClient
                    .get()
                    .uri(instalacionUrl)
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .retrieve()
                    .bodyToFlux(Contrato.class)
                    .collectList()
                    .block();
            log.info("=================================================================");
            log.info("Response instalacion/venta: " + instalacionUrl + " : {}", contractList);
            log.info("=================================================================");
            if (contractList.size() == 0) {
                throw new Exception("contractList esta vacia");
            } else if (contractList.get(0).getContrato() == null
                    || contractList.get(0).getContrato().trim().length() == 0) {
                throw new Exception("contractList esta vacia");
            }
        } catch (WebClientResponseException e) {
            log.error("Error al consumir el servicio instalacion venta : {}", e.getRawStatusCode());
            log.error("Respuesta del servidor: {}", e.getResponseBodyAsString());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al consumir el servicio: {}", e.getMessage());
            throw e;
        }
        return contractList;
    }

    public List<Contrato> instalacion() throws Exception {
        Token token = tokenService.getToken();
        List<Contrato> contractList = instalacion(token.getAccessToken());
        for (Contrato contrato : contractList) {
            log.info("contrato: {}", contrato.toString());
            String emailOriginal = contrato.getContactoEmail() != null ? contrato.getContactoEmail(): contrato.getEmailAlternativoContacto();
            String alfaNum = contrato.getContrato() != null ? contrato.getContrato() : "NA";
            String emailAlias = Utils.emailAlias(emailOriginal, alfaNum);
            contrato.setContactoEmail(emailAlias);
            contrato.setEmailOriginal(emailOriginal);
            manageLog.recordInstallationLog(Utils.fillForInstallLog(contrato, emailOriginal, ConstantDb.CONFIG_INSTALACION));
        }
        return contractList;
    }

    public List<Contrato> instalacion_dummy() throws Exception {
        ClassPathResource resource = new ClassPathResource("instalacion.json");
        ObjectMapper objectMapper = new ObjectMapper();
        List<Contrato> ticketList = null;
        try {
            ticketList = objectMapper.readValue(resource.getInputStream(), new TypeReference<List<Contrato>>() {
            });
            for (Contrato contrato : ticketList) {
                // log.info("contrato: {}", contrato.toString());
                String emailOriginal = contrato.getContactoEmail() != null ? contrato.getContactoEmail(): contrato.getEmailAlternativoContacto();
                String alfaNum = contrato.getContrato() != null ? contrato.getContrato() : "NA";
                String emailAlias = Utils.emailAlias(emailOriginal, alfaNum);
                contrato.setContactoEmail(emailAlias);
                manageLog.recordInstallationLog(Utils.fillForInstallLog(contrato, emailOriginal, ConstantDb.CONFIG_INSTALACION));
            }
        } catch (StreamReadException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DatabindException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        log.info("=================================================================");
        log.info("venta/instalacion: " + instalacionUrl + " : {}", ticketList);
        log.info("=================================================================");
        return ticketList;
    }

    public List<Contrato> instalacion_venta() throws Exception {
        Token token = tokenService.getToken();
        List<Contrato> contractList = instalacion(token.getAccessToken());
        for (Contrato contrato : contractList) {
            log.info("contrato: {}", contrato.toString());
            String emailOriginal = contrato.getContactoEmail() != null ? contrato.getContactoEmail(): contrato.getEmailAlternativoContacto();
            String alfaNum = contrato.getContrato() != null ? contrato.getContrato() : "NA";
            String emailAlias = Utils.emailAlias(emailOriginal, alfaNum);
            contrato.setContactoEmail(emailAlias);
            contrato.setEmailOriginal(emailOriginal);
            manageLog.recordInstallationLog(Utils.fillForInstallLog(contrato, emailOriginal, ConstantDb.CONFIG_INSTALACION_VENTA));
        }
        return contractList;
    }
    public List<Contrato> instalacion_venta_dummy() throws Exception {
        ClassPathResource resource = new ClassPathResource("instalacion.json");
        ObjectMapper objectMapper = new ObjectMapper();
        List<Contrato> ticketList = null;
        try {
            ticketList = objectMapper.readValue(resource.getInputStream(), new TypeReference<List<Contrato>>() {
            });
            for (Contrato contrato : ticketList) {
                // log.info("contrato: {}", contrato.toString());
                String emailOriginal = contrato.getContactoEmail() != null ? contrato.getContactoEmail(): contrato.getEmailAlternativoContacto();
                String alfaNum = contrato.getContrato() != null ? contrato.getContrato() : "NA";
                String emailAlias = Utils.emailAlias(emailOriginal, alfaNum);
                contrato.setContactoEmail(emailAlias);
                contrato.setEmailOriginal(emailOriginal);
                manageLog.recordInstallationLog(Utils.fillForInstallLog(contrato, emailOriginal, ConstantDb.CONFIG_INSTALACION_VENTA));
            }
        } catch (StreamReadException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DatabindException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        log.info("=================================================================");
        log.info("instalcion-venta: " + instalacionUrl + " : {}", ticketList);
        log.info("=================================================================");
        return ticketList;
    }

    private List<TicketServicoCliente> fGetExperienciaTiendaDex(String token) throws Exception {
        List<TicketServicoCliente> ticketList = new ArrayList<>();

        try {
            ticketList = webClient
                    .get()
                    .uri(experienciaTiendaDexUrl)
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .retrieve()
                    .bodyToFlux(TicketServicoCliente.class)
                    .collectList()
                    .block();
            log.info("=================================================================");
            log.info("fGetExperienciaTiendaDex: " + experienciaTiendaDexUrl + " {}", ticketList);
            log.info("=================================================================");
            if (ticketList.size() == 0) {
                throw new Exception("ticketList esta vacia");
            } else if (ticketList.get(0).getIdTicket() == null
                    || ticketList.get(0).getIdTicket().trim().length() == 0) {
                throw new Exception("ticketList esta vacia");
            }
        } catch (WebClientResponseException e) {
            log.error("Error al consumir fGetExperienciaTiendaDex: {}", e.getRawStatusCode());
            log.error("Respuesta del servidor: {}", e.getResponseBodyAsString());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al consumir el servicio {}", e.getMessage());
            throw e;
        }
        return ticketList;
    }

    public List<TicketServicoCliente> fGetExperienciaTiendaDex() throws Exception {
        Token token = tokenService.getToken();
        List<TicketServicoCliente> ticketList = fGetExperienciaTiendaDex(token.getAccessToken());
        for (TicketServicoCliente ticket : ticketList) {
            log.info("ticket: {}", ticket.toString());
            String emailOriginal = ticket.getEmail() != null ? ticket.getEmail() : ticket.getEmailAlternativo();
            String alfaNum = ticket.getIdTicket() != null ? ticket.getIdTicket() : "NA";
            String emailAlias = Utils.emailAlias(emailOriginal, alfaNum);
            ticket.setEmail(emailAlias);
            ticket.setEmailOriginal(emailOriginal);
            manageLog.recordServiceComplainLog(Utils.fillExperiencia(ticket, ConstantDb.CONFIG_SRCIO_AL_CLI_PRESEN_REGIO));
        }
        return ticketList;
    }

    public List<TicketServicoCliente> fGetExperienciaTiendaDex_dummy() {
        ClassPathResource resource = new ClassPathResource("experiencia_tienda.json");
        ObjectMapper objectMapper = new ObjectMapper();
        List<TicketServicoCliente> ticketList = null;
        try {
            ticketList = objectMapper.readValue(resource.getInputStream(),
                    new TypeReference<List<TicketServicoCliente>>() {
                    });
            for (TicketServicoCliente ticket : ticketList) {
                // log.info("ticket: {}", ticket.toString());
                String emailOriginal = ticket.getEmail() != null ? ticket.getEmail() : ticket.getEmailAlternativo();
                String alfaNum = ticket.getIdTicket() != null ? ticket.getIdTicket() : "NA";
                String emailAlias = Utils.emailAlias(emailOriginal, alfaNum);
                ticket.setEmail(emailAlias);
                manageLog.recordServiceComplainLog(Utils.fillExperiencia(ticket, ConstantDb.CONFIG_SRCIO_AL_CLI_PRESEN_REGIO));
            }
        } catch (StreamReadException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DatabindException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        log.info("=================================================================");
        log.info("fGetExperienciaTiendaDex: " + experienciaTiendaDexUrl + " {}", ticketList);
        log.info("=================================================================");
        return ticketList;
    }

    private List<Ticket> fGetReclamosComercialesDex(String token) throws Exception {
        List<Ticket> ticketList = new ArrayList<>();

        try {
            ticketList = webClient
                    .get()
                    .uri(reclamosComercialesDexUrl)
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .retrieve()
                    .bodyToFlux(Ticket.class)
                    .collectList()
                    .block();
            log.info("=================================================================");
            log.info("fGetReclamosComercialesDex: " + reclamosComercialesDexUrl + " {}", ticketList);
            log.info("=================================================================");
            if (ticketList.size() == 0) {
                throw new Exception("ticketList esta vacia");
            } else if (ticketList.get(0).getIdTicket() == null
                    || ticketList.get(0).getIdTicket().trim().length() == 0) {
                throw new Exception("ticketList esta vacia");
            }
        } catch (WebClientResponseException e) {
            log.error("Error al consumir el servicio fGetReclamosComercialesDex: {}", e.getRawStatusCode());
            log.error("Respuesta del servidor: {}", e.getResponseBodyAsString());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al consumir el servicio {}", e.getMessage());
            throw e;
        }
        return ticketList;
    }

    public List<Ticket> fGetReclamosComercialesDex() throws Exception {
        Token token = tokenService.getToken();
        List<Ticket> ticketList = fGetReclamosComercialesDex(token.getAccessToken());
        for (Ticket ticket : ticketList) {
            log.info("ticket: {}", ticket.toString());
            String emailOriginal = ticket.getEmail() != null ? ticket.getEmail() : ticket.getEmailAlternativo();
            String alfaNum = ticket.getIdTicket() != null ? ticket.getIdTicket() : "NA";
            String emailAlias = Utils.emailAlias(emailOriginal, alfaNum);
            ticket.setEmail(emailAlias);
            ticket.setEmailOriginal(emailOriginal);
            manageLog.recordServiceComplainLog(Utils.fillForReclamos(ticket, ConstantDb.CONFIG_RECLAMO_COMERCIALES));
        }
        return ticketList;
    }

    public List<Ticket> fGetReclamosComercialesDex_dummy() {
        ClassPathResource resource = new ClassPathResource("reclamos_comerciales.json");
        ObjectMapper objectMapper = new ObjectMapper();
        List<Ticket> ticketList = null;
        try {
            ticketList = objectMapper.readValue(resource.getInputStream(), new TypeReference<List<Ticket>>() {
            });
            for (Ticket ticket : ticketList) {
                // log.info("ticket: {}", ticket.toString());
                String emailOriginal = ticket.getEmail() != null ? ticket.getEmail() : ticket.getEmailAlternativo();
                String alfaNum = ticket.getIdTicket() != null ? ticket.getIdTicket() : "NA";
                String emailAlias = Utils.emailAlias(emailOriginal, alfaNum);
                ticket.setEmail(emailAlias);
                manageLog
                        .recordServiceComplainLog(Utils.fillForReclamos(ticket, ConstantDb.CONFIG_RECLAMO_COMERCIALES));
            }
        } catch (StreamReadException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DatabindException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        log.info("=================================================================");
        log.info("fGetReclamosComercialesDex: " + reclamosComercialesDexUrl + " {}", ticketList);
        log.info("=================================================================");
        return ticketList;
    }

    private List<Ticket> fGetReclamosTecnicosDex(String token) throws Exception {
        List<Ticket> ticketList = new ArrayList<>();

        try {
            ticketList = webClient
                    .get()
                    .uri(reclamosTecnicosDexUrl)
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .retrieve()
                    .bodyToFlux(Ticket.class)
                    .collectList()
                    .block();
            log.info("=================================================================");
            log.info("fGetReclamosTecnicosDex: " + reclamosTecnicosDexUrl + " {}", ticketList);
            log.info("=================================================================");
            if (ticketList.size() == 0) {
                throw new Exception("ticketList esta vacia");
            } else if (ticketList.get(0).getIdTicket() == null
                    || ticketList.get(0).getIdTicket().trim().length() == 0) {
                throw new Exception("ticketList esta vacia");
            }
        } catch (WebClientResponseException e) {
            log.error("Error al consumir el servicio fGetReclamosTecnicosDex: {}", e.getRawStatusCode());
            log.error("Respuesta del servidor: {}", e.getResponseBodyAsString());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al consumir el servicio {}", e.getMessage());
            throw e;
        }
        return ticketList;
    }

    public List<Ticket> fGetReclamosTecnicosDex() throws Exception {

        Token token = tokenService.getToken();
        List<Ticket> ticketList = fGetReclamosTecnicosDex(token.getAccessToken());
        for (Ticket ticket : ticketList) {
            log.info("ticket: {}", ticket.toString());
            String emailOriginal = ticket.getEmail() != null ? ticket.getEmail() : ticket.getEmailAlternativo();
            String alfaNum = ticket.getIdTicket() != null ? ticket.getIdTicket() : "NA";
            String emailAlias = Utils.emailAlias(emailOriginal, alfaNum);
            ticket.setEmail(emailAlias);
            ticket.setEmailOriginal(emailOriginal);
            manageLog.recordServiceComplainLog(Utils.fillForReclamos(ticket, ConstantDb.CONFIG_RECLAMOS_TECNICOS));
        }
        return ticketList;
    }

    public List<Ticket> fGetReclamosTecnicosDex_dummy() {
        ClassPathResource resource = new ClassPathResource("reclamos_tecnicos.json");
        ObjectMapper objectMapper = new ObjectMapper();
        List<Ticket> ticketList = null;
        try {
            ticketList = objectMapper.readValue(resource.getInputStream(), new TypeReference<List<Ticket>>() {
            });
            for (Ticket ticket : ticketList) {
                // log.info("ticket: {}", ticket.toString());
                String emailOriginal = ticket.getEmail() != null ? ticket.getEmail() : ticket.getEmailAlternativo();
                String alfaNum = ticket.getIdTicket() != null ? ticket.getIdTicket() : "NA";
                String emailAlias = Utils.emailAlias(emailOriginal, alfaNum);
                ticket.setEmail(emailAlias);
                manageLog.recordServiceComplainLog(Utils.fillForReclamos(ticket, ConstantDb.CONFIG_RECLAMOS_TECNICOS));
                //
            }
        } catch (StreamReadException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DatabindException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        log.info("=================================================================");
        log.info("fGetReclamosTecnicosDex: " + reclamosTecnicosDexUrl + " {}", ticketList);
        log.info("=================================================================");
        return ticketList;

    }

    private List<TicketServicoCliente> fGetExperienciaCallDex(String token) throws Exception {
        List<TicketServicoCliente> ticketList = new ArrayList<>();

        try {
            ticketList = webClient
                    .get()
                    .uri(experienceCallDexUrl)
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .retrieve()
                    .bodyToFlux(TicketServicoCliente.class)
                    .collectList()
                    .block();
            log.info("=================================================================");
            log.info("fGetExperienciaCallDex: " + experienceCallDexUrl + " {}", ticketList);
            log.info("=================================================================");
            if (ticketList.size() == 0) {
                throw new Exception("ticketList esta vacia");
            } else if (ticketList.get(0).getIdTicket() == null
                    || ticketList.get(0).getIdTicket().trim().length() == 0) {
                throw new Exception("ticketList esta vacia");
            }
        } catch (WebClientResponseException e) {
            log.error("Error al consumir el servicio fGetExperienciaCallDex: {}", e.getRawStatusCode());
            log.error("Respuesta del servidor: {}", e.getResponseBodyAsString());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al consumir el servicio {}", e.getMessage());
            throw e;
        }
        return ticketList;
    }

    public List<TicketServicoCliente> fGetExperienciaCallDex() throws Exception {

        Token token = tokenService.getToken();
        List<TicketServicoCliente> ticketList = fGetExperienciaCallDex(token.getAccessToken());
        for (TicketServicoCliente ticket : ticketList) {
            log.info("ticket: {}", ticket.toString());
            String emailOriginal = ticket.getEmail() != null ? ticket.getEmail() : ticket.getEmailAlternativo();
            String alfaNum = ticket.getIdTicket() != null ? ticket.getIdTicket() : "NA";
            String emailAlias = Utils.emailAlias(emailOriginal, alfaNum);
            ticket.setEmail(emailAlias);
            ticket.setEmailOriginal(emailOriginal);
            manageLog.recordServiceComplainLog(Utils.fillExperiencia(ticket, ConstantDb.CONFIG_SRCIO_AL_CLI_REMO_CC));
        }
        return ticketList;
    }

    public List<TicketServicoCliente> fGetExperienciaCallDex_dummy() {
        ClassPathResource resource = new ClassPathResource("experiencia_callcenter.json");
        ObjectMapper objectMapper = new ObjectMapper();
        List<TicketServicoCliente> ticketList = null;
        try {
            ticketList = objectMapper.readValue(resource.getInputStream(),
                    new TypeReference<List<TicketServicoCliente>>() {});
            for (TicketServicoCliente ticket : ticketList) {
                // log.info("ticket: {}", ticket.toString());
                String emailOriginal = ticket.getEmail() != null ? ticket.getEmail() : ticket.getEmailAlternativo();
                String alfaNum = ticket.getIdTicket() != null ? ticket.getIdTicket() : "NA";
                String emailAlias = Utils.emailAlias(emailOriginal, alfaNum);
                ticket.setEmail(emailAlias);
                manageLog.recordServiceComplainLog(Utils.fillExperiencia(ticket, ConstantDb.CONFIG_SRCIO_AL_CLI_REMO_CC));
            }
        } catch (StreamReadException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DatabindException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        log.info("=================================================================");
        log.info("fGetExperienciaCallDex: " + experienceCallDexUrl + " {}", ticketList);
        log.info("=================================================================");
        return ticketList;

    }

    public List<Ticket> fGetReclamosTecnicosCcDex() throws Exception {

        Token token = tokenService.getToken();
        List<Ticket> ticketList = fGetReclamosTecnicosCcDex(token.getAccessToken());
        for (Ticket ticket : ticketList) {
            log.info("ticket: {}", ticket.toString());
            String emailOriginal = ticket.getEmail() != null ? ticket.getEmail() : ticket.getEmailAlternativo();
            String alfaNum = ticket.getIdTicket() != null ? ticket.getIdTicket() : "NA";
            String emailAlias = Utils.emailAlias(emailOriginal, alfaNum);
            ticket.setEmail(emailAlias);
            ticket.setEmailOriginal(emailOriginal);
            manageLog.recordServiceComplainLog(Utils.fillForReclamos(ticket, ConstantDb.CONFIG_RECLAMOS_CALL_CENTER));
        }
        return ticketList;
    }

    public List<Ticket> fGetReclamosTecnicosCcDex_dummy() {
        ClassPathResource resource = new ClassPathResource("reclamos_tecnicos_cc.json");
        ObjectMapper objectMapper = new ObjectMapper();
        List<Ticket> ticketList = null;
        try {
            ticketList = objectMapper.readValue(resource.getInputStream(), new TypeReference<List<Ticket>>() {
            });
            for (Ticket ticket : ticketList) {
                // log.info("ticket: {}", ticket.toString());
                String emailOriginal = ticket.getEmail() != null ? ticket.getEmail() : ticket.getEmailAlternativo();
                String alfaNum = ticket.getIdTicket() != null ? ticket.getIdTicket() : "NA";
                String emailAlias = Utils.emailAlias(emailOriginal, alfaNum);
                ticket.setEmail(emailAlias);
                manageLog.recordServiceComplainLog(Utils.fillForReclamos(ticket, ConstantDb.CONFIG_RECLAMOS_CALL_CENTER));
            }
        } catch (StreamReadException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DatabindException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        log.info("=================================================================");
        log.info("fGetReclamosTecnicosCcDex: " + reclamosTecnicosCcDexUrl + " {}", ticketList);
        log.info("=================================================================");
        return ticketList;

    }

    private List<Ticket> fGetReclamosTecnicosCcDex(String token) throws Exception {
        List<Ticket> ticketList = new ArrayList<>();

        try {
            ticketList = webClient
                    .get()
                    .uri(reclamosTecnicosCcDexUrl)
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .retrieve()
                    .bodyToFlux(Ticket.class)
                    .collectList()
                    .block();
            log.info("=================================================================");
            log.info("fGetReclamosTecnicosCcDex: " + reclamosTecnicosCcDexUrl + " {}", ticketList);
            log.info("=================================================================");
            if (ticketList.size() == 0) {
                throw new Exception("ticketList esta vacia");
            } else if (ticketList.get(0).getIdTicket() == null
                    || ticketList.get(0).getIdTicket().trim().length() == 0) {
                throw new Exception("ticketList esta vacia");
            }
        } catch (WebClientResponseException e) {
            log.error("Error al consumir el servicio fGetReclamosTecnicosCcDex: {}", e.getRawStatusCode());
            log.error("Respuesta del servidor: {}", e.getResponseBodyAsString());
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al consumir el servicio {}", e.getMessage());
            throw e;
        }
        return ticketList;
    }

    public List<Contrato> preVenta() throws Exception {
        ClassPathResource resource = new ClassPathResource("preventa.json");
        ObjectMapper objectMapper = new ObjectMapper();
        List<Contrato> ticketList = null;
        try {
            ticketList = objectMapper.readValue(resource.getInputStream(), new TypeReference<List<Contrato>>() {
            });
            for (Contrato contrato : ticketList) {
                // log.info("contrato: {}", contrato.toString());
                String emailOriginal = contrato.getContactoEmail() != null ? contrato.getContactoEmail()
                        : contrato.getEmailAlternativoContacto();
                String alfaNum = contrato.getContrato() != null ? contrato.getContrato() : "NA";
                String emailAlias = Utils.emailAlias(emailOriginal, alfaNum);
                contrato.setContactoEmail(emailAlias);
                contrato.setEmailOriginal(emailOriginal);
                manageLog.recordInstallationLog(Utils.fillForInstallLog(contrato, emailOriginal, ConstantDb.PREVENTA));
            }
        } catch (StreamReadException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DatabindException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        log.info("=================================================================");
        log.info("venta/preventa: " + preventaUrl + " : {}", ticketList);
        log.info("=================================================================");
        return ticketList;
    }
}
