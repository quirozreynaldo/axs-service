package com.keysolbo.axsservice.model.db;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ServiceComplainLog {
    private Integer id;
    private String idTicket;
    private String codCliente;
    private String idServicio;
    private String idContacto;
    private String area;
    private String subArea;
    private String sintoma;
    private String creadoPor;
    private String areaCreador;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private String fechaCierre;
    private String email;
    private String emailAlternativo;
    private String emailOriginal;
    private String telCelular;
    private String ciudadServicio;
    private String contrato;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private String fechaApertura;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private String fechaSolucion;
    private String ciudad;
    private String sucursal;
    private String direccionInstalacion;
    private String areaCreacion;
    private String serviceComplain;
    private String status;
    private String sendStatus;
    private Timestamp recordDate;
}
