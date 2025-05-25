package com.keysolbo.axsservice.database;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import com.keysolbo.axsservice.Util.ConstantDb;
import com.keysolbo.axsservice.Util.Utils;
import com.keysolbo.axsservice.model.db.ContactHistory;
import com.keysolbo.axsservice.model.db.ContactLog;
import com.keysolbo.axsservice.model.db.InstallationLog;
import com.keysolbo.axsservice.model.db.ServiceComplainLog;
import com.keysolbo.axsservice.model.db.WebhookLog;
import com.keysolbo.axsservice.model.survey.Recipient;
import com.keysolbo.axsservice.model.survey.RecipientResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ManageLog {
    @Autowired
    @Qualifier("digitalTemplate")
    private JdbcTemplate jdbcTemplate;

    public void recordContactLog(ContactHistory contactHistory){
        log.info(">>>>>>>: {}", contactHistory);
        //String smsgatewayId = UUID.randomUUID().toString().replace("-", "");
        String insert = "INSERT INTO record.contact_history " +
                        "( email, name, last_name, phone_number, state, data, href) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
           // contactHistory.setSmContactId(smsgatewayId);
    
            jdbcTemplate.update(insert, 
                                contactHistory.getEmail(), 
                                contactHistory.getName(), 
                                contactHistory.getLastName(), 
                                contactHistory.getPhoneNumber(), 
                                contactHistory.getState(), 
                                contactHistory.getData(), 
                                contactHistory.getHref());
        } catch (Exception ex){
            log.error("Record log exception", ex);
        }
    }

    public void recordServiceComplainLog(ServiceComplainLog  serviceComplain){
       // log.info(">>>>>>>: {}", serviceComplain);
        String insert = "INSERT INTO deaxs_record.service_complain_log  ( id_ticket, cod_cliente, id_servicio, "+
        " id_contacto, area, sub_area, sintoma, creado_por, area_creador, fecha_cierre, email, email_alternativo, "+
        " tel_celular, ciudad_servicio, contrato, fecha_apertura, fecha_solucion, ciudad, sucursal, direccion_instalacion,"+
        " area_creacion, service_complain, status, send_status) "+ 
        " VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
        try {
            jdbcTemplate.update(insert, 
            serviceComplain.getIdTicket(),serviceComplain.getCodCliente(),serviceComplain.getIdServicio(),
            serviceComplain.getIdContacto(),serviceComplain.getArea(),serviceComplain.getSubArea(),serviceComplain.getSintoma(),
            serviceComplain.getCreadoPor(),serviceComplain.getAreaCreador(),serviceComplain.getFechaCierre(),serviceComplain.getEmail(),
            serviceComplain.getEmailAlternativo(),Utils.cleanString(serviceComplain.getTelCelular()),serviceComplain.getCiudadServicio(),
            serviceComplain.getContrato(),serviceComplain.getFechaApertura(),serviceComplain.getFechaSolucion(),
            serviceComplain.getCiudad(),serviceComplain.getSucursal(),serviceComplain.getDireccionInstalacion(),
            serviceComplain.getAreaCreacion(),serviceComplain.getServiceComplain(),serviceComplain.getStatus(),serviceComplain.getSendStatus());
        } catch (Exception ex){
            log.error("recordServiceComplainLog log exception", ex);
        }
    }
    public void recordInstallationLog(InstallationLog  installationLog){
        log.info(">>>>>>>: {}", installationLog);
        String insert = "INSERT INTO deaxs_record.installation_log (contrato, fecha_inicio, periodo_instalacion,"+
                        " id_servicio, ciudad, instancia, vendedor, ciudad_vendedor, canal_vendedor, contacto_email, "+
                        " email_alternativo_contacto, tecnico_instalacion, telefono_celular, ciudad_servicio,"+
                        " service_complain, status, send_status,id_vendedor)"+
        " VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
        try {
            jdbcTemplate.update(insert, installationLog.getContrato(),installationLog.getFechaInicio(),installationLog.getPeriodoInstalacion(),
            installationLog.getIdServicio(),installationLog.getCiudad(),installationLog.getInstancia(),installationLog.getVendedor(),
            installationLog.getCiudadVendedor(),installationLog.getCanalVendedor(),installationLog.getContactoEmail(),
            installationLog.getEmailAlternativoContacto(),installationLog.getTecnicoInstalacion(),Utils.cleanString(installationLog.getTelefonoCelular()),
            installationLog.getCiudadServicio(),installationLog.getServiceComplain(),installationLog.getStatus(),installationLog.getSendStatus(),installationLog.getIdVendedor());
        } catch (Exception ex){
            log.error("recordInstallationLog log exception", ex);
        }
    }

    public void recordContactLog(ContactLog  contactLog){
        log.info(">>>>>>>: {}", contactLog);
        String insert = "INSERT INTO deaxs_record.contact_log ( first_name, last_name, email, custom_field_1, "+
                        "custom_field_2, custom_field_3, custom_field_4, custom_field_5, custom_field_6, custom_field_7, "+
                        "custom_field_8, custom_field_9, custom_field_10, custom_field_11, custom_field_12, custom_field_13,"+
                        "custom_field_14, custom_field_15, custom_field_16, custom_field_17, custom_field_18, custom_field_19,"+
                        "custom_field_20, custom_field_21, custom_field_22, custom_field_23, custom_field_24, custom_field_25,"+
                        "custom_field_26, custom_field_27, custom_field_28, custom_field_29, custom_field_30, fecha_apertura,"+
                        " contact_status, contact_id, contact_href, service_complain, status) "+
               "  VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            jdbcTemplate.update(insert, contactLog.getFirstName(),contactLog.getLastName(),contactLog.getEmail(),contactLog.getCustomField1()
            ,contactLog.getCustomField2(),contactLog.getCustomField3(),contactLog.getCustomField4(),contactLog.getCustomField5(),contactLog.getCustomField6()
            ,contactLog.getCustomField7(),contactLog.getCustomField8(),contactLog.getCustomField9(),contactLog.getCustomField10(),contactLog.getCustomField11()
            ,contactLog.getCustomField12(),contactLog.getCustomField13(),contactLog.getCustomField14(),contactLog.getCustomField15(),contactLog.getCustomField16()
            ,contactLog.getCustomField17(),contactLog.getCustomField18(),contactLog.getCustomField19(),contactLog.getCustomField20(),contactLog.getCustomField21()
            ,contactLog.getCustomField22(),contactLog.getCustomField23(),contactLog.getCustomField24(),contactLog.getCustomField25(),contactLog.getCustomField26()
            ,contactLog.getCustomField27(),contactLog.getCustomField28(),contactLog.getCustomField29(),contactLog.getCustomField30(),contactLog.getFechaApertura(),
            contactLog.getContactStatus(),contactLog.getContactId(),contactLog.getContactHref(),contactLog.getServiceComplain(),contactLog.getStatus());
        } catch (Exception ex){
            log.error("recordContactLog log exception", ex);
        }
    }


    private void recipientLog(RecipientLog  recipientLog){
        log.info(">>>>>>>: {}", recipientLog);
        String insert = "INSERT INTO deaxs_record.recipient_log( recipient_id, email, phone_number, href,"+
        " bounced, existing, duplicate, invalids, opted_out, collector_id, message_id, service_complain, status, sent, responded) "+
        " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)";
        try {
            jdbcTemplate.update(insert, recipientLog.getRecipientId(),recipientLog.getEmail(),recipientLog.getPhoneNumber(),
            recipientLog.getHref(),recipientLog.getBounced(),recipientLog.getExisting(),recipientLog.getDuplicate(),recipientLog.getInvalids(),
            recipientLog.getOptedOut(),recipientLog.getCollectorId(),recipientLog.getMessageId(), recipientLog.getServiceComplain(),recipientLog.getStatus(),
            recipientLog.getSent(),recipientLog.getResponded());
        } catch (Exception ex){
            log.error("recordRecipientLog log exception", ex);
        }
    }

    public void recordRecipientLog(RecipientResponse recipientResponse,String collectorId,String messageId,String serviceComplain){
        List<Recipient> succeededs = recipientResponse.getSucceeded();
        for(Recipient recipient:succeededs){
            RecipientLog  recipientLog = new RecipientLog();
            recipientLog.setRecipientId(recipient.getId()); 
            recipientLog.setEmail(recipient.getEmail());
            recipientLog.setPhoneNumber(recipient.getPhone_number());
            recipientLog.setHref(recipient.getHref());
            recipientLog.setCollectorId(collectorId);
            recipientLog.setMessageId(messageId);
            recipientLog.setServiceComplain(serviceComplain);
            recipientLog.setStatus(ConstantDb.ACTIVE_STATUS);
            recipientLog.setBounced(Utils.stringJson(recipientResponse.getBounced()));
            recipientLog.setExisting(Utils.stringJson(recipientResponse.getExisting()));
            recipientLog.setDuplicate(Utils.stringJson(recipientResponse.getDuplicate()));
            recipientLog.setInvalids(Utils.stringJson(recipientResponse.getInvalids()));
            recipientLog.setOptedOut(Utils.stringJson(recipientResponse.getOpted_out()));
            recipientLog.setSent(ConstantDb.SENT_YES);
            recipientLog(recipientLog);
        }

    }

    public void recordWebhook(WebhookLog  webhookLog){
        log.info(">>>>>>>: {}", webhookLog);
        String insert = "INSERT INTO deaxs_record.webhook_log (name, filter_type, filter_id, event_type, event_id, event_datetime, object_type, "+
        " `object_id`, respondent_id, recipient_id, survey_id, user_id, collector_id, status, contact_updated) "+
        " VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            jdbcTemplate.update(insert,webhookLog.getName(),webhookLog.getFilterType(),webhookLog.getFilterId(),webhookLog.getEventType(),
            webhookLog.getEventId(),webhookLog.getEventDatetime(),webhookLog.getObjectType(),webhookLog.getObjectId(),webhookLog.getRespondentId(),
            webhookLog.getRecipientId(),webhookLog.getSurveyId(),webhookLog.getUserId(),webhookLog.getCollectorId(),webhookLog.getStatus(),webhookLog.getContactUpdated());
        } catch (Exception ex){
            log.error("recordWebhook log exception", ex);
        }
    }
    public int updateRecipientResponded(String collectorId, String recipientId,String responded){
        int updateRows =0;
        String update="UPDATE deaxs_record.recipient_log SET responded= ? WHERE collector_id = ? AND recipient_id = ?";
        try {
             updateRows= jdbcTemplate.update(update, responded,collectorId,recipientId);
        }catch (Exception ex){
            log.error("updateRecipientResponded log exception",ex);
        }
        return updateRows;
    }
}
