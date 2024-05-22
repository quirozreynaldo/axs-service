package com.keysolbo.axsservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.keysolbo.axsservice.Util.ConstantDb;
import com.keysolbo.axsservice.database.ManageLog;
import com.keysolbo.axsservice.model.db.WebhookLog;
import com.keysolbo.axsservice.model.survey.WebhookResponseComplete;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WebhookService {
   @Autowired
   private ManageLog manageLog;

   /*
    * private int webhookLogId;
    * private String name;
    * private String filterType;
    * private String filterId;
    * private String eventType;
    * private String eventId;
    * private String eventDatetime;
    * private String objectType;
    * private String objectId;
    * private String respondentId;
    * private String recipientId;
    * private String surveyId;
    * private String userId;
    * private String collectorId;
    * private String status;
    * private String contactUpdated;
    * private Timestamp recordDate;
    */
   public void recordWebhook(WebhookResponseComplete webhookResponse) {
      log.info("webhookResponse: {}", webhookResponse);

      manageLog.updateRecipientResponded(webhookResponse.getResources().getCollector_id(), webhookResponse.getResources().getRecipient_id(), ConstantDb.RESPONDED);

      WebhookLog webhookLog = new WebhookLog();
      try {
         webhookLog.setName(webhookResponse.getName());
         webhookLog.setFilterType(webhookResponse.getFilter_type());
         webhookLog.setFilterId(webhookResponse.getFilter_id());
         webhookLog.setEventType(webhookResponse.getEvent_type());
         webhookLog.setEventId(webhookResponse.getEvent_id());
         webhookLog.setEventDatetime(webhookResponse.getEvent_datetime());
         webhookLog.setObjectType(webhookResponse.getObject_type());
         webhookLog.setObjectId(webhookResponse.getObject_id());
         webhookLog.setRespondentId(webhookResponse.getResources().getRespondent_id());
         webhookLog.setRecipientId(webhookResponse.getResources().getRecipient_id());
         webhookLog.setSurveyId(webhookResponse.getResources().getSurvey_id());
         webhookLog.setUserId(webhookResponse.getResources().getUser_id());
         webhookLog.setCollectorId(webhookResponse.getResources().getCollector_id());
         webhookLog.setStatus(ConstantDb.ACTIVE_STATUS);
         manageLog.recordWebhook(webhookLog);
      } catch (Exception ex) {
         log.error("recordWebhook: {}", ex.getMessage());
      }

   }
}