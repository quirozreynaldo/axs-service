package com.keysolbo.axsservice.service;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.keysolbo.axsservice.Util.ConstantDb;
import com.keysolbo.axsservice.Util.Utils;
import com.keysolbo.axsservice.database.ManageLog;
import com.keysolbo.axsservice.database.ManageSurveyInMemory;
import com.keysolbo.axsservice.model.Contrato;
import com.keysolbo.axsservice.model.Ticket;
import com.keysolbo.axsservice.model.survey.Contact;
import com.keysolbo.axsservice.model.survey.Contacts;
import com.keysolbo.axsservice.model.survey.CustomFields;
import com.keysolbo.axsservice.model.survey.GetContactList;
import com.keysolbo.axsservice.model.survey.GetContactListsResponse;
import com.keysolbo.axsservice.model.survey.MessageResponse;
import com.keysolbo.axsservice.model.survey.RecipientRequest;
import com.keysolbo.axsservice.model.survey.RecipientRequestList;
import com.keysolbo.axsservice.model.survey.RecipientResponse;
import com.keysolbo.axsservice.model.survey.SendSurveyRequest;
import com.keysolbo.axsservice.model.survey.SendSurveyResponse;
import com.keysolbo.axsservice.model.survey.Succeeded;
import com.keysolbo.axsservice.model.survey.TicketServicoCliente;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class IntegrationService {
    @Autowired
    private AxsService axsService;
    @Autowired
    private SurveyMonkeyService surveyMonkeyService;
    private Hashtable<String, String> hashContactLists;
    @Autowired
    private ManageSurveyInMemory manageSurveyInMemory;
    @Autowired
    private ManageLog manageLog;

   // @PostConstruct
    public void init() {

        hashContactLists = new Hashtable<>();
        GetContactListsResponse contactLists = surveyMonkeyService.getContactList();
        for (GetContactList response : contactLists.getData()) {
            hashContactLists.put(response.getName(), response.getId());
        }
        log.info("IntegrationService-init lista de contactos cargada {}:",hashContactLists.size());
    }

    /*
     * 1.- Consumir api AXS fGetReclamosComercialesDex
     * 2.- Guardar los datos en la BD y marcar en proceso
     * 2.1.-Insertar datos y poner estado en pendiente de proceso
     * 3.- Consumir api mokey
     */
    public void procesoReclamosComerciales() throws Exception {
        log.info("Consumiendo fGetReclamosComercialesDex");
        executeReclamosComerciales();
        //List<Ticket> reclamosComerciales = axsService.fGetReclamosComercialesDex();
        log.info("apunta datos de archivo");
    }

    public String executeReclamosComerciales() throws Exception {
        // 1.-Recupera los tickets del ws
        List<Ticket> reclamosComerciales = axsService.fGetReclamosComercialesDex();
        // 2.-Identifica el contact List id
        ////String contactListId = hashContactLists.get("axs-reclamo-comercial");
        String contactListId = manageSurveyInMemory.getConfigContactList(ConstantDb.CONFIG_RECLAMO_COMERCIALES);
        log.info("axs-reclamo-comercial contactListId={}=============", contactListId);
        // 3.-Adiciona los contactos a la lista de contactos en base los tickets
        Contacts contacts = new Contacts();
        List<Contact> contactsList = new ArrayList<>();
        for (Ticket ticket : reclamosComerciales) {
            if(ticket.getEmail() != null || ticket.getEmailAlternativo()!= null ){
                String dataConcat =
                "*Area: " + (ticket.getArea()!=null?ticket.getArea():"NA") + " " +
                "*Ciudad: " + (ticket.getCiudad()!=null?ticket.getCiudad():"NA") + " " +
                "*Sintoma: " + (ticket.getSintoma()!=null?ticket.getSintoma():"NA") + " " +
                "*SubArea: " + (ticket.getSubArea()!=null?ticket.getSubArea():"NA") + " " +
                "*Creado Por: " + (ticket.getCreadoPor()!=null?ticket.getCreadoPor():"NA") + " " +
                "*Id Servicio: " +(ticket.getIdServicio()!=null?ticket.getIdServicio():"NA")  + " " +
                "*Fecha Cierre: " + (ticket.getFechaCierre()!=null?ticket.getFechaCierre():"NA") + " " +
                "*Area Creación: " + (ticket.getAreaCreacion()!=null?ticket.getAreaCreacion():"NA") + " " +
                "*Fecha Apertura: " +(ticket.getFechaApertura()!=null?ticket.getFechaApertura():"NA")  + " " +
                "*Fecha Solución: " + (ticket.getFechaSolucion()!=null?ticket.getFechaSolucion():"NA") + " " +
                "*Dirección Instalación: " + (ticket.getDireccionInstalacion()!=null?ticket.getDireccionInstalacion():"NA") + " " +
                "*Email Alternativo: " + (ticket.getEmailAlternativo()!=null?ticket.getEmailAlternativo():"NA");
                log.info("dataConcat:*********************************** "+dataConcat);
                Contact contact = new Contact();
                contact.setEmail(ticket.getEmail() != null ? ticket.getEmail() : ticket.getEmailAlternativo());
                contact.setFirstName(ticket.getEmailOriginal()!=null?ticket.getEmailOriginal():"NA");
                contact.setLastName(ticket.getIdTicket()!=null?ticket.getIdTicket():"NA");
                CustomFields customFields = new CustomFields();
                customFields.setField1(ticket.getTelCelular()!=null?ticket.getTelCelular():"NA");
                customFields.setField2(ticket.getIdTicket()!=null?ticket.getIdTicket():"NA");
                customFields.setField3(ticket.getContrato()!=null?ticket.getContrato():"NA");
                customFields.setField4(ticket.getCodCliente()!=null?ticket.getCodCliente():"NA");
                customFields.setField5(dataConcat);
                customFields.setField6(ticket.getCiudadServicio()!=null?ticket.getCiudadServicio():"NA");
                contact.setCustomFields(customFields);
                contactsList.add(contact);
        }
        }
        contacts.setContacts(contactsList);
        Succeeded succeeded = surveyMonkeyService.createMultiContacts(contacts, contactListId);
        log.debug("succeeded:{}", succeeded);
        Utils.waitMilliSeconds(500);

        List<Contact> contactList = new ArrayList<>();
        log.info("succeeded.getExisting():{}",succeeded.getExisting());
        log.info("succeeded.getSucceeded(): {}",succeeded.getSucceeded());
        log.info("succeeded.getInvalid(): {}",succeeded.getInvalid());
        if (succeeded.getExisting().size() > 0) {
            contactList.addAll(succeeded.getExisting());
        } 
        if (succeeded.getSucceeded().size() > 0) {
            contactList.addAll(succeeded.getSucceeded());
        }

        ////String collectorId = Constant.COLLECTOR_RECLAMO_COMERCIALES;
        String collectorId= manageSurveyInMemory.getConfigCollector(ConstantDb.CONFIG_RECLAMO_COMERCIALES);
        String messageConfigId=manageSurveyInMemory.getConfigMessage(ConstantDb.CONFIG_RECLAMO_COMERCIALES);
        ////MessageResponse messageResponse = surveyMonkeyService.getCollectorMessages(collectorId, Constant.RECLAMO_COMERCIAL_MESSAGE_ID);
        MessageResponse messageResponse = surveyMonkeyService.getCollectorMessages(collectorId, messageConfigId);
        String messageId = messageResponse.getId();
        Utils.waitMilliSeconds(500);
       /////////////////////////////////////////////////////////////////////////
        List<String> list_contacts = new ArrayList<>();
        List<String> contacts_ids = new ArrayList<>();

        RecipientRequestList recipientRequestList = new RecipientRequestList();
        recipientRequestList.setContact_list_ids(list_contacts);
        for (Contact contact : contactList) {
            contacts_ids.add(contact.getId());
            manageLog.recordContactLog(Utils.fromContactToContactLog(contact, ConstantDb.CONFIG_RECLAMO_COMERCIALES));
        }
       //list_contacts.add(contactListId);
        recipientRequestList.setContact_list_ids(contacts_ids);

        RecipientRequest recipientRequest= new RecipientRequest();
        recipientRequest.setContact_list_ids(list_contacts.toArray(new String[list_contacts.size()]));
        recipientRequest.setContact_ids(contacts_ids.toArray(new String[contacts_ids.size()]));
        RecipientResponse recipientResponse= surveyMonkeyService.addRecipientBulk(recipientRequest,collectorId,messageId);
        log.info("{}",recipientResponse);
        manageLog.recordRecipientLog(recipientResponse,collectorId,messageId,ConstantDb.CONFIG_RECLAMO_COMERCIALES);
        ////////////////////////////////////////////////////////////////////////
        SendSurveyRequest sendSurveyRequest = new SendSurveyRequest();
        // sendSurveyRequest.setScheduled_date("2024-03-30T09:30:00+00:00");
        sendSurveyRequest.setScheduled_date(Utils.getCurrentDateTimeString());
        SendSurveyResponse sendSurveyResponse = surveyMonkeyService.sendSurvey(sendSurveyRequest, collectorId,messageId);
        log.info("{}", sendSurveyResponse);
        Utils.waitMilliSeconds(500);
        
        return "executed";
    }

    public void procesoServicoAlClientePresencial() throws Exception {
        log.info("consumiendo fGetExperienciaTiendaDex");
        executeServicoAlClientePresencial();
        // axsService.fGetExperienciaTiendaDex();
        log.info("Datos de archivo");
    }

    public String executeServicoAlClientePresencial() throws Exception {
        // 1.-Recupera los tickets del ws
        List<TicketServicoCliente> servicoAlClientePresencial = axsService.fGetExperienciaTiendaDex();
        // 2.-Identifica el contact List id
        ////String contactListId = hashContactLists.get("axs-sc-presencial");
        String contactListId = manageSurveyInMemory.getConfigContactList(ConstantDb.CONFIG_SRCIO_AL_CLI_PRESEN_REGIO);
        log.info("axs-sc-presencial contactListId={}", contactListId);
        // 3.-Adiciona los contactos a la lista de contactos en base los tickets
        Contacts contacts = new Contacts();
        List<Contact> contactsList = new ArrayList<>();
        for (TicketServicoCliente ticket : servicoAlClientePresencial) {
            if(ticket.getEmail() != null || ticket.getEmailAlternativo()!= null ){
                String dataConcat =
				    "*Id Contacto: " +(ticket.getIdContacto()!=null?ticket.getIdContacto():"NA")  + " " +
					"*Area: " +(ticket.getArea()!=null?ticket.getArea():"NA")  + " " +
					"*Sub Area: " + (ticket.getSubArea()!=null?ticket.getSubArea():"NA") + " " +
					"*Síntoma: " + (ticket.getSintoma()!=null?ticket.getSintoma():"NA") + " " +
					"*Creado Por: " +(ticket.getCreadoPor()!=null?ticket.getCreadoPor():"NA")  + " " +
					"*Area Creador: " + (ticket.getAreaCreador()!=null?ticket.getAreaCreador():"NA") + " " +
					"*Fecha Apertura: " + (ticket.getFechaApertura()!=null?ticket.getFechaApertura():"NA") + " " +
					"*Fecha Solución: " +(ticket.getFechaSolucion()!=null?ticket.getFechaSolucion():"NA")  + " " +
					"*Fecha Cierre: " + (ticket.getFechaCierre()!=null?ticket.getFechaCierre():"NA") + " " +
					"*Ciudad: " + (ticket.getCiudad()!=null?ticket.getCiudad():"NA") + " " +
					"*Sucursal: " + (ticket.getSucursal()!=null?ticket.getSucursal():"NA") + " " +
					"*Email Alternativo: " + (ticket.getEmailAlternativo()!=null?ticket.getEmailAlternativo():"NA") + " " +
                    "*Teléfono Celular: " + (ticket.getTelCelular()!=null?ticket.getTelCelular():"NA");
                Contact contact = new Contact();
                contact.setEmail(ticket.getEmail() != null ? ticket.getEmail() : ticket.getEmailAlternativo());
                contact.setFirstName(ticket.getEmailOriginal()!=null?ticket.getEmailOriginal():"NA");
                contact.setLastName(ticket.getIdTicket()!=null?ticket.getIdTicket():"NA");
                CustomFields customFields = new CustomFields();
                customFields.setField1(ticket.getIdTicket()!=null?ticket.getIdTicket():"NA");
                customFields.setField2(ticket.getCodCliente()!=null?ticket.getCodCliente():"NA");
                customFields.setField3(ticket.getIdServicio()!=null?ticket.getIdServicio():"NA");
                customFields.setField4(ticket.getContrato()!=null?ticket.getContrato():"NA");
                customFields.setField5(dataConcat);
                customFields.setField6(ticket.getCiudadServicio()!=null?ticket.getCiudadServicio():"NA");
                contact.setCustomFields(customFields);
                contactsList.add(contact);
            }
        }
        contacts.setContacts(contactsList);
        Succeeded succeeded = surveyMonkeyService.createMultiContacts(contacts, contactListId);
        log.debug("succeeded:{}", succeeded);
        Utils.waitMilliSeconds(500);

        List<Contact> contactList = new ArrayList<>();
        log.info("succeeded.getExisting():{}",succeeded.getExisting());
        log.info("succeeded.getSucceeded(): {}",succeeded.getSucceeded());
        log.info("succeeded.getInvalid(): {}",succeeded.getInvalid());
        if (succeeded.getExisting().size() > 0) {
            contactList.addAll(succeeded.getExisting());
        } 
        if (succeeded.getSucceeded().size() > 0) {
            contactList.addAll(succeeded.getSucceeded());
        }
        ////String collectorId = Constant.COLLECTOR_SRCIO_AL_CLI_PRESEN_REGIO;
        String collectorId= manageSurveyInMemory.getConfigCollector(ConstantDb.CONFIG_SRCIO_AL_CLI_PRESEN_REGIO);
        String messageConfigId=manageSurveyInMemory.getConfigMessage(ConstantDb.CONFIG_SRCIO_AL_CLI_PRESEN_REGIO);
        MessageResponse messageResponse = surveyMonkeyService.getCollectorMessages(collectorId, messageConfigId);
        /////MessageResponse messageResponse = surveyMonkeyService.getCollectorMessages(collectorId, Constant.SRCIO_AL_CLI_PRESEN_REGIO_MESSAGE_ID);
        String messageId = messageResponse.getId();
        Utils.waitMilliSeconds(500);
       /////////////////////////////////////////////////////////////////////////
       List<String> list_contacts = new ArrayList<>();
       List<String> contacts_ids = new ArrayList<>();

       RecipientRequestList recipientRequestList = new RecipientRequestList();
       recipientRequestList.setContact_list_ids(list_contacts);
       for (Contact contact : contactList) {
           contacts_ids.add(contact.getId());
           manageLog.recordContactLog(Utils.fromContactToContactLog(contact, ConstantDb.CONFIG_SRCIO_AL_CLI_PRESEN_REGIO));
       }
       //list_contacts.add(contactListId);
       recipientRequestList.setContact_list_ids(contacts_ids);

       RecipientRequest recipientRequest= new RecipientRequest();
       recipientRequest.setContact_list_ids(list_contacts.toArray(new String[list_contacts.size()]));
       recipientRequest.setContact_ids(contacts_ids.toArray(new String[contacts_ids.size()]));
       RecipientResponse recipientResponse= surveyMonkeyService.addRecipientBulk(recipientRequest,collectorId,messageId);
       log.info("{}",recipientResponse);
       manageLog.recordRecipientLog(recipientResponse,collectorId,messageId,ConstantDb.CONFIG_SRCIO_AL_CLI_PRESEN_REGIO);
       ////////////////////////////////////////////////////////////////////////        
        SendSurveyRequest sendSurveyRequest = new SendSurveyRequest();
        // sendSurveyRequest.setScheduled_date("2024-03-30T09:30:00+00:00");
        sendSurveyRequest.setScheduled_date(Utils.getCurrentDateTimeString());
        SendSurveyResponse sendSurveyResponse = surveyMonkeyService.sendSurvey(sendSurveyRequest, collectorId,messageId);
        log.info("{}", sendSurveyResponse);
        Utils.waitMilliSeconds(500);
        return "executed";
    }

    public void procesoReclamosTecnicos() throws Exception {
        log.info("consumiendo fGetReclamosComercialesDex");
        executeReclamosTecnicos();
        //axsService.fGetReclamosTecnicosDex();
        log.info("Datos del archivo json");

    }

    public String executeReclamosTecnicos() throws Exception {
        // 1.-Recupera los tickets del ws
        List<Ticket> reclamosTecnicos = axsService.fGetReclamosTecnicosDex();
        // 2.-Identifica el contact List id
        ////String contactListId = hashContactLists.get("axs-reclamos-tecnicos");
        String contactListId = manageSurveyInMemory.getConfigContactList(ConstantDb.CONFIG_RECLAMOS_TECNICOS);
        log.info("axs-reclamos-tecnicos contactListId={}=============", contactListId);
        // 3.-Adiciona los contactos a la lista de contactos en base los tickets
        Contacts contacts = new Contacts();
        List<Contact> contactsList = new ArrayList<>();
        for (Ticket ticket : reclamosTecnicos) {
            if(ticket.getEmail() != null || ticket.getEmailAlternativo()!= null ){
                String dataConcat =
				    "*Area: " +(ticket.getArea()!=null?ticket.getArea():"NA")  + " " +
					"*Ciudad: " + (ticket.getCiudad()!=null?ticket.getCiudad():"NA") + " " +
					"*Síntoma: " + (ticket.getSintoma()!=null?ticket.getSintoma():"NA") + " " +
					"*Sub Area: " + (ticket.getSubArea()!=null?ticket.getSubArea():"NA") + " " +
					"*Creado Por: " + (ticket.getCreadoPor()!=null?ticket.getCreadoPor():"NA") + " " +
					"*Id Servicio: " + (ticket.getIdServicio()!=null?ticket.getIdServicio():"NA") + " " +
					"*Fecha Cierre: " + (ticket.getFechaCierre()!=null?ticket.getFechaCierre():"NA") + " " +
					"*Fecha Apertura: " + (ticket.getFechaApertura()!=null?ticket.getFechaApertura():"NA") + " " +
					"*Fecha Solución: " + (ticket.getFechaSolucion()!=null?ticket.getFechaSolucion():"NA") + " " +
					"*Dirección Instalación	: " + (ticket.getDireccionInstalacion()!=null?ticket.getDireccionInstalacion():"NA") + " " +
	                "*Email Alternativo: " + (ticket.getEmailAlternativo()!=null?ticket.getEmailAlternativo():"NA");
                Contact contact = new Contact();
                contact.setEmail(ticket.getEmail() != null ? ticket.getEmail() : ticket.getEmailAlternativo());
                contact.setFirstName(ticket.getEmailOriginal()!=null?ticket.getEmailOriginal():"NA");
                contact.setLastName(ticket.getIdTicket()!=null?ticket.getIdTicket():"NA");
                CustomFields customFields = new CustomFields();
                customFields.setField1(ticket.getTelCelular()!=null?ticket.getTelCelular():"NA");
                customFields.setField2(ticket.getIdTicket()!=null?ticket.getIdTicket():"NA");
                customFields.setField3(ticket.getContrato()!=null?ticket.getContrato():"NA");
                customFields.setField4(ticket.getCodCliente()!=null?ticket.getCodCliente():"NA");
                customFields.setField5(dataConcat);
                customFields.setField6(ticket.getCiudadServicio()!=null?ticket.getCiudadServicio():"NA");
                contact.setCustomFields(customFields);
                contactsList.add(contact);
            }
        }
        contacts.setContacts(contactsList);
        Succeeded succeeded = surveyMonkeyService.createMultiContacts(contacts, contactListId);
        log.debug("succeeded:{}", succeeded);
        Utils.waitMilliSeconds(500);

        List<Contact> contactList = new ArrayList<>();
        log.info("succeeded.getExisting():{}",succeeded.getExisting());
        log.info("succeeded.getSucceeded(): {}",succeeded.getSucceeded());
        log.info("succeeded.getInvalid(): {}",succeeded.getInvalid());
        if (succeeded.getExisting().size() > 0) {
            contactList.addAll(succeeded.getExisting());
        } 
        if (succeeded.getSucceeded().size() > 0) {
            contactList.addAll(succeeded.getSucceeded());
        }

        ////String collectorId = Constant.COLLECTOR_RECLAMOS_TECNICOS;
        String collectorId= manageSurveyInMemory.getConfigCollector(ConstantDb.CONFIG_RECLAMOS_TECNICOS);
        String messageConfigId=manageSurveyInMemory.getConfigMessage(ConstantDb.CONFIG_RECLAMOS_TECNICOS);
        MessageResponse messageResponse = surveyMonkeyService.getCollectorMessages(collectorId, messageConfigId);
        ////MessageResponse messageResponse = surveyMonkeyService.getCollectorMessages(collectorId, Constant.RECLAMOS_TECNICOS_MESSAGE_ID);
        String messageId = messageResponse.getId();
        Utils.waitMilliSeconds(500);
       /////////////////////////////////////////////////////////////////////////
       List<String> list_contacts = new ArrayList<>();
       List<String> contacts_ids = new ArrayList<>();

       RecipientRequestList recipientRequestList = new RecipientRequestList();
       recipientRequestList.setContact_list_ids(list_contacts);
       for (Contact contact : contactList) {
           contacts_ids.add(contact.getId());
           manageLog.recordContactLog(Utils.fromContactToContactLog(contact, ConstantDb.CONFIG_RECLAMOS_TECNICOS));
       }
       //list_contacts.add(contactListId);
       recipientRequestList.setContact_list_ids(contacts_ids);

       RecipientRequest recipientRequest= new RecipientRequest();
       recipientRequest.setContact_list_ids(list_contacts.toArray(new String[list_contacts.size()]));
       recipientRequest.setContact_ids(contacts_ids.toArray(new String[contacts_ids.size()]));
       RecipientResponse recipientResponse= surveyMonkeyService.addRecipientBulk(recipientRequest,collectorId,messageId);
       log.info("{}",recipientResponse);
       manageLog.recordRecipientLog(recipientResponse,collectorId,messageId,ConstantDb.CONFIG_RECLAMOS_TECNICOS);
       ////////////////////////////////////////////////////////////////////////        
       SendSurveyRequest sendSurveyRequest = new SendSurveyRequest();
       // sendSurveyRequest.setScheduled_date("2024-03-30T09:30:00+00:00");
       sendSurveyRequest.setScheduled_date(Utils.getCurrentDateTimeString());
       SendSurveyResponse sendSurveyResponse = surveyMonkeyService.sendSurvey(sendSurveyRequest, collectorId,messageId);
       log.info("sendSurveyResponse:{}", sendSurveyResponse);
       Utils.waitMilliSeconds(500);
      
       return "executed";
    }

    public void procesoReclamosTecnicosCc() throws Exception {
        log.info("consumiendo fGetReclamosTecnicosCcDex");
        executeReclamosTecnicosCc();
        //axsService.fGetReclamosTecnicosCcDex();
        log.info("Datos del archivo json");

    }

    public String executeReclamosTecnicosCc() throws Exception {
        // 1.-Recupera los tickets del ws
        List<Ticket> reclamosTecnicos = axsService.fGetReclamosTecnicosCcDex();
        // 2.-Identifica el contact List id
        ////String contactListId = hashContactLists.get("axs-reclamos-cc");
        String contactListId = manageSurveyInMemory.getConfigContactList(ConstantDb.CONFIG_RECLAMOS_CALL_CENTER);
        log.info("axs-reclamos-cc contactListId={}=============", contactListId);
        // 3.-Adiciona los contactos a la lista de contactos en base los tickets
        Contacts contacts = new Contacts();
        List<Contact> contactsList = new ArrayList<>();
        for (Ticket ticket : reclamosTecnicos) {
            if(ticket.getEmail() != null || ticket.getEmailAlternativo()!= null ){
                String dataConcat =
                "*Area: " +(ticket.getArea()!=null?ticket.getArea():"NA")  + " " +
                "*Ciudad: " + (ticket.getCiudad()!=null?ticket.getCiudad():"NA") + " " +
                "*Síntoma: " + (ticket.getSintoma()!=null?ticket.getSintoma():"NA") + " " +
                "*Sub Area: " + (ticket.getSubArea()!=null?ticket.getSubArea():"NA") + " " +
                "*Creado Por: " + (ticket.getCreadoPor()!=null?ticket.getCreadoPor():"NA") + " " +
                "*Id Servicio: " + (ticket.getIdServicio()!=null?ticket.getIdServicio():"NA") + " " +
                "*Fecha Cierre: " + (ticket.getFechaCierre()!=null?ticket.getFechaCierre():"NA") + " " +
                "*Fecha Apertura: " + (ticket.getFechaApertura()!=null?ticket.getFechaApertura():"NA") + " " +
                "*Fecha Solución: " + (ticket.getFechaSolucion()!=null?ticket.getFechaSolucion():"NA") + " " +
                "*Dirección Instalación	: " + (ticket.getDireccionInstalacion()!=null?ticket.getDireccionInstalacion():"NA") + " " +
                "*Email Alternativo: " + (ticket.getEmailAlternativo()!=null?ticket.getEmailAlternativo():"NA");
                Contact contact = new Contact();
                contact.setEmail(ticket.getEmail() != null ? ticket.getEmail() : ticket.getEmailAlternativo());
                contact.setFirstName(ticket.getEmailOriginal()!=null?ticket.getEmailOriginal():"NA");
                contact.setLastName(ticket.getIdTicket()!=null?ticket.getIdTicket():"NA");
                CustomFields customFields = new CustomFields();
                customFields.setField1(ticket.getTelCelular()!=null?ticket.getTelCelular():"NA");
                customFields.setField2(ticket.getIdTicket()!=null?ticket.getIdTicket():"NA");
                customFields.setField3(ticket.getContrato()!=null?ticket.getContrato():"NA");
                customFields.setField4(ticket.getCodCliente()!=null?ticket.getCodCliente():"NA");
                customFields.setField5(dataConcat);
                customFields.setField6(ticket.getCiudadServicio()!=null?ticket.getCiudadServicio():"NA");
                contact.setCustomFields(customFields);
                contactsList.add(contact);
            }
        }
        contacts.setContacts(contactsList);
        Succeeded succeeded = surveyMonkeyService.createMultiContacts(contacts, contactListId);
        log.debug("succeeded:{}", succeeded);
        Utils.waitMilliSeconds(500);

        List<Contact> contactList = new ArrayList<>();
        log.info("succeeded.getExisting():{}",succeeded.getExisting());
        log.info("succeeded.getSucceeded(): {}",succeeded.getSucceeded());
        log.info("succeeded.getInvalid(): {}",succeeded.getInvalid());
        if (succeeded.getExisting().size() > 0) {
            contactList.addAll(succeeded.getExisting());
        } 
        if (succeeded.getSucceeded().size() > 0) {
            contactList.addAll(succeeded.getSucceeded());
        }

        ////String collectorId = Constant.COLLECTOR_RECLAMOS_CALL_CENTER;
        String collectorId= manageSurveyInMemory.getConfigCollector(ConstantDb.CONFIG_RECLAMOS_CALL_CENTER);
        String messageConfigId=manageSurveyInMemory.getConfigMessage(ConstantDb.CONFIG_RECLAMOS_CALL_CENTER);
        MessageResponse messageResponse = surveyMonkeyService.getCollectorMessages(collectorId, messageConfigId);
        ////MessageResponse messageResponse = surveyMonkeyService.getCollectorMessages(collectorId, Constant.RECLAMOS_CALL_CENTER_MESSAGE_ID);
        String messageId = messageResponse.getId();
        Utils.waitMilliSeconds(500);
       /////////////////////////////////////////////////////////////////////////
       List<String> list_contacts = new ArrayList<>();
       List<String> contacts_ids = new ArrayList<>();

       RecipientRequestList recipientRequestList = new RecipientRequestList();
       recipientRequestList.setContact_list_ids(list_contacts);
       for (Contact contact : contactList) {
           contacts_ids.add(contact.getId());
           manageLog.recordContactLog(Utils.fromContactToContactLog(contact, ConstantDb.CONFIG_RECLAMOS_CALL_CENTER));
       }
       //list_contacts.add(contactListId);
       recipientRequestList.setContact_list_ids(contacts_ids);

       RecipientRequest recipientRequest= new RecipientRequest();
       recipientRequest.setContact_list_ids(list_contacts.toArray(new String[list_contacts.size()]));
       recipientRequest.setContact_ids(contacts_ids.toArray(new String[contacts_ids.size()]));
       RecipientResponse recipientResponse= surveyMonkeyService.addRecipientBulk(recipientRequest,collectorId,messageId);
       log.info("{}",recipientResponse);
       manageLog.recordRecipientLog(recipientResponse,collectorId,messageId,ConstantDb.CONFIG_RECLAMOS_CALL_CENTER);
       ////////////////////////////////////////////////////////////////////////        
       SendSurveyRequest sendSurveyRequest = new SendSurveyRequest();
       // sendSurveyRequest.setScheduled_date("2024-03-30T09:30:00+00:00");
       sendSurveyRequest.setScheduled_date(Utils.getCurrentDateTimeString());
       SendSurveyResponse sendSurveyResponse = surveyMonkeyService.sendSurvey(sendSurveyRequest, collectorId,messageId);
       log.info("sendSurveyResponse:{}", sendSurveyResponse);
   
       return "executed";
    }

    public void procesoServicoAlClienteRemotoCc() throws Exception {
        log.info("consumiendo fGetExperienciaCallDex");
        executeServicoAlClienteRemotoCc();
       // axsService.fGetExperienciaCallDex();
    }

    public String executeServicoAlClienteRemotoCc() throws Exception {
        // 1.-Recupera los tickets del ws
        List<TicketServicoCliente> servicoAlClientePresencial = axsService.fGetExperienciaCallDex();
        // 2.-Identifica el contact List id
        ////String contactListId = hashContactLists.get("axs-sc-remoto-cc");
        String contactListId = manageSurveyInMemory.getConfigContactList(ConstantDb.CONFIG_SRCIO_AL_CLI_REMO_CC);
        log.info("axs-sc-remoto-cc contactListId={}=============", contactListId);
        // 3.-Adiciona los contactos a la lista de contactos en base los tickets
        Contacts contacts = new Contacts();
        List<Contact> contactsList = new ArrayList<>();
        for (TicketServicoCliente ticket : servicoAlClientePresencial) {
            if(ticket.getEmail() != null || ticket.getEmailAlternativo()!= null ){
                String dataConcat =
                "*Id Contacto: " +(ticket.getIdContacto()!=null?ticket.getIdContacto():"NA") + " " +
                "*Area: " +(ticket.getArea()!=null?ticket.getArea():"NA")  + " " +
                "*Sub Area: " + (ticket.getSubArea()!=null?ticket.getSubArea():"NA") + " " +
                "*Síntoma: " + (ticket.getSintoma()!=null?ticket.getSintoma():"NA") + " " +
                "*Creado Por: " + (ticket.getCreadoPor()!=null?ticket.getCreadoPor():"NA") + " " +
                "*Area Creador: " + (ticket.getAreaCreador()!=null?ticket.getAreaCreador():"NA") + " " +
                "*Fecha Apertura: " + (ticket.getFechaApertura()!=null?ticket.getFechaApertura():"NA") + " " +
                "*Fecha Solución: " + (ticket.getFechaSolucion()!=null?ticket.getFechaSolucion():"NA") + " " +
                "*Fecha Cierre: " + (ticket.getFechaCierre()!=null?ticket.getFechaCierre():"NA") + " " +
                "*Email Alternativo : " + (ticket.getEmailAlternativo()!=null?ticket.getEmailAlternativo():"NA") + " " +
                "*Teléfono Celular : " + (ticket.getTelCelular()!=null?ticket.getTelCelular():"NA");

            Contact contact = new Contact();
                contact.setEmail(ticket.getEmail() != null ? ticket.getEmail() : ticket.getEmailAlternativo());
                contact.setFirstName(ticket.getEmailOriginal()!=null?ticket.getEmailOriginal():"NA");
                contact.setLastName(ticket.getIdTicket()!=null?ticket.getIdTicket():"NA");
                CustomFields customFields = new CustomFields();
                customFields.setField1(ticket.getIdTicket()!=null?ticket.getIdTicket():"NA");
                customFields.setField2(ticket.getCodCliente()!=null?ticket.getCodCliente():"NA");
                customFields.setField3(ticket.getIdServicio()!=null?ticket.getIdServicio():"NA");
                customFields.setField4(ticket.getContrato()!=null?ticket.getContrato():"NA");
                customFields.setField5(dataConcat);
                customFields.setField6(ticket.getCiudadServicio()!=null?ticket.getCiudadServicio():"NA");
                contact.setCustomFields(customFields);
                contactsList.add(contact);
            }
        }
        contacts.setContacts(contactsList);
        Succeeded succeeded = surveyMonkeyService.createMultiContacts(contacts, contactListId);
        log.debug("succeeded:{}", succeeded);
        Utils.waitMilliSeconds(500);

        List<Contact> contactList = new ArrayList<>();
        log.info("succeeded.getExisting():{}",succeeded.getExisting());
        log.info("succeeded.getSucceeded(): {}",succeeded.getSucceeded());
        log.info("succeeded.getInvalid(): {}",succeeded.getInvalid());
        if (succeeded.getExisting().size() > 0) {
            contactList.addAll(succeeded.getExisting());
        } 
        if (succeeded.getSucceeded().size() > 0) {
            contactList.addAll(succeeded.getSucceeded());
        }

        ////String collectorId = Constant.COLLECTOR_SRCIO_AL_CLI_REMO_CC;
        String collectorId= manageSurveyInMemory.getConfigCollector(ConstantDb.CONFIG_SRCIO_AL_CLI_REMO_CC);
        String messageConfigId=manageSurveyInMemory.getConfigMessage(ConstantDb.CONFIG_SRCIO_AL_CLI_REMO_CC);
        MessageResponse messageResponse = surveyMonkeyService.getCollectorMessages(collectorId,messageConfigId);
        ////MessageResponse messageResponse = surveyMonkeyService.getCollectorMessages(collectorId, Constant.SRCIO_AL_CLI_REMO_CC_MESSAGE_ID);
        
        String messageId = messageResponse.getId();
        Utils.waitMilliSeconds(500);
       /////////////////////////////////////////////////////////////////////////
       List<String> list_contacts = new ArrayList<>();
       List<String> contacts_ids = new ArrayList<>();

       RecipientRequestList recipientRequestList = new RecipientRequestList();
       recipientRequestList.setContact_list_ids(list_contacts);
       for (Contact contact : contactList) {
           contacts_ids.add(contact.getId());
           manageLog.recordContactLog(Utils.fromContactToContactLog(contact, ConstantDb.CONFIG_SRCIO_AL_CLI_REMO_CC));
       }
       //list_contacts.add(contactListId);
       recipientRequestList.setContact_list_ids(contacts_ids);

       RecipientRequest recipientRequest= new RecipientRequest();
       recipientRequest.setContact_list_ids(list_contacts.toArray(new String[list_contacts.size()]));
       recipientRequest.setContact_ids(contacts_ids.toArray(new String[contacts_ids.size()]));
       RecipientResponse recipientResponse= surveyMonkeyService.addRecipientBulk(recipientRequest,collectorId,messageId);
       log.info("{}",recipientResponse);
       manageLog.recordRecipientLog(recipientResponse,collectorId,messageId,ConstantDb.CONFIG_SRCIO_AL_CLI_REMO_CC);
       ////////////////////////////////////////////////////////////////////////        
         SendSurveyRequest sendSurveyRequest = new SendSurveyRequest();
        // sendSurveyRequest.setScheduled_date("2024-03-30T09:30:00+00:00");
        sendSurveyRequest.setScheduled_date(Utils.getCurrentDateTimeString());
        SendSurveyResponse sendSurveyResponse = surveyMonkeyService.sendSurvey(sendSurveyRequest, collectorId,messageId);
        log.info("{}", sendSurveyResponse);
        Utils.waitMilliSeconds(500);
      
        return "executed";
    }
    //========================================================
    public void procesoInstalacion() throws Exception {
        log.info("Consumiendo servicio instalacion");
        executeInstalacion();
        //List<Contrato> instalacion = axsService.instalacion();
        System.out.println("Data del archivo json");
    }

    public String executeInstalacion() throws Exception {
        // 1.-Recupera los tickets del ws
        List<Contrato> instalacion = axsService.instalacion();
        // 2.-Identifica el contact List id
        ////String contactListId = hashContactLists.get("axs-instalacion");
        String contactListId = manageSurveyInMemory.getConfigContactList(ConstantDb.CONFIG_INSTALACION);
        log.info("axs-instalacion contactListId={}=============", contactListId);
        // 3.-Adiciona los contactos a la lista de contactos en base los tickets
        Contacts contacts = new Contacts();
        List<Contact> contactsList = new ArrayList<>();
        for (Contrato contrato : instalacion) {
            if(contrato.getContactoEmail() != null || contrato.getEmailAlternativoContacto()!= null ){
                Contact contact = new Contact();
                contact.setEmail(contrato.getContactoEmail() != null ? contrato.getContactoEmail() : contrato.getEmailAlternativoContacto());
                contact.setFirstName(contrato.getEmailOriginal()!=null?contrato.getEmailOriginal():"NA");
                contact.setLastName(contrato.getContrato()!=null?contrato.getContrato():"NA");
                CustomFields customFields = new CustomFields();
                customFields.setField1(contrato.getIdServicio()!=null?contrato.getIdServicio():"NA");
                customFields.setField2(contrato.getCiudad()!=null?contrato.getCiudad():"NA");
                customFields.setField3(contrato.getInstancia()!=null?contrato.getInstancia():"NA");
                customFields.setField4(contrato.getPeriodoInstalacion()!=null?contrato.getPeriodoInstalacion():"NA");
                customFields.setField5(contrato.getVendedor()!=null?contrato.getVendedor():"NA");
                customFields.setField6(contrato.getCiudadVendedor()!=null?contrato.getCiudadVendedor():"NA");

                customFields.setField7(contrato.getCanalVendedor()!=null?contrato.getCanalVendedor():"NA");
                customFields.setField8(contrato.getFechaInicio()!=null?contrato.getFechaInicio():"NA");
                customFields.setField9(contrato.getEmailAlternativoContacto()!=null?contrato.getEmailAlternativoContacto():"NA");
                customFields.setField10(contrato.getTelefonoCelular()!=null?contrato.getTelefonoCelular():"NA");
                customFields.setField11(contrato.getContrato()!=null?contrato.getContrato():"NA");
                customFields.setField12(contrato.getCiudadServicio()!=null?contrato.getCiudadServicio():"NA");
                customFields.setField13(contrato.getTecnicoInstalacion()!=null?contrato.getTecnicoInstalacion():"NA");
                customFields.setField14("NA");
                customFields.setField15("NA");
                customFields.setField16("NA");
                customFields.setField17("NA");
                customFields.setField18("NA");
                customFields.setField19("NA");
                customFields.setField20("NA");
                contact.setCustomFields(customFields);
                contactsList.add(contact);
            }
        }
        contacts.setContacts(contactsList);
        Succeeded succeeded = surveyMonkeyService.createMultiContacts(contacts, contactListId);
        log.debug("succeeded:{}", succeeded);
        Utils.waitMilliSeconds(500);

        List<Contact> contactList = new ArrayList<>();
        log.info("succeeded.getExisting():{}",succeeded.getExisting());
        log.info("succeeded.getSucceeded(): {}",succeeded.getSucceeded());
        log.info("succeeded.getInvalid(): {}",succeeded.getInvalid());
        if (succeeded.getExisting().size() > 0) {
            contactList.addAll(succeeded.getExisting());
        } 
        if (succeeded.getSucceeded().size() > 0) {
            contactList.addAll(succeeded.getSucceeded());
        }

        ////String collectorId = Constant.COLLECTOR_INSTALACION;
        String collectorId= manageSurveyInMemory.getConfigCollector(ConstantDb.CONFIG_INSTALACION);
        String messageConfigId=manageSurveyInMemory.getConfigMessage(ConstantDb.CONFIG_INSTALACION);
        MessageResponse messageResponse = surveyMonkeyService.getCollectorMessages(collectorId,messageConfigId);
        ////MessageResponse messageResponse = surveyMonkeyService.getCollectorMessages(collectorId, Constant.INSTALACION_MESSAGE_ID);
        String messageId = messageResponse.getId();
        Utils.waitMilliSeconds(500);
       /////////////////////////////////////////////////////////////////////////
       List<String> list_contacts = new ArrayList<>();
       List<String> contacts_ids = new ArrayList<>();

       RecipientRequestList recipientRequestList = new RecipientRequestList();
       recipientRequestList.setContact_list_ids(list_contacts);
       for (Contact contact : contactList) {
           contacts_ids.add(contact.getId());
           manageLog.recordContactLog(Utils.fromContactToContactLog(contact, ConstantDb.CONFIG_INSTALACION));
       }
       //list_contacts.add(contactListId);
       recipientRequestList.setContact_list_ids(contacts_ids);

       RecipientRequest recipientRequest= new RecipientRequest();
       recipientRequest.setContact_list_ids(list_contacts.toArray(new String[list_contacts.size()]));
       recipientRequest.setContact_ids(contacts_ids.toArray(new String[contacts_ids.size()]));
       RecipientResponse recipientResponse= surveyMonkeyService.addRecipientBulk(recipientRequest,collectorId,messageId);
       log.info("{}",recipientResponse);
       manageLog.recordRecipientLog(recipientResponse,collectorId,messageId,ConstantDb.CONFIG_INSTALACION);
       ////////////////////////////////////////////////////////////////////////        
        SendSurveyRequest sendSurveyRequest = new SendSurveyRequest();
        // sendSurveyRequest.setScheduled_date("2024-03-30T09:30:00+00:00");
        sendSurveyRequest.setScheduled_date(Utils.getCurrentDateTimeString());
        SendSurveyResponse sendSurveyResponse = surveyMonkeyService.sendSurvey(sendSurveyRequest, collectorId,messageId);
        log.info("{}", sendSurveyResponse);
        Utils.waitMilliSeconds(500);
 
        return "executed";
    }

    public void procesoPreVenta() throws Exception {
        log.info("Consumiendo servicio preventa");
        executePreVenta();
        //List<Contrato> instalacion = axsService.preVenta();
        System.out.println("Data del archivo de preventa json");
    }

    public String executePreVenta() throws Exception {
        // 1.-Recupera los tickets del ws
        List<Contrato> instalacion = axsService.preVenta(); //actualmente datos de json
        // 2.-Identifica el contact List id
        ////String contactListId = hashContactLists.get("axs-instalacion");
        String contactListId = manageSurveyInMemory.getConfigContactList(ConstantDb.PREVENTA);
        log.info("axs-preventa contactListId={}=============", contactListId);
        // 3.-Adiciona los contactos a la lista de contactos en base los tickets
        Contacts contacts = new Contacts();
        List<Contact> contactsList = new ArrayList<>();
        for (Contrato contrato : instalacion) {
            if(contrato.getContactoEmail() != null || contrato.getEmailAlternativoContacto()!= null ){
                Contact contact = new Contact();
                contact.setEmail(contrato.getContactoEmail() != null ? contrato.getContactoEmail() : contrato.getEmailAlternativoContacto());
                contact.setFirstName(contrato.getEmailOriginal()!=null?contrato.getEmailOriginal():"NA");
                contact.setLastName(contrato.getContrato()!=null?contrato.getContrato():"NA");
                CustomFields customFields = new CustomFields();
                customFields.setField1(contrato.getIdServicio()!=null?contrato.getIdServicio():"NA");
                customFields.setField2(contrato.getCiudad()!=null?contrato.getCiudad():"NA");
                customFields.setField3(contrato.getInstancia()!=null?contrato.getInstancia():"NA");
                customFields.setField4(contrato.getPeriodoInstalacion()!=null?contrato.getPeriodoInstalacion():"NA");
                customFields.setField5(contrato.getVendedor()!=null?contrato.getVendedor():"NA");
                customFields.setField6(contrato.getCiudadVendedor()!=null?contrato.getCiudadVendedor():"NA");

                customFields.setField7(contrato.getCanalVendedor()!=null?contrato.getCanalVendedor():"NA");
                customFields.setField8(contrato.getFechaInicio()!=null?contrato.getFechaInicio():"NA");
                customFields.setField9(contrato.getEmailAlternativoContacto()!=null?contrato.getEmailAlternativoContacto():"NA");
                customFields.setField10(contrato.getTelefonoCelular()!=null?contrato.getTelefonoCelular():"NA");
                customFields.setField11(contrato.getContrato()!=null?contrato.getContrato():"NA");
                customFields.setField12(contrato.getCiudadServicio()!=null?contrato.getCiudadServicio():"NA");
                customFields.setField13(contrato.getTecnicoInstalacion()!=null?contrato.getTecnicoInstalacion():"NA");
                customFields.setField14(contrato.getIdVendedor()!=null?contrato.getIdVendedor():"NA");
                customFields.setField15("NA");
                customFields.setField16("NA");
                customFields.setField17("NA");
                customFields.setField18("NA");
                customFields.setField19("NA");
                customFields.setField20("NA");
                contact.setCustomFields(customFields);
                contactsList.add(contact);
            }
        }
        contacts.setContacts(contactsList);
        Succeeded succeeded = surveyMonkeyService.createMultiContacts(contacts, contactListId);
        log.debug("succeeded:{}", succeeded);
        Utils.waitMilliSeconds(500);

        List<Contact> contactList = new ArrayList<>();
        log.info("succeeded.getExisting():{}",succeeded.getExisting());
        log.info("succeeded.getSucceeded(): {}",succeeded.getSucceeded());
        log.info("succeeded.getInvalid(): {}",succeeded.getInvalid());
        if (succeeded.getExisting().size() > 0) {
            contactList.addAll(succeeded.getExisting());
        } 
        if (succeeded.getSucceeded().size() > 0) {
            contactList.addAll(succeeded.getSucceeded());
        }

        ////String collectorId = Constant.COLLECTOR_INSTALACION;
        String collectorId= manageSurveyInMemory.getConfigCollector(ConstantDb.PREVENTA);
        String messageConfigId=manageSurveyInMemory.getConfigMessage(ConstantDb.PREVENTA);
        MessageResponse messageResponse = surveyMonkeyService.getCollectorMessages(collectorId,messageConfigId);
        ////MessageResponse messageResponse = surveyMonkeyService.getCollectorMessages(collectorId, Constant.INSTALACION_MESSAGE_ID);
        String messageId = messageResponse.getId();
        Utils.waitMilliSeconds(500);
       /////////////////////////////////////////////////////////////////////////
       List<String> list_contacts = new ArrayList<>();
       List<String> contacts_ids = new ArrayList<>();

       RecipientRequestList recipientRequestList = new RecipientRequestList();
       recipientRequestList.setContact_list_ids(list_contacts);
       for (Contact contact : contactList) {
           contacts_ids.add(contact.getId());
           manageLog.recordContactLog(Utils.fromContactToContactLog(contact, ConstantDb.PREVENTA));
       }
       //list_contacts.add(contactListId);
       recipientRequestList.setContact_list_ids(contacts_ids);

       RecipientRequest recipientRequest= new RecipientRequest();
       recipientRequest.setContact_list_ids(list_contacts.toArray(new String[list_contacts.size()]));
       recipientRequest.setContact_ids(contacts_ids.toArray(new String[contacts_ids.size()]));
       RecipientResponse recipientResponse= surveyMonkeyService.addRecipientBulk(recipientRequest,collectorId,messageId);
       manageLog.recordRecipientLog(recipientResponse,collectorId,messageId,ConstantDb.PREVENTA);
       log.info("{}",recipientResponse);
       ////////////////////////////////////////////////////////////////////////        
      /*  SendSurveyRequest sendSurveyRequest = new SendSurveyRequest();
        // sendSurveyRequest.setScheduled_date("2024-03-30T09:30:00+00:00");
        sendSurveyRequest.setScheduled_date(Utils.getCurrentDateTimeString());
        SendSurveyResponse sendSurveyResponse = surveyMonkeyService.sendSurvey(sendSurveyRequest, collectorId,messageId);
        log.info("{}", sendSurveyResponse);
        Utils.waitMilliSeconds(500);
      */
        return "executed";
    }

    //========================================================
    //========================================================
    public void procesoInstalacionVenta() throws Exception {
        log.info("Consumiendo servicio instalacion-venta");
        executeInstalacionVenta();
    }

    public String executeInstalacionVenta() throws Exception {
        // 1.-Recupera los tickets del ws
        List<Contrato> instalacion = axsService.instalacion_venta();
        // 2.-Identifica el contact List id
        ////String contactListId = hashContactLists.get("axs-instalacion");
        String contactListId = manageSurveyInMemory.getConfigContactList(ConstantDb.CONFIG_INSTALACION_VENTA);
        log.info("axs-instalacion-vwnr contactListId={}=============", contactListId);
        // 3.-Adiciona los contactos a la lista de contactos en base los tickets
        Contacts contacts = new Contacts();
        List<Contact> contactsList = new ArrayList<>();
        for (Contrato contrato : instalacion) {
            if(contrato.getContactoEmail() != null || contrato.getEmailAlternativoContacto()!= null ){
                String dataConcat = "*Vendedor: " + (contrato.getVendedor() != null ? contrato.getVendedor() : "NA") + " " +
                    "*Ciudad Vendedor: " + (contrato.getCiudadVendedor() != null ? contrato.getCiudadVendedor() : "NA") + " " +
                    "*Canal Vendedor: " + (contrato.getCanalVendedor() != null ? contrato.getCanalVendedor() : "NA") + " " +
                    "*Fecha Inicio: " + (contrato.getFechaInicio() != null ? contrato.getFechaInicio() : "NA") + " " +
                    "*Email Alternativo: " + (contrato.getEmailAlternativoContacto() != null ? contrato.getEmailAlternativoContacto() : "NA") + " " +
                    "*Teléfono Celular: " + (contrato.getTelefonoCelular() != null ? contrato.getTelefonoCelular() : "NA") + " " +
                    "*Contrato: " + (contrato.getContrato() != null ? contrato.getContrato() : "NA") + " " +
                    "*Ciudad Vendedor: " + (contrato.getCiudadVendedor() != null ? contrato.getCiudadVendedor() : "NA") + " " +
                    "*Técnico Instalación: " + (contrato.getTecnicoInstalacion() != null ? contrato.getTecnicoInstalacion() : "NA");
                    log.info("dataConcat:*********************************** "+dataConcat);
                Contact contact = new Contact();
                contact.setEmail(contrato.getContactoEmail() != null ? contrato.getContactoEmail() : contrato.getEmailAlternativoContacto());
                contact.setFirstName(contrato.getEmailOriginal()!=null?contrato.getEmailOriginal():"NA");
                contact.setLastName(contrato.getContrato()!=null?contrato.getContrato():"NA");
                CustomFields customFields = new CustomFields();
                customFields.setField1(contrato.getIdServicio()!=null?contrato.getIdServicio():"NA");
                customFields.setField2(contrato.getCiudad()!=null?contrato.getCiudad():"NA");
                customFields.setField3(contrato.getInstancia()!=null?contrato.getInstancia():"NA");
                customFields.setField4(contrato.getPeriodoInstalacion()!=null?contrato.getPeriodoInstalacion():"NA");
                customFields.setField5(dataConcat);
                customFields.setField6(contrato.getCiudadServicio()!=null?contrato.getCiudadServicio():"NA");
                contact.setCustomFields(customFields);
                contactsList.add(contact);
            }
        }
        contacts.setContacts(contactsList);
        Succeeded succeeded = surveyMonkeyService.createMultiContacts(contacts, contactListId);
        log.debug("succeeded:{}", succeeded);
        Utils.waitMilliSeconds(500);

        List<Contact> contactList = new ArrayList<>();
        log.info("succeeded.getExisting():{}",succeeded.getExisting());
        log.info("succeeded.getSucceeded(): {}",succeeded.getSucceeded());
        log.info("succeeded.getInvalid(): {}",succeeded.getInvalid());
        if (succeeded.getExisting().size() > 0) {
            contactList.addAll(succeeded.getExisting());
        }
        if (succeeded.getSucceeded().size() > 0) {
            contactList.addAll(succeeded.getSucceeded());
        }

        String collectorId= manageSurveyInMemory.getConfigCollector(ConstantDb.CONFIG_INSTALACION_VENTA);
        String messageConfigId=manageSurveyInMemory.getConfigMessage(ConstantDb.CONFIG_INSTALACION_VENTA);
        MessageResponse messageResponse = surveyMonkeyService.getCollectorMessages(collectorId,messageConfigId);
        ////MessageResponse messageResponse = surveyMonkeyService.getCollectorMessages(collectorId, Constant.INSTALACION_MESSAGE_ID);
        String messageId = messageResponse.getId();
        Utils.waitMilliSeconds(500);
        /////////////////////////////////////////////////////////////////////////
        List<String> list_contacts = new ArrayList<>();
        List<String> contacts_ids = new ArrayList<>();

        RecipientRequestList recipientRequestList = new RecipientRequestList();
        recipientRequestList.setContact_list_ids(list_contacts);
        for (Contact contact : contactList) {
            contacts_ids.add(contact.getId());
            manageLog.recordContactLog(Utils.fromContactToContactLog(contact, ConstantDb.CONFIG_INSTALACION_VENTA));
        }
        //list_contacts.add(contactListId);
        recipientRequestList.setContact_list_ids(contacts_ids);

        RecipientRequest recipientRequest= new RecipientRequest();
        recipientRequest.setContact_list_ids(list_contacts.toArray(new String[list_contacts.size()]));
        recipientRequest.setContact_ids(contacts_ids.toArray(new String[contacts_ids.size()]));
        RecipientResponse recipientResponse= surveyMonkeyService.addRecipientBulk(recipientRequest,collectorId,messageId);
        log.info("{}",recipientResponse);
        manageLog.recordRecipientLog(recipientResponse,collectorId,messageId,ConstantDb.CONFIG_INSTALACION_VENTA);
        ////////////////////////////////////////////////////////////////////////
        SendSurveyRequest sendSurveyRequest = new SendSurveyRequest();
        // sendSurveyRequest.setScheduled_date("2024-03-30T09:30:00+00:00");
        sendSurveyRequest.setScheduled_date(Utils.getCurrentDateTimeString());
        SendSurveyResponse sendSurveyResponse = surveyMonkeyService.sendSurvey(sendSurveyRequest, collectorId,messageId);
        log.info("{}", sendSurveyResponse);
        Utils.waitMilliSeconds(500);

        return "executed";
    }
}
