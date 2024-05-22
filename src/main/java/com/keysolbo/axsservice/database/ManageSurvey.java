package com.keysolbo.axsservice.database;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.keysolbo.axsservice.model.db.ConfigValue;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ManageSurvey {
    @Autowired
    @Qualifier("digitalTemplate")
    private JdbcTemplate jdbcTemplate;

    public List<ConfigValue> retrieveConfigValue(String type){
        String sqlSelect="SELECT config_value_id, `type`, code, sm_value, `timestamp` "+
                         " FROM deaxs_config.config_value "+
                         " WHERE  `type` = ?";
        List<ConfigValue> result = new ArrayList<>();
        try {
            result = jdbcTemplate.query(sqlSelect, (rs, row) ->
                    new ConfigValue(rs.getInt("config_value_id"),
                            rs.getString("type"),
                            rs.getString("code"),
                            rs.getString("sm_value"),
                            rs.getTimestamp("timestamp")
                    ), new String[]{type});
        } catch (Exception ex) {
            log.error("retrieveConfigValue fail", ex);
        }
        return result;
    }
    
    
   
      /*  public List<SmsGatewayProperties> retrieveSmsGatewayProperties(){
        String sqlSelect ="SELECT smsgw_properties_id, code, value, status, record_date " +
                          " FROM config.smsgateway_properties where status = 'A'";
        List<SmsGatewayProperties> result = new ArrayList<>();
        try {
            result = jdbcTemplate.query(sqlSelect, (rs, row) ->
                    new SmsGatewayProperties(rs.getString("smsgw_properties_id"),
                            rs.getString("code"),
                            rs.getString("value"),
                            rs.getString("status"),
                            rs.getTimestamp("record_date")
                    ), new String[]{});
        } catch (Exception ex) {
            log.error("retrieveSmsGatewayProperties fail", ex);
        }
        return result;
    }

    public SmsGatewayOperatorsRange retrieveOtherOperatorRange(String phoneNumber) {
        String sql = "SELECT smsgateway_oper_range_id, start_range, end_range, operator, status, record_date FROM config.smsgateway_operators_range " +
                    " WHERE ? BETWEEN start_range and end_range "+
                    " and status != 'I'";
        SmsGatewayOperatorsRange user = null;
        try {
            user = jdbcTemplate.queryForObject(sql, (rs, row) ->
                    new SmsGatewayOperatorsRange(rs.getString("smsgateway_oper_range_id"),
                            rs.getLong("start_range"),
                            rs.getLong("end_range"),
                            rs.getString("operator"),
                            rs.getString("status"),
                            rs.getTimestamp("record_date")
                    ), new String[]{phoneNumber});
        } catch (Exception ex) {
            log.error(String.format("retrieveOtherOperatorRange: %s was not found.", phoneNumber));
        }
        return user;
    }
        public void updateAckDeliveryStatus(SmsGatewayLog smsGatewayLog){
        String update="UPDATE record.smsgateway_ack_log_0 SET smpp_in = ?, ack_out=? WHERE sms_id=?";
        try {
            int updateRows= jdbcTemplate.update(update, smsGatewayLog.getSmsIn(), Util.extractSmppStatus(smsGatewayLog.getSmsIn()) ,smsGatewayLog.getSmsId());
            if (updateRows==0){
                int updateTable_1 = updateAckDeliveryStatus_1(smsGatewayLog);
                if (updateTable_1==0) {
                    SmsGatewayAckLog smsGatewayAckLog = new SmsGatewayAckLog();
                    smsGatewayAckLog.setSmsId(smsGatewayLog.getSmsId());
                    smsGatewayAckLog.setSmsIn(smsGatewayLog.getSmsIn());
                    recordAckLog(smsGatewayAckLog);
                }
            }
        }catch (Exception ex){
            log.error("updateAckDeliveryStatus log exception",ex);
        }
    }
        public void recordLog(SmsGatewayLog smsGatewayLog){
        log.info(">>>>>>>: {}",smsGatewayLog);
       String smsgatewayId = UUID.randomUUID().toString().replace("-", "");
       String cleanText = smsGatewayLog.getShortMessage().replaceAll("[^\\x00-\\x7F]", "?");
       String insert="INSERT INTO record.smsgateway_log_0 (smsgateway_id, origin_number, destination_number, short_message, sms_id, smpp_in, smpp_out, app_name,client_ip,request,smpp_connection,coding,check_compliance ,destination_type,end_timestamp,request_id) "
        +"VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,now(),?)";
         try {
             smsGatewayLog.setSmsgatewayId(smsgatewayId);
             jdbcTemplate.update(insert, smsgatewayId, smsGatewayLog.getOriginNumber(), smsGatewayLog.getDestinationNumber(), cleanText,smsGatewayLog.getSmsId(),
                     smsGatewayLog.getSmsIn(), smsGatewayLog.getSmsOut(), smsGatewayLog.getAppName(),smsGatewayLog.getClientIp(),smsGatewayLog.getRequest(),smsGatewayLog.getSmppConnection(),smsGatewayLog.getCoding(),smsGatewayLog.getCheckCompliance(),smsGatewayLog.getDestinationType(),smsGatewayLog.getRequestId()!=null?smsGatewayLog.getRequestId():null);
         }catch (Exception ex){
             log.error("Record log exception",ex);
         }
    }
    */

}
