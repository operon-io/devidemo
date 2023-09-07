package com.devikone.service.ibgateway.model;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;

//
// Returns an object, which contains list of Control-messages,
// and also other fields.
// { c: 1, m: [] }
//   c: count, m: messages
//
public class ControlMessagePollResponse {
    
    private Map<String, Object> result;

    // 
    // Constructors:
    //
    public ControlMessagePollResponse() {
        this.result = new HashMap<String, Object>();
    }

    public ControlMessagePollResponse(List<ControlMessage> messages) {
        this.result = new HashMap<String, Object>();
        this.result.put("c", messages.size());
        this.result.put("m", messages);
    }

    //
    // Serializers:
    //
    public String toJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String result = mapper.writeValueAsString(this);
        return result;
    }

    //
    // Parsers:
    //
    public static ControlMessagePollResponse fromJson(String value) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> mappedList = mapper.readValue(value, new TypeReference<Map<String,Object>>(){});
        List<ControlMessage> controlMessages = (List<ControlMessage>) mappedList.get("m");
        ControlMessagePollResponse result = new ControlMessagePollResponse(controlMessages);
        return result;
    }

    //
    // Setters:
    //
    public ControlMessagePollResponse addMessage(ControlMessage message) {
        List<ControlMessage> msgs = ((List<ControlMessage>) this.result.get("m"));
        msgs.add(message);

        Long count = (Long) this.result.get("c");
        if (count != null) {
            this.result.put("c", count ++);
        }
        else {
            this.result.put("c", msgs.size());
        }
        return this;
    }

    //
    // Getters:
    // 

    @JsonGetter("m")
    public List<ControlMessage> m() { 
        return (List<ControlMessage>) this.result.get("m");
    }

    public static List<ControlMessage> m(ControlMessagePollResponse msg) { 
        return (List<ControlMessage>) msg.result.get("m");
    }

    @JsonGetter("c")
    public Long c() { 
        List<ControlMessage> msgList = ((List<ControlMessage>) this.result.get("m"));
        Long result = (long) msgList.size();
        return result;
    }

    public static Long c(ControlMessagePollResponse msg) { 
        List<ControlMessage> msgList = ((List<ControlMessage>) msg.result.get("m"));
        Long result = (long) msgList.size();
        return result;
    }

}