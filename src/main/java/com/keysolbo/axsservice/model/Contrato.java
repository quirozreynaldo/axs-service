package com.keysolbo.axsservice.model;

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
public class Contrato {
    private String contrato;
    private String fechaInicio;
    private String periodoInstalacion;
    private String idServicio;
    private String ciudad;
    private String instancia;
    private String idVendedor;
    private String vendedor;
    private String ciudadVendedor;
    private String canalVendedor;
    private String contactoEmail;
    private String emailAlternativoContacto;
    private String tecnicoInstalacion;
    private String telefonoCelular;
    private String ciudadServicio;
    private String emailOriginal;
}
