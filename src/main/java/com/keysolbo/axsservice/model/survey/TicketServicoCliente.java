package com.keysolbo.axsservice.model.survey;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class TicketServicoCliente {
    @JsonProperty("idTicket")
    private String idTicket;
    @JsonProperty("codCliente")
    private String codCliente;
    @JsonProperty("idServicio")
    private String idServicio;
    @JsonProperty("idContacto")
    private String idContacto;
    private String area;
    @JsonProperty("subArea")
    private String subArea;
    private String sintoma;
    @JsonProperty("creadoPor")
    private String creadoPor;
    @JsonProperty("areaCreador")
    private String areaCreador;
    @JsonProperty("fechaCierre")
    private String fechaCierre;
    private String ciudad;
    private String sucursal;
    private String email;
    @JsonProperty("emailAlternativo")
    private String emailAlternativo;
    private String telCelular;
    @JsonProperty("ciudadServicio")
    private String ciudadServicio;
    private String contrato;
    @JsonProperty("fechaApertura")
    private String fechaApertura;
    @JsonProperty("fechaSolucion")
    private String fechaSolucion;
    private String emailOriginal;
}
