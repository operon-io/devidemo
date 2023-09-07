package com.devikone.service.ibgateway;

import java.util.Date;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.math.BigInteger;

import org.apache.camel.Exchange;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.devikone.service.ibgateway.exception.*;
import com.devikone.service.ibgateway.model.ControlMessage;
import com.devikone.service.ibgateway.model.ControlMessagePollResponse;
import com.devikone.service.ibgateway.model.DataMetaInfo;
import com.devikone.transports.Redis;

@ApplicationScoped
@Named("ServiceIbgw")
public class IBGatewayController {

    @Inject
    Redis redis;

    private final String APP_PREFIX = "ibgw:app:";
    private final String CONTROL_QUEUE_PREFIX = "control.queue";
    private final String CONTROL_KEY_PREFIX = "control.key";
    private final String CONTROL_READY_PREFIX = "control.rdy";
    private final String DATA_KEY_PREFIX = "data.key";
    private final String DATA_META_KEY_PREFIX = "data.meta.key";
    private static final String IBGW_VERSION = "1.0.0";

    public static String version() {
        return IBGW_VERSION;
    }

    //
    // find the associated app-name by api-key
    //
    public String getAppNameByApiKey(String apiKey) throws InterruptedException {
        String appName = redis.get("ibgw:apikey:" + apiKey);
        return appName;
    }

    public void setAppNameByApiKey(String apiKey, String appName) throws InterruptedException {
        redis.set("ibgw:apikey:" + apiKey, appName);
    }

    //
    // TODO
    //  Return the descriptors of the control-messages and data-messages
    //  This could be the count of such messages or list of ids or both.
    //
    public String getAppStatusByApiKey(String apiKey) throws InterruptedException {
        String appName = redis.get("ibgw:apikey:" + apiKey);
        return appName;
    }

    public void authenticate(String apiKey) throws UnauthorizedException, InterruptedException {
        String appName = redis.get("ibgw:apikey:" + apiKey);
        if (appName == null || appName == "") {
            throw new UnauthorizedException(apiKey);
        }
    }

    //
    // This is for testing
    //
    public ControlMessage newTestControlMessage() {
        ControlMessage msg = new ControlMessage();
        System.out.println("mid: " + msg.mid()); // Message ID
        msg.sid("ibgw"); // Source ID
        msg.tid("demo"); // Target ID
        msg.subid("direct:demo.route.process"); // Sub-id (in this case Camel-route)
        msg.cid("123"); // Correlation ID
        msg.hd(true); // Has data

        // Data-keys
        List<Map<String, Object>> redisKeys = new ArrayList<Map<String, Object>>();
        Map<String, Object> key1 = new HashMap<String, Object>();
        key1.put("key", "myKey");
        key1.put("description", "test key");
        redisKeys.add(key1);
        msg.dk(redisKeys);

        return msg;
    }

    //
    // Control-message is expected to be as an JSON-object in the HTTP Request's body
    //
    public ControlMessage mapControlMessage(String apiKey, Exchange ex) throws JsonProcessingException {
        String body = ex.getIn().getBody(String.class);
        ControlMessage msg = ControlMessage.fromJson(body);
        if (msg.mid() == null) {
            msg.mid(UUID.randomUUID().toString());
        }
        if (msg.cts() == null) {
            Date now = new Date();
            msg.cts(now.getTime());
        }
        ex.getIn().setBody(msg);
        return msg;
    }

    public ControlMessage saveControlMessage(String apiKey, ControlMessage msg) throws InterruptedException, JsonProcessingException {
        String appName = this.getAppNameByApiKey(apiKey);
        System.out.println("API-KEY :: " + apiKey + ", APP-NAME :: " + appName);
        String queueName = APP_PREFIX + appName + ":" + CONTROL_QUEUE_PREFIX;
        System.out.println("SAVE TO QUEUE :: " + queueName);
        
        //
        // Control-message is pushed into Queue
        //
        System.out.println("MESSAGE :: " + msg);
        redis.lpush(queueName, msg.toJson());
        
        //
        // Control-message is backupped into key which is set to expire after a while
        //
        String keyName = APP_PREFIX + appName + ":" + CONTROL_KEY_PREFIX + ":" + msg.mid();
        redis.set(keyName, msg.toJson());
        redis.expire(keyName, 900000L);
        return msg;
    }

    //
    // Read all the control-messages from the control-message queue
    //
    public ControlMessagePollResponse popControlMessages(String apiKey) throws InterruptedException, JsonProcessingException {
        String appName = this.getAppNameByApiKey(apiKey);
        String msgStr = null;
        List<ControlMessage> messages = new ArrayList<ControlMessage>();
        do {
            msgStr = redis.rpop(APP_PREFIX + appName + ":" + CONTROL_QUEUE_PREFIX);
            if (msgStr == null) {
                break;
            }
            else {
                ControlMessage msg = ControlMessage.fromJson(msgStr);
                messages.add(msg);
            }
        } while (msgStr != null);
        ControlMessagePollResponse result = null;
        if (messages.size() > 0) {
            result = new ControlMessagePollResponse(messages);
        }
        return result;
    }

    //
    // Marks the control-message as "ready", i.e. completed.
    //
    public void ackReadyControlMessage(String apiKey, String messageId, Long expireSeconds) throws InterruptedException {
        String appName = this.getAppNameByApiKey(apiKey);
        Date now = new Date();
        redis.setex(APP_PREFIX + appName + ":" + CONTROL_READY_PREFIX + ":" + messageId, String.valueOf(now.getTime()), expireSeconds);
    }

    public boolean hasControlMessageReadyAck(String apiKey, String messageId) throws InterruptedException {
        String appName = this.getAppNameByApiKey(apiKey);
        String value = redis.get(APP_PREFIX + appName + ":" + CONTROL_READY_PREFIX + ":" + messageId);
        if (value == null) {
            return false;
        }
        else {
            return true;
        }
    }

    public String controlMessageToJson(ControlMessage value) throws JsonProcessingException {
        return value.toJson();
    }

    public String controlMessagePollResponseToJson(ControlMessagePollResponse value) throws JsonProcessingException {
        return value.toJson();
    }

    // ------------------------
    //
    // Data-related operations:
    //
    // ------------------------

    //
    // Data-POST-request has one dataKey (dk) and it is saved into Redis
    // To send multiple files, then multiple data-requests must be made.
    //
    public DataMetaInfo saveData(String apiKey, Exchange ex) 
            throws InterruptedException, JsonProcessingException, DataSaveException, NoSuchAlgorithmException {
        String appName = this.getAppNameByApiKey(apiKey);
        String dataKey = ex.getIn().getHeader("x-ibgw-dk", String.class);
        if (dataKey == null) {
            throw new DataSaveException("Data-key was null.");
        }
        
        byte[] data = ex.getIn().getBody(byte[].class);
        
        DataMetaInfo metaInfo = new DataMetaInfo();

        //
        // Map values from the request
        //
        String sid = ex.getIn().getHeader("x-ibgw-sid", String.class);
        String cid = ex.getIn().getHeader("x-ibgw-cid", String.class);
        String dct = ex.getIn().getHeader("x-ibgw-dct", String.class);
        String denc = ex.getIn().getHeader("x-ibgw-denc", String.class);
        String dttl = ex.getIn().getHeader("x-ibgw-dttl", String.class);
        String extStr = ex.getIn().getHeader("x-ibgw-ext", String.class);

        if (extStr != null && extStr != "") {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> extMap = mapper.readValue(extStr, Map.class);
            metaInfo.ext(extMap);
        }

        String sha1 = null;
        try {
            sha1 = sha1(data);
        } catch (EmptyDataException e) {
            throw new DataSaveException("Data was empty.");
        }

        metaInfo.sid(sid);
        metaInfo.cid(cid);
        metaInfo.dct(dct);
        metaInfo.denc(denc);
        metaInfo.dl(data.length);
        metaInfo.dk(dataKey);
        metaInfo.ds1(sha1);

        //
        // Set the meta-data:
        //
        if (dttl != null) {
            Long dttlLong = Long.valueOf(dttl);
            metaInfo.dttl(dttlLong);
            redis.set(APP_PREFIX + appName + ":" + DATA_META_KEY_PREFIX + ":" + dataKey, metaInfo.toJson());
            if (dttlLong != null && dttlLong > 0L) {
                redis.expire(APP_PREFIX + appName + ":" + DATA_META_KEY_PREFIX + ":" + dataKey, dttlLong);
            }
        }
        else {
            redis.set(APP_PREFIX + appName + ":" + DATA_META_KEY_PREFIX + ":" + dataKey, metaInfo.toJson());
        }
        
        //
        // Set the actual data:
        //
        redis.set(APP_PREFIX + appName + ":" + DATA_KEY_PREFIX + ":" + dataKey, data);
        if (dttl != null) {
            Long dttlLong = Long.valueOf(dttl);
            if (dttlLong != null && dttlLong > 0L) {
                redis.expire(APP_PREFIX + appName + ":" + DATA_KEY_PREFIX + ":" + dataKey, dttlLong);
            }
        }
        
        return metaInfo;
    }

    public DataMetaInfo loadData(String apiKey, Exchange ex) 
        throws InterruptedException, JsonProcessingException, DataLoadException, NoSuchAlgorithmException {
        String appName = this.getAppNameByApiKey(apiKey);
        String dataKey = ex.getIn().getHeader("x-ibgw-dk", String.class);
        if (dataKey == null) {
            throw new DataLoadException("Data-key was null.");
        }
        String redisDataKey = APP_PREFIX + appName + ":" + DATA_KEY_PREFIX + ":" + dataKey;
        byte[] data = redis.getBytes(redisDataKey);
        String sha1 = null;
        try {
            sha1 = sha1(data);
        } catch (EmptyDataException e) {
            throw new DataLoadException("Data was empty.");
        }

        Long ttl = redis.ttl(redisDataKey);

        String redisDataMetaKey = APP_PREFIX + appName + ":" + DATA_META_KEY_PREFIX + ":" + dataKey;
        String dataMetaInfoJson = redis.get(redisDataMetaKey);
        DataMetaInfo result = DataMetaInfo.fromJson(dataMetaInfoJson);
        
        if (sha1.equals(result.ds1()) == false) {
            throw new DataLoadException("Data corrupted, sha-1 does not match.");
        }

        ex.getIn().setHeader("IBGW_DATA", data);
        ex.getIn().setHeader("x-ibgw-did", result.did());
        ex.getIn().setHeader("x-ibgw-dl", result.dl());
        ex.getIn().setHeader("x-ibgw-dttl", ttl);
        ex.getIn().setHeader("x-ibgw-ds1", result.ds1());
        ex.getIn().setHeader("x-ibgw-sid", result.sid());
        ex.getIn().setHeader("x-ibgw-cid", result.cid());
        ex.getIn().setHeader("x-ibgw-dct", result.dct());
        ex.getIn().setHeader("x-ibgw-denc", result.denc());
        ex.getIn().setHeader("x-ibgw-dk", result.dk());
        ex.getIn().setHeader("x-ibgw-dq", result.dq());
        ex.getIn().setHeader("x-ibgw-cts", result.cts().toString());
        ex.getIn().setHeader("x-ibgw-ext", result.extToJson());

        return result;
    }

    public String dataMetaInfoToJson(DataMetaInfo value) throws JsonProcessingException {
        return value.toJson();
    }

    public String sha1(byte[] value) throws NoSuchAlgorithmException, EmptyDataException {
        try {
            if (value == null || value.length == 0) {
                throw new EmptyDataException();
            }
            // getInstance() method is called with algorithm SHA-1
            MessageDigest md = MessageDigest.getInstance("SHA-1");
    
            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(value);
    
            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);
    
            // Convert message digest into hex value
            String hashtext = no.toString(16);
    
            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
    
            // return the HashText
            return hashtext;
        }
    
        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}