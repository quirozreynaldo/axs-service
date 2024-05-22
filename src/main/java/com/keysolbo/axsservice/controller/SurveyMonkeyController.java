package com.keysolbo.axsservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.keysolbo.axsservice.model.AxsResponse;
import com.keysolbo.axsservice.model.survey.CollectorMessageRequest;
import com.keysolbo.axsservice.model.survey.CollectorMessageResponse;
import com.keysolbo.axsservice.model.survey.CollectorType;
import com.keysolbo.axsservice.model.survey.CollectorTypeResponse;
import com.keysolbo.axsservice.model.survey.ContactListRequest;
import com.keysolbo.axsservice.model.survey.ContactListResponse;
import com.keysolbo.axsservice.model.survey.Contacts;
import com.keysolbo.axsservice.model.survey.SendSurveyRequest;
import com.keysolbo.axsservice.model.survey.SendSurveyResponse;
import com.keysolbo.axsservice.model.survey.Succeeded;
import com.keysolbo.axsservice.model.survey.WebhookResponseComplete;
import com.keysolbo.axsservice.service.SurveyMonkeyService;
import com.keysolbo.axsservice.service.WebhookService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/survey")
@RestController
public class SurveyMonkeyController {
    @Autowired
    private SurveyMonkeyService surveyMonkeyService;
    @Autowired
    private WebhookService webhookLog;

    @PostMapping("/contactList")
    public AxsResponse<ContactListResponse> createContactList(@RequestBody ContactListRequest contactListRequest) {
        AxsResponse<ContactListResponse> axsResponse = new AxsResponse<>();
        ContactListResponse contactListResponse = surveyMonkeyService.createContactList(contactListRequest.getName());
        axsResponse.setErrorCode("OK");
        axsResponse.setData(contactListResponse);
        return axsResponse;
    }

    @PostMapping("/create-multi-contacts")
    public AxsResponse<Succeeded> createMultiContacts(@RequestBody Contacts contacts, String contactListId) {
        AxsResponse<Succeeded> axsResponse = new AxsResponse<>();
        contactListId = "255858277";
        Succeeded succeeded = surveyMonkeyService.createMultiContacts(contacts, contactListId);
        axsResponse.setErrorCode("OK");
        axsResponse.setData(succeeded);
        return axsResponse;
    }

    @PostMapping("/create-collector-type")
    public AxsResponse<CollectorTypeResponse> createSurveyCollector(@RequestBody CollectorType collectorType,
            String surveyId) {
        AxsResponse<CollectorTypeResponse> axsResponse = new AxsResponse<>();
        surveyId = "412461367";
        CollectorTypeResponse collectorTypeResponse = surveyMonkeyService.createSurveyCollector(collectorType,
                surveyId);
        axsResponse.setErrorCode("OK");
        axsResponse.setData(collectorTypeResponse);
        return axsResponse;
    }

    @PostMapping("/create-collector-message")
    public AxsResponse<CollectorMessageResponse> createCollectorMessage(
            @RequestBody CollectorMessageRequest collectorMessageRequest, String collectorId) {
        AxsResponse<CollectorMessageResponse> axsResponse = new AxsResponse<>();
        collectorId = "430665516";
        CollectorMessageResponse collectorMessageResponse = surveyMonkeyService
                .createCollectorMessage(collectorMessageRequest, collectorId);
        axsResponse.setErrorCode("OK");
        axsResponse.setData(collectorMessageResponse);
        return axsResponse;
    }

    @PostMapping("/send-survey")
    public AxsResponse<SendSurveyResponse> sendSurvey(@RequestBody SendSurveyRequest sendSurveyRequest,String collectorId,String messageId){
        AxsResponse<SendSurveyResponse> axsResponse = new AxsResponse<>();
        collectorId = "430665516";
        messageId="115893736";
        SendSurveyResponse sendSurveyResponse = surveyMonkeyService.sendSurvey(sendSurveyRequest, collectorId, messageId);
        axsResponse.setErrorCode("OK");
        axsResponse.setData(sendSurveyResponse);
        return axsResponse;
    }

    @RequestMapping(value = "/responsewebhook", method = RequestMethod.HEAD)
    public ResponseEntity<String> handleInboundWebhook(HttpServletRequest request) {
        log.info("Received HEAD request");
        return ResponseEntity.ok().build();
    }
/*
    @PostMapping("/responsewebhook")
    public ResponseEntity<String>  handleInboundWebhook(@RequestBody String webhookResponse,HttpServletRequest request) {
        log.info("Payload : {} " , webhookResponse);
        return ResponseEntity.ok("Webhook recibido correctamente");
    }
 */      
@PostMapping("/responsewebhook")
public ResponseEntity<String>  handleInboundWebhook(@RequestBody WebhookResponseComplete webhookResponse,HttpServletRequest request) {
    log.info("Payload : {} " , webhookResponse);
    webhookLog.recordWebhook(webhookResponse);
    return ResponseEntity.ok("Webhook recibido correctamente");
}
}
