package com.keysolbo.axsservice.database;

import java.util.Hashtable;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.keysolbo.axsservice.model.db.ConfigValue;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class ManageSurveyInMemory {
    @Autowired
    private ManageSurvey manageSurvey;
    private Hashtable<String, String> surveyConfigValue;
    private Hashtable<String, String> collectorConfigValue;
    private Hashtable<String, String> messageConfigValue;
    private Hashtable<String, String> contactLisConfigValue;

    @PostConstruct
    public String loadConfigValues(){
        surveyConfigValue = new Hashtable<>();
        collectorConfigValue = new Hashtable<>();
        messageConfigValue = new Hashtable<>();
        contactLisConfigValue = new Hashtable<>();

        String result="Load loadConfigValues properties was done";
        try {
            List<ConfigValue> surveyConfigValues = manageSurvey.retrieveConfigValue("sm_survey");
            surveyConfigValues.forEach((n) -> {
                surveyConfigValue.put(n.getCode(), n.getSmValue());
            });

            List<ConfigValue> collectorConfigValues = manageSurvey.retrieveConfigValue("sm_collector");
            collectorConfigValues.forEach((n) -> {
                collectorConfigValue.put(n.getCode(), n.getSmValue());
            });
  
            List<ConfigValue> messageConfigValues = manageSurvey.retrieveConfigValue("sm_message_id");
            messageConfigValues.forEach((n) -> {
                messageConfigValue.put(n.getCode(), n.getSmValue());
            });

            List<ConfigValue> contactListConfigValues = manageSurvey.retrieveConfigValue("axs_contact_list");
            contactListConfigValues.forEach((n) -> {
                contactLisConfigValue.put(n.getCode(), n.getSmValue());
            });

            log.info("Load Survey were done : \n{}",surveyConfigValue.toString());
            log.info("Load Collectors were done : \n{}",collectorConfigValue.toString());
            log.info("Load Messages were done : \n{}",messageConfigValue.toString());
            log.info("Load Conctact List were done : \n{}",contactLisConfigValue.toString());
        }catch (Exception ex){
            result="loadConfigValues error: "+ex.getMessage();
            log.error("loadConfigValues error: "+ex.getMessage());
        }
        return result;
    }
    
    public String getConfigSurvey(String sureyName){
       return surveyConfigValue.getOrDefault(sureyName, "UNKNOWN");
    }

    public String getConfigCollector(String collectorName){
        return collectorConfigValue.getOrDefault(collectorName, "UNKNOWN");
     }

     public String getConfigMessage(String messageName){
        return messageConfigValue.getOrDefault(messageName, "UNKNOWN");
     }

     public String getConfigContactList(String contactName){
        return contactLisConfigValue.getOrDefault(contactName, "UNKNOWN");
     }


}
