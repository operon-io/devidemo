package com.devikone.service.ibgateway.model;

import java.util.Map;
import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

//
// This model describes the actual data. This info is received e.g. from the
// HTTP-request's headers.
//
// This is saved into Redis:
//   DataMetaInfo --> save this into ibgw:data:meta:UUID
//
// Then the actual data is saved as: ibgw:data:key:UUID
// or into queue: ibgw:data:queue:{queue-name}
//
// When data is fetched (by UUID, dk, or dq), this meta-info is first loaded.
//
public class DataMetaInfo {
    
    //
    // Fields:
    //
    
    // required
    private String did; // data-id: datan uniikki ID (uuid) 
    
    private Long cts; // created timestamp: datasanoman luontiaikaleima unix-muodossa 
    private String sid; // source-id, datan luoneen prosessin tunniste esim. integraation nimi 
    private String cid; // korrelaatiotunniste
    
    // required either dk or dq
    private String dk; // data-key: redis-avaimen nimi --> { key: "required key name", anyOtherValues: "" }
    private String dq; // redis-jonon nimi
    
    private Long dl; // data-length: datan koko
    private Long dttl; // data-time-to-live: datan annettu elinikä (jos dk) sekunneissa. 0 / null = ei vanhene.
    private String ds1; // datan sha1-tiiviste 
    private String dct; // datan content-type, esim. "json", "xml", "binary", ... 
    private String denc; // datan enkoodaus, esim. "raw", "base64", "gzip", "base64-gzip"
    private Map<String, Object> ext; // mitä tahansa muuta dataa JSON-objektimuodossa

    // 
    // Constructors:
    //
    public DataMetaInfo() {
        this.did = UUID.randomUUID().toString();
        Date now = new Date();
        this.cts = now.getTime();
    }

    //
    // Serializers:
    //
    public String toJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String result = mapper.writeValueAsString(this);
        return result;
    }

    public String extToJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String result = mapper.writeValueAsString(this.ext());
        return result;
    }

    //
    // Parsers:
    //
    public static DataMetaInfo fromJson(String value) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DataMetaInfo result = mapper.readValue(value, DataMetaInfo.class);
        return result;
    }

    //
    // Setters:
    //
    public DataMetaInfo did(String value) { this.did = value; return this; }
    public DataMetaInfo cts(Long value) { this.cts = value; return this; }
    public DataMetaInfo sid(String value) { this.sid = value; return this; }
    public DataMetaInfo cid(String value) { this.cid = value; return this; }
    public DataMetaInfo dk(String value) { this.dk = value; return this; }
    public DataMetaInfo dq(String value) { this.dq = value; return this; }
    public DataMetaInfo dl(long value) { this.dl = value; return this; }
    public DataMetaInfo dttl(Long value) { this.dttl = value; return this; }
    public DataMetaInfo ds1(String value) { this.ds1 = value; return this; }
    public DataMetaInfo dct(String value) { this.dct = value; return this; }
    public DataMetaInfo denc(String value) { this.denc = value; return this; }
    public DataMetaInfo ext(Map<String, Object> value) { this.ext = value; return this; }

    //
    // Getters:
    // 

    @JsonGetter("did")
    public String did() { return this.did; }

    @JsonGetter("cts")
    public Long cts() { return this.cts; }

    @JsonGetter("sid")
    public String sid() { return this.sid; }
    
    @JsonGetter("cid")
    public String cid() { return this.cid; }
    
    @JsonGetter("dk")
    public String dk() { return this.dk; }
    
    @JsonGetter("dq")
    public String dq() { return this.dq; }
    
    @JsonGetter("dl")
    public Long dl() { return this.dl; }
    
    @JsonGetter("dttl")
    public Long dttl() { return this.dttl; }

    @JsonGetter("ds1")
    public String ds1() { return this.ds1; }
    
    @JsonGetter("dct")
    public String dct() { return this.dct; }
    
    @JsonGetter("denc")
    public String denc() { return this.denc; }
    
    @JsonAnyGetter
    public Map<String, Object> ext() { return this.ext; }
}