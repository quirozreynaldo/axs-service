package com.keysolbo.axsservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.keysolbo.axsservice.model.AxsResponse;
import com.keysolbo.axsservice.model.Contrato;
import com.keysolbo.axsservice.model.Ticket;
import com.keysolbo.axsservice.model.Token;
import com.keysolbo.axsservice.model.survey.TicketServicoCliente;
import com.keysolbo.axsservice.service.AxsService;
import com.keysolbo.axsservice.service.TokenService;

@RequestMapping("/api")
@RestController
public class AxsController {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AxsService axsService;

    @GetMapping("/token")
    public AxsResponse<Token> getToken() {
        AxsResponse<Token> axsResponse = new AxsResponse<>();
        Token token = tokenService.getToken();
        axsResponse.setErrorCode("OK");
        axsResponse.setData(token);
        return axsResponse;
    }
    @GetMapping("/venta")
    public AxsResponse<List<Contrato>> venta() throws Exception {
        List<Contrato> contractList = axsService.instalacion();
        AxsResponse<List<Contrato>> response = new AxsResponse<>();
        response.setErrorCode("OK");
        response.setData(contractList);
        return response;
    }
    @GetMapping("/experiencia-tienda-dex")
    public AxsResponse<List<TicketServicoCliente>> fGetExperienciaTiendaDex() throws Exception {
        List<TicketServicoCliente> ticketList = axsService.fGetExperienciaTiendaDex();
        AxsResponse<List<TicketServicoCliente>> response = new AxsResponse<>();
        response.setErrorCode("OK");
        response.setData(ticketList);
        return response;
    }

    @GetMapping("/reclamos-comerciales-dex")
    public AxsResponse<List<Ticket>> fGetReclamosComercialesDex() throws Exception {
        List<Ticket> ticketList = axsService.fGetReclamosComercialesDex();
        AxsResponse<List<Ticket>> response = new AxsResponse<>();
        response.setErrorCode("OK");
        response.setData(ticketList);
        return response;
    }

    @GetMapping("/reclamos-tecnicos-dex")
    public AxsResponse<List<Ticket>> fGetReclamosTecnicosDex() throws Exception {
        List<Ticket> ticketList = axsService.fGetReclamosTecnicosDex();
        AxsResponse<List<Ticket>> response = new AxsResponse<>();
        response.setErrorCode("OK");
        response.setData(ticketList);
        return response;
    }

    @GetMapping("/experiencia-call-dex")
    public AxsResponse<List<TicketServicoCliente>> fGetExperienciaCallDex() throws Exception {
        List<TicketServicoCliente> ticketList = axsService.fGetExperienciaCallDex();
        AxsResponse<List<TicketServicoCliente>> response = new AxsResponse<>();
        response.setErrorCode("OK");
        response.setData(ticketList);
        return response;
    }
}
