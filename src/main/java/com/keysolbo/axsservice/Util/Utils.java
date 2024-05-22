package com.keysolbo.axsservice.Util;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keysolbo.axsservice.model.Contrato;
import com.keysolbo.axsservice.model.Ticket;
import com.keysolbo.axsservice.model.db.ContactList;
import com.keysolbo.axsservice.model.db.ContactLog;
import com.keysolbo.axsservice.model.db.InstallationLog;
import com.keysolbo.axsservice.model.db.ServiceComplainLog;
import com.keysolbo.axsservice.model.survey.Contact;
import com.keysolbo.axsservice.model.survey.TicketServicoCliente;

public class Utils {
    public static String getContactListIdByName(List<ContactList> contactLists, String name) {
        Optional<ContactList> matchingContactList = contactLists.stream()
                .filter(contactList -> contactList.getName().equals(name))
                .findFirst();
        return matchingContactList.map(ContactList::getId).orElse(null);
    }

    public static String getCurrentDateTimeString() {
        // Obtener la fecha y hora actual con el desplazamiento UTC
        OffsetDateTime currentDateTime = OffsetDateTime.now(ZoneOffset.UTC);

        // Formatear la fecha y hora en el formato deseado
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        System.out.println("FECHA HORA DE ENVIO: " + currentDateTime.format(formatter).replace("Z", "+00:00"));
        return currentDateTime.format(formatter).replace("Z", "+00:00");
    }

    public static void waitMilliSeconds(int milliSeconds) {
        try {
            Thread.sleep(milliSeconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static InstallationLog fillForInstallLog(Contrato contrato, String emailOriginal, String servicioComplain) {
        InstallationLog installationLog = new InstallationLog();
        try {
            installationLog.setContrato(contrato.getContrato());
            installationLog.setFechaInicio(contrato.getFechaInicio());
            installationLog.setPeriodoInstalacion(contrato.getPeriodoInstalacion());
            installationLog.setIdServicio(contrato.getIdServicio());
            installationLog.setCiudad(contrato.getCiudad());
            installationLog.setInstancia(contrato.getInstancia());
            installationLog.setVendedor(contrato.getVendedor());
            installationLog.setCiudadVendedor(contrato.getCiudadVendedor());
            installationLog.setCanalVendedor(contrato.getCanalVendedor());
            installationLog.setContactoEmail(contrato.getContactoEmail());
            installationLog.setEmailAlternativoContacto(contrato.getEmailAlternativoContacto());
            installationLog.setTecnicoInstalacion(contrato.getTecnicoInstalacion());
            installationLog.setTelefonoCelular(contrato.getTelefonoCelular());
            installationLog.setCiudadServicio(contrato.getCiudadServicio());
            installationLog.setIdVendedor(contrato.getIdVendedor());
            installationLog.setServiceComplain(servicioComplain);
            installationLog.setEmailOriginal(emailOriginal);
        } catch (Exception ex) {
            System.out.println("fillForInstallLog error: " + ex.getMessage());
        }
        return installationLog;
    }

    public static ServiceComplainLog fillForReclamos(Ticket ticket, String servicioComplain) {
        ServiceComplainLog serviceComplainLog = new ServiceComplainLog();
        try {
            serviceComplainLog.setIdTicket(ticket.getIdTicket());
            serviceComplainLog.setCodCliente(ticket.getCodCliente());
            serviceComplainLog.setIdServicio(ticket.getIdServicio());
            serviceComplainLog.setContrato(ticket.getContrato());
            serviceComplainLog.setDireccionInstalacion(ticket.getDireccionInstalacion());
            serviceComplainLog.setCiudad(ticket.getCiudad());
            serviceComplainLog.setFechaApertura(stringToDate(ticket.getFechaApertura()));
            serviceComplainLog.setFechaCierre(stringToDate(ticket.getFechaCierre()));
            serviceComplainLog.setEmail(ticket.getEmail());
            serviceComplainLog.setEmailAlternativo(ticket.getEmailAlternativo());
            serviceComplainLog.setTelCelular(ticket.getTelCelular());
            serviceComplainLog.setCiudadServicio(ticket.getCiudadServicio());
            serviceComplainLog.setCreadoPor(ticket.getCreadoPor());
            serviceComplainLog.setAreaCreacion(ticket.getAreaCreacion());
            serviceComplainLog.setFechaSolucion(stringToDate(ticket.getFechaSolucion()));
            serviceComplainLog.setArea(ticket.getArea());
            serviceComplainLog.setSubArea(ticket.getSubArea());
            serviceComplainLog.setSintoma(ticket.getSintoma());
            serviceComplainLog.setServiceComplain(servicioComplain);
        } catch (Exception ex) {
            System.out.println(servicioComplain + " error: " + ex.getMessage());
        }
        return serviceComplainLog;
    }

    public static ServiceComplainLog fillExperiencia(TicketServicoCliente ticket, String serviceComplain) {
        ServiceComplainLog serviceComplainLog = new ServiceComplainLog();
        try {
            serviceComplainLog.setIdTicket(ticket.getIdTicket());
            serviceComplainLog.setCodCliente(ticket.getCodCliente());
            serviceComplainLog.setIdServicio(ticket.getIdServicio());
            serviceComplainLog.setIdContacto(ticket.getIdContacto());
            serviceComplainLog.setArea(ticket.getArea());
            serviceComplainLog.setSubArea(ticket.getSubArea());
            serviceComplainLog.setSintoma(ticket.getSintoma());
            serviceComplainLog.setCreadoPor(ticket.getCreadoPor());
            serviceComplainLog.setAreaCreador(ticket.getAreaCreador());
            serviceComplainLog.setFechaCierre(stringToDate(ticket.getFechaCierre()));
            serviceComplainLog.setCiudad(ticket.getCiudad());
            serviceComplainLog.setSucursal(ticket.getSucursal());
            serviceComplainLog.setEmail(ticket.getEmail());
            serviceComplainLog.setEmailAlternativo(ticket.getEmailAlternativo());
            serviceComplainLog.setTelCelular(ticket.getTelCelular());
            serviceComplainLog.setCiudadServicio(ticket.getCiudadServicio());
            serviceComplainLog.setContrato(ticket.getContrato());
            serviceComplainLog.setFechaApertura(stringToDate(ticket.getFechaApertura()));
            serviceComplainLog.setFechaSolucion(stringToDate(ticket.getFechaSolucion()));
            serviceComplainLog.setServiceComplain(serviceComplain);
        } catch (Exception ex) {
            System.out.println(serviceComplain + " error: " + ex.getMessage());
        }
        return serviceComplainLog;
    }

    public static String stringToDate(String fechaString) {
        if (fechaString == null || fechaString.isEmpty()) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            LocalDateTime fecha = LocalDateTime.parse(fechaString, formatter);
            String fechaMySQL = fecha.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            return fechaMySQL;
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static String IsoStringDate(String fechaString) {
        if (fechaString == null || fechaString.isEmpty()) {
            return null;
        }
        try {
            OffsetDateTime fecha = OffsetDateTime.parse(fechaString);
            LocalDateTime fechaLocal = fecha.toLocalDateTime();
            String fechaMySQL = fechaLocal.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            return fechaMySQL;
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static String emailAlias(String email, String alfanumerico) {
        if (email != null && email.trim().length() > 0) {
            int indiceArroba = email.indexOf('@');
            if (indiceArroba != -1) {
                String parteAntesDeArroba = email.substring(0, indiceArroba);
                String parteDespuesDeArroba = email.substring(indiceArroba);
                return parteAntesDeArroba.trim() + "+" + alfanumerico + parteDespuesDeArroba;
            } else {
                return null;
            }
        }else{
            return null;
        }
    }

    public static ContactLog fromContactToContactLog(Contact contact, String serviceComplain) {
        ContactLog contactLog = new ContactLog();
        try {
            contactLog.setFirstName(contact.getFirstName());
            contactLog.setLastName(contact.getLastName());
            contactLog.setEmail(contact.getEmail());
            contactLog.setCustomField1(contact.getCustomFields().getField1());
            contactLog.setCustomField2(contact.getCustomFields().getField2());
            contactLog.setCustomField3(contact.getCustomFields().getField3());
            contactLog.setCustomField4(contact.getCustomFields().getField4());
            contactLog.setCustomField5(contact.getCustomFields().getField5());
            contactLog.setCustomField6(contact.getCustomFields().getField6());
            contactLog.setCustomField7(contact.getCustomFields().getField7());
            contactLog.setCustomField8(contact.getCustomFields().getField8());
            contactLog.setCustomField9(contact.getCustomFields().getField9());
            contactLog.setCustomField10(contact.getCustomFields().getField10());
            contactLog.setCustomField11(contact.getCustomFields().getField11());
            contactLog.setCustomField12(contact.getCustomFields().getField12());
            contactLog.setCustomField13(contact.getCustomFields().getField13());
            contactLog.setCustomField14(contact.getCustomFields().getField14());
            contactLog.setCustomField15(contact.getCustomFields().getField15());
            contactLog.setCustomField16(contact.getCustomFields().getField16());
            contactLog.setCustomField17(contact.getCustomFields().getField17());
            contactLog.setCustomField18(contact.getCustomFields().getField18());
            contactLog.setCustomField19(contact.getCustomFields().getField19());
            contactLog.setCustomField20(contact.getCustomFields().getField20());
            contactLog.setCustomField21(contact.getCustomFields().getField21());
            contactLog.setCustomField22(contact.getCustomFields().getField22());
            contactLog.setCustomField23(contact.getCustomFields().getField23());
            contactLog.setCustomField24(contact.getCustomFields().getField24());
            contactLog.setCustomField25(contact.getCustomFields().getField25());
            contactLog.setCustomField25(contact.getCustomFields().getField26());
            contactLog.setCustomField27(contact.getCustomFields().getField27());
            contactLog.setCustomField28(contact.getCustomFields().getField28());
            contactLog.setCustomField29(contact.getCustomFields().getField29());
            contactLog.setCustomField30(contact.getCustomFields().getField30());
            if (serviceComplain.equals(ConstantDb.CONFIG_INSTALACION)) {
                contactLog.setFechaApertura(IsoStringDate(contact.getCustomFields().getField8()));
            } else if (serviceComplain.equals(ConstantDb.CONFIG_RECLAMOS_CALL_CENTER)) {
                contactLog.setFechaApertura(stringToDate(contact.getCustomFields().getField12()));
            } else if (serviceComplain.equals(ConstantDb.CONFIG_RECLAMOS_TECNICOS)) {
                contactLog.setFechaApertura(stringToDate(contact.getCustomFields().getField12()));
            } else if (serviceComplain.equals(ConstantDb.CONFIG_RECLAMO_COMERCIALES)) {
                contactLog.setFechaApertura(stringToDate(contact.getCustomFields().getField13()));
            } else if (serviceComplain.equals(ConstantDb.CONFIG_SRCIO_AL_CLI_PRESEN_REGIO)) {
                contactLog.setFechaApertura(stringToDate(contact.getCustomFields().getField11()));
            } else if (serviceComplain.equals(ConstantDb.CONFIG_SRCIO_AL_CLI_REMO_CC)) {
                contactLog.setFechaApertura(stringToDate(contact.getCustomFields().getField11()));
            } else if (serviceComplain.equals(ConstantDb.PREVENTA)) {
                contactLog.setFechaApertura(IsoStringDate(contact.getCustomFields().getField8()));
            }else if (serviceComplain.equals(ConstantDb.CONFIG_INSTALACION_VENTA)) {
                contactLog.setFechaApertura(IsoStringDate(contact.getCustomFields().getField8()));
            }
            contactLog.setContactStatus(contact.getStatus());
            contactLog.setContactId(contact.getId());
            contactLog.setContactHref(contact.getHref());
            contactLog.setServiceComplain(serviceComplain);
            contactLog.setStatus(ConstantDb.ACTIVE_STATUS);
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return contactLog;
    }

    public static String stringJson(List<String> list) {
        String result = "{}";
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            result = objectMapper.writeValueAsString(list);
        } catch (Exception e) {
            result = "{}";
        }
        return result;
    }
}
