package com.keysolbo.axsservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.keysolbo.axsservice.model.survey.ArecipientRequest;
import com.keysolbo.axsservice.model.survey.ArecipientResponse;
import com.keysolbo.axsservice.model.survey.CollectorMessageRequest;
import com.keysolbo.axsservice.model.survey.CollectorMessageResponse;
import com.keysolbo.axsservice.model.survey.CollectorType;
import com.keysolbo.axsservice.model.survey.CollectorTypeResponse;
import com.keysolbo.axsservice.model.survey.ContactListRequest;
import com.keysolbo.axsservice.model.survey.ContactListResponse;
import com.keysolbo.axsservice.model.survey.Contacts;
import com.keysolbo.axsservice.model.survey.GetCollectorsResponse;
import com.keysolbo.axsservice.model.survey.GetContactListsResponse;
import com.keysolbo.axsservice.model.survey.MessageRequest;
import com.keysolbo.axsservice.model.survey.MessageResponse;
import com.keysolbo.axsservice.model.survey.RecipientRequest;
import com.keysolbo.axsservice.model.survey.RecipientResponse;
import com.keysolbo.axsservice.model.survey.SendSurveyRequest;
import com.keysolbo.axsservice.model.survey.SendSurveyResponse;
import com.keysolbo.axsservice.model.survey.Succeeded;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class SurveyMonkeyService {
    @Value("${surveymonkey.service.urlbase}")
    private String surveyMonkeyUrlBase;
    @Value("${surveymonkey.service.contactlists}")
    private String contactListUrl;
    @Value("${surveymonkey.service.contactsbulk}")
    private String contactsBulkUrl;
    @Value("${surveymonkey.service.collector}")
    private String createCollectorUrl;
    @Value("${surveymonkey.service.collectormsg}")
    private String createCollectorMsgUrl;
    @Value("${surveymonkey.service.recipient}")
    private String addRecipientUrl;
    @Value("${surveymonkey.service.sendsurvey}")
    private String sendSurveyUrl;
    @Value("${surveymonkey.service.arecipient}")
    private String arecipientUrl;
    

    @Value("${surveymonkey.service.token}")
    private String surveyMonkytoken;
    @Autowired
    private WebClient webClientApi;

    public ContactListResponse createContactList(String contactListName) {
        log.info("contactListName: " + contactListName);
        ContactListRequest contactListRequest = new ContactListRequest();
        contactListRequest.setName(contactListName);
        ContactListResponse contactListResponse = new ContactListResponse();
        try {

            contactListResponse = webClientApi
                    .post()
                    .uri(contactListUrl)
                    .header(HttpHeaders.AUTHORIZATION, surveyMonkytoken)
                    .body(Mono.just(contactListRequest), ContactListRequest.class)
                    .retrieve()
                    .bodyToMono(ContactListResponse.class)
                    .block();
            log.info("contactListResponse: {}", contactListResponse);

        } catch (WebClientResponseException e) {
            // Capturar errores relacionados con la respuesta del servidor
            log.error("Error al consumir el servicio. Código de error: {}", e.getRawStatusCode());
            log.error("Respuesta del servidor: {}", e.getResponseBodyAsString());
            throw e;
        } catch (Exception e) {
            // Capturar otros errores inesperados
            log.error("Error inesperado al consumir el servicio", e);
            throw e;
        }

        return contactListResponse;

    }

    public Succeeded createMultiContacts(Contacts contacts, String contactListId) {
        Succeeded succeeded = new Succeeded();
        log.info("contacts: " + contacts);
        try {

            succeeded = webClientApi
                    .post()
                    .uri(uriBuilder -> uriBuilder.path(contactsBulkUrl).build(contactListId))
                    .header(HttpHeaders.AUTHORIZATION, surveyMonkytoken)
                    .body(Mono.just(contacts), Contacts.class)
                    .retrieve()
                    .bodyToMono(Succeeded.class)
                    .block();
        } catch (WebClientResponseException e) {
            // Capturar errores relacionados con la respuesta del servidor
            log.error("Error al consumir el servicio. Código de error: {}", e.getRawStatusCode());
            log.error("Respuesta del servidor: {}", e.getResponseBodyAsString());
            throw e;
        } catch (Exception e) {
            // Capturar otros errores inesperados
            log.error("Error inesperado al consumir el servicio", e);
            throw e;
        }
        return succeeded;
    }

    public CollectorTypeResponse createSurveyCollector(CollectorType collectorType, String surveyId) {
        CollectorTypeResponse collectorTypeResponse = new CollectorTypeResponse();
        log.info("surveyId: " + surveyId);
        log.info("collectorType: " + collectorType);
        try {

            collectorTypeResponse = webClientApi
                    .post()
                    .uri(uriBuilder -> uriBuilder.path(createCollectorUrl).build(surveyId))
                    .header(HttpHeaders.AUTHORIZATION, surveyMonkytoken)
                    .body(Mono.just(collectorType), CollectorType.class)
                    .retrieve()
                    .bodyToMono(CollectorTypeResponse.class)
                    .block();
            log.info("succeeded: {}", collectorTypeResponse);

        } catch (WebClientResponseException e) {
            // Capturar errores relacionados con la respuesta del servidor
            log.error("Error al consumir el servicio. Código de error: {}", e.getRawStatusCode());
            log.error("Respuesta del servidor: {}", e.getResponseBodyAsString());
            throw e;
        } catch (Exception e) {
            // Capturar otros errores inesperados
            log.error("Error inesperado al consumir el servicio", e);
            throw e;
        }
        return collectorTypeResponse;
    }

    public CollectorMessageResponse createCollectorMessage(CollectorMessageRequest collectorMessageRequest,
            String collectorId) {
        CollectorMessageResponse collectorMessageResponse = new CollectorMessageResponse();
        log.info("surveyId: " + collectorId);
        log.info("collectorMessageRequest: " + collectorMessageRequest);
        try {

            collectorMessageResponse = webClientApi
                    .post()
                    .uri(uriBuilder -> uriBuilder.path(createCollectorMsgUrl).build(collectorId))
                    .header(HttpHeaders.AUTHORIZATION, surveyMonkytoken)
                    .body(Mono.just(collectorMessageRequest), CollectorMessageRequest.class)
                    .retrieve()
                    .bodyToMono(CollectorMessageResponse.class)
                    .block();
            log.info("collectorMessageResponse: {}", collectorMessageResponse);

        } catch (WebClientResponseException e) {
            // Capturar errores relacionados con la respuesta del servidor
            log.error("Error al consumir el servicio. Código de error: {}", e.getRawStatusCode());
            log.error("Respuesta del servidor: {}", e.getResponseBodyAsString());
            throw e;
        } catch (Exception e) {
            // Capturar otros errores inesperados
            log.error("Error inesperado al consumir el servicio", e);
            throw e;
        }
        return collectorMessageResponse;
    }

    public RecipientResponse addRecipientBulk(RecipientRequest recipientRequest, String collectorId, String messageId) {
        RecipientResponse recipientResponse = new RecipientResponse();
        log.info("recipientRequest: " + recipientRequest);
        log.info("collectorId: " + collectorId);
        log.info("messageId: " + messageId);
        try {

            recipientResponse = webClientApi
                    .post()
                    .uri(uriBuilder -> uriBuilder.path(addRecipientUrl).build(collectorId, messageId))
                    .header(HttpHeaders.AUTHORIZATION, surveyMonkytoken)
                    .body(Mono.just(recipientRequest), RecipientRequest.class)
                    .retrieve()
                    .bodyToMono(RecipientResponse.class)
                    .block();
            log.info("recipientResponse: {}", recipientResponse);

        } catch (WebClientResponseException e) {
            // Capturar errores relacionados con la respuesta del servidor
            log.error("Error al consumir el servicio. Código de error: {}", e.getRawStatusCode());
            log.error("Respuesta del servidor: {}", e.getResponseBodyAsString());
            throw e;
        } catch (Exception e) {
            // Capturar otros errores inesperados
            log.error("Error inesperado al consumir el servicio", e);
            throw e;
        }
        return recipientResponse;
    }

    public SendSurveyResponse sendSurvey(SendSurveyRequest sendSurveyRequest, String collectorId, String messageId) {
        SendSurveyResponse sendSurveyResponse = new SendSurveyResponse();
        log.info("sendSurveyRequest: " + sendSurveyRequest);
        log.info("collectorId: " + collectorId);
        log.info("messageId: " + messageId);
        try {

            sendSurveyResponse = webClientApi
                    .post()
                    .uri(uriBuilder -> uriBuilder.path(sendSurveyUrl).build(collectorId, messageId))
                    .header(HttpHeaders.AUTHORIZATION, surveyMonkytoken)
                    .body(Mono.just(sendSurveyRequest), SendSurveyRequest.class)
                    .retrieve()
                    .bodyToMono(SendSurveyResponse.class)
                    .block();
            log.info("sendSurveyResponse: {}", sendSurveyResponse);

        } catch (WebClientResponseException e) {
            // Capturar errores relacionados con la respuesta del servidor
            log.error("Error al consumir el servicio. Código de error: {}", e.getRawStatusCode());
            log.error("Respuesta del servidor: {}", e.getResponseBodyAsString());
            throw e;
        } catch (Exception e) {
            // Capturar otros errores inesperados
            log.error("Error inesperado al consumir el servicio", e);
            throw e;
        }
        return sendSurveyResponse;
    }

   public GetContactListsResponse getContactList() {
        GetContactListsResponse contactListResponse;
        try {
            Flux<GetContactListsResponse> contactListFlux = webClientApi
                    .get()
                    .uri(contactListUrl)
                    .accept(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, surveyMonkytoken)
                    .retrieve()
                    .bodyToFlux(GetContactListsResponse.class);

            contactListResponse = contactListFlux.blockFirst();

            log.debug("contactListResponse: {}", contactListResponse);
        } catch (WebClientResponseException e) {
            // Capturar errores relacionados con la respuesta del servidor
            log.error("Error al consumir el servicio. Código de error: {}", e.getRawStatusCode());
            log.error("Respuesta del servidor: {}", e.getResponseBodyAsString());
            throw e;
        } catch (Exception e) {
            // Capturar otros errores inesperados
            log.error("Error inesperado al consumir el servicio", e);
            throw e;
        }
        return contactListResponse;
    }

    public GetCollectorsResponse getCollectors(String surveyId) {
        GetCollectorsResponse collectorsResponse;
        try {
            Flux<GetCollectorsResponse> collectorsMono = webClientApi.get()
                    .uri(createCollectorUrl, surveyId)
                    .accept(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, surveyMonkytoken)
                    .retrieve()
                    .bodyToFlux(GetCollectorsResponse.class);

            collectorsResponse = collectorsMono.blockFirst();

            log.info("collectorsResponse: {}", collectorsResponse);
        } catch (WebClientResponseException e) {
            // Capturar errores relacionados con la respuesta del servidor
            log.error("Error al consumir el servicio. Código de error: {}", e.getRawStatusCode());
            log.error("Respuesta del servidor: {}", e.getResponseBodyAsString());
            throw e;
        } catch (Exception e) {
            // Capturar otros errores inesperados
            log.error("Error inesperado al consumir el servicio", e);
            throw e;
        }
        return collectorsResponse;
    }

        public MessageResponse getCollectorMessages(String collectorId,String fromMessageId) {
        try {
            MessageRequest request = new MessageRequest(collectorId, fromMessageId, false);
            log.info("{} : ",request);

            return webClientApi.post()
                    .uri(createCollectorMsgUrl, collectorId)
                    .accept(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION,surveyMonkytoken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(request))
                    .retrieve()
                    .bodyToMono(MessageResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            // Capturar errores relacionados con la respuesta del servidor
            log.error("Error al consumir el servicio. Código de error: {}", e.getRawStatusCode());
            log.error("Respuesta del servidor: {}", e.getResponseBodyAsString());
            throw e;
        } catch (Exception e) {
            // Capturar otros errores inesperados
            log.error("Error inesperado al consumir el servicio", e);
            throw e;
        }
    }

    public ArecipientResponse addNewMessage(String collectorId, String messageId, String contactId) {
        try {
            // Construir el cuerpo de la solicitud
            ArecipientRequest request = new ArecipientRequest(contactId);

            // Realizar la llamada al servicio
            return webClientApi.post()
                    .uri(arecipientUrl, collectorId, messageId)
                    .header(HttpHeaders.AUTHORIZATION, surveyMonkytoken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(request))
                    .retrieve()
                    .bodyToMono(ArecipientResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            // Capturar errores relacionados con la respuesta del servidor
            log.error("Error al consumir el servicio. Código de error: {}", e.getRawStatusCode());
            log.error("Respuesta del servidor: {}", e.getResponseBodyAsString());
            throw e;
        } catch (Exception e) {
            // Capturar otros errores inesperados
            log.error("Error inesperado al consumir el servicio", e);
            throw e;
        }
    }
}


