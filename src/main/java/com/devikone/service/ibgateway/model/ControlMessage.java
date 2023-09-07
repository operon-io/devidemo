package com.devikone.service.ibgateway.model;

import java.util.Map;
import java.util.List;
import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

public class ControlMessage {
    
    //
    // Fields:
    //
    private String mid; // message-id: unique id for this control-message. Automatically generated.
    private Long cts; // created timestamp: given in Unix-format
    private String sid; // source-id, name of the process that created this message, e.g. integration name (Optional)
    private String tid; // target-id, e.g. integration name (Optional)
    private String subid; // could be e.g. Camel Route-name from the target process (Optional)
    private String cid; // correlation id (Optional)
    private Boolean rcack; // require-completed-ack: tells the recipient that Ack-message is required after processing is ready (Optional, default false)
    private Boolean hd; // has-data: flag which tells if the message has data associated with it (Optional, default false)
    // data-key: Redis-key name if message has data associated with it (Optional)
    // [{key: "keyNameInRedis", otherDescriptors...}]
    private List<Map<String, Object>> dk;
    private String dq; // redis-queue name if the data was sent into queue. (Optional)
    private Long dl; // data-length (Optional)
    private Long dttl; // data-time-to-live (0=no expire, >0 expire time) (Optional, default false)
    private Boolean ddel; // instruct to delete data after reading it (Optional, default false)
    private String ds1; // data sha1-hash (Optional)
    private String dct; // data content-type, e.g.. "json", "xml", "binary", ...  (Optional)
    private String denc; // datan encoding, e.g. "raw", "base64", "gzip", "base64-gzip" (Optional)
    private Boolean rdr; // require-data-response (Optional, default false)
    private String rdk; // response-data-key: in which Redis-key the response should be saved (rdk and rdq are mutually exclusive) (Optional)
    private String rdq; // response-data-queue: in which Redis-queue the response should be saved (rdk and rdq are mutually exclusive) (Optional)
    //
    // Any data in JSON-format, e.g. SQL-query or Base64-formatted data.
    // This should not be used to send large data-sets. Use data-messages for this purpose.
    //
    private Map<String, Object> ext;

    // 
    // Constructors:
    //
    public ControlMessage() {
        this.mid = UUID.randomUUID().toString();
        Date now = new Date();
        this.cts = now.getTime();

        this.rcack = false;
        this.hd = false;
        this.rdr = false;
        this.ddel = false;
        this.dttl = 0L;
    }

    public ControlMessage ControlMessage(String sid, String tid, String subid, String cid) {
        this.mid = UUID.randomUUID().toString();
        Date now = new Date();
        this.cts = now.getTime();

        this.sid = sid;
        this.tid = tid;
        this.subid = subid;
        this.cid = cid;
        
        this.rcack = false;
        this.hd = false;
        this.rdr = false;
        this.ddel = false;
        this.dttl = 0L;

        return this;
    }

    //
    // Serializers:
    //
    public String toJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String result = mapper.writeValueAsString(this);
        return result;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        String NEW_LINE = "\n";
        sb.append("mid=" + this.mid() + NEW_LINE);
        sb.append("cts=" + this.cts() + NEW_LINE);
        sb.append("sid=" + this.sid() + NEW_LINE);
        sb.append("tid=" + this.tid() + NEW_LINE);
        sb.append("subid=" + this.subid() + NEW_LINE);
        sb.append("cid=" + this.cid() + NEW_LINE);
        sb.append("rcack=" + this.rcack() + NEW_LINE);
        sb.append("hd=" + this.hd() + NEW_LINE);
        sb.append("rdr=" + this.rdr() + NEW_LINE);
        sb.append("ddel=" + this.ddel() + NEW_LINE);
        sb.append("dttl=" + this.dttl() + NEW_LINE);
        return sb.toString();
    }

    //
    // Parsers:
    //
    public static ControlMessage fromJson(String value) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ControlMessage result = mapper.readValue(value, ControlMessage.class);
        return result;
    }

    //
    // Setters:
    //
    public ControlMessage mid(String value) { this.mid = value; return this; }
    public ControlMessage cts(Long value) { this.cts = value; return this; }
    public ControlMessage sid(String value) { this.sid = value; return this; }
    public ControlMessage tid(String value) { this.tid = value; return this; }
    public ControlMessage subid(String value) { this.subid = value; return this; }
    public ControlMessage cid(String value) { this.cid = value; return this; }
    public ControlMessage rcack(boolean value) { this.rcack = value; return this; }
    public ControlMessage hd(boolean value) { this.hd = value; return this; }
    public ControlMessage dk(List<Map<String, Object>> value) { this.dk = value; return this; }
    public ControlMessage dq(String value) { this.dq = value; return this; }
    public ControlMessage dl(long value) { this.dl = value; return this; }
    public ControlMessage dttl(Long value) { this.dttl = value; return this; }
    public ControlMessage ddel(boolean value) { this.ddel = value; return this; }
    public ControlMessage ds1(String value) { this.ds1 = value; return this; }
    public ControlMessage dct(String value) { this.dct = value; return this; }
    public ControlMessage denc(String value) { this.denc = value; return this; }
    public ControlMessage rdr(boolean value) { this.rdr = value; return this; }
    public ControlMessage rdk(String value) { this.rdk = value; return this; }
    public ControlMessage rdq(String value) { this.rdq = value; return this; }
    public ControlMessage ext(Map<String, Object> value) { this.ext = value; return this; }

    //
    // Getters:
    // 

    @JsonGetter("mid")
    public String mid() { return this.mid; }

    @JsonGetter("cts")
    public Long cts() { return this.cts; }

    @JsonGetter("sid")
    public String sid() { return this.sid; }
    
    @JsonGetter("tid")
    public String tid() { return this.tid; }
    
    @JsonGetter("subid")
    public String subid() { return this.subid; }
    
    @JsonGetter("cid")
    public String cid() { return this.cid; }
    
    @JsonGetter("rcack")
    public Boolean rcack() { return this.rcack; }
    
    @JsonGetter("hd")
    public Boolean hd() { return this.hd; }
    
    @JsonGetter("dk")
    public List<Map<String, Object>> dk() { return this.dk; }
    
    @JsonGetter("dq")
    public String dq() { return this.dq; }
    
    @JsonGetter("dl")
    public Long dl() { return this.dl; }
    
    @JsonGetter("dttl")
    public Long dttl() { return this.dttl; }

    @JsonGetter("ddel")
    public Boolean ddel() { return this.ddel; }

    @JsonGetter("ds1")
    public String ds1() { return this.ds1; }
    
    @JsonGetter("dct")
    public String dct() { return this.dct; }
    
    @JsonGetter("denc")
    public String denc() { return this.denc; }
    
    @JsonGetter("rdr")
    public Boolean rdr() { return this.rdr; }
    
    @JsonGetter("rdk")
    public String rdk() { return this.rdk; }
    
    @JsonGetter("rdq")
    public String rdq() { return this.rdq; }
    
    @JsonAnyGetter
    public Map<String, Object> ext() { return this.ext; }
}