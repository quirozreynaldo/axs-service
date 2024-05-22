package com.keysolbo.axsservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Ticket {
    private String idTicket;
    private String codCliente;
    private String idServicio;
    private String contrato;
    private String direccionInstalacion;
    private String ciudad;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private String fechaApertura;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private String fechaCierre;
    private String email;
    private String emailAlternativo;
    private String telCelular;
    private String ciudadServicio;
    private String creadoPor;
    private String areaCreacion;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private String fechaSolucion;
    private String area;
    private String subArea;
    private String sintoma;
    private String emailOriginal;
}