package com.keysolbo.axsservice.model.db;

import java.sql.Timestamp;

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
public class InstallationLog {
    private Integer id;
    private String contrato;
    private String fechaInicio;
    private String periodoInstalacion;
    private String idServicio;
    private String ciudad;
    private String instancia;
    private String vendedor;
    private String ciudadVendedor;
    private String canalVendedor;
    private String contactoEmail;
    private String emailAlternativoContacto;
    private String emailOriginal;
    private String tecnicoInstalacion;
    private String telefonoCelular;
    private String ciudadServicio;
    private String idVendedor;
    private String serviceComplain;
    private String status;
    private String sendStatus;
    private Timestamp recordDate;
}
