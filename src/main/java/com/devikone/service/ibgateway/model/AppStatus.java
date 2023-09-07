package com.devikone.service.ibgateway.model;

import java.util.Map;
import java.util.List;
import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

//
// TODO
//  This should model the count of control and data-messages by app.
//  And also the list of IDs of such messages.
//  There could be also counter for control-message total amount gone through the system,
//  and same for data-message amount.
//
//  Returning the last message timestamp is minimum required info for status.
//
public class AppStatus {
    
    //
    // Fields:
    //
    private Long lastMessageTs;
    //private List<String> controlMessages;

    // 
    // Constructors:
    //
    public AppStatus() {

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
        sb.append("status=" + "TODO" + NEW_LINE);
        return sb.toString();
    }

    //
    // Setters:
    //
    
    //public AppStatus controlMessages(List<String> value) { this.controlMessages = value; return this; }
    public AppStatus lastMessageTs(Long value) { this.lastMessageTs = value; return this; }

    
    //
    // Getters:
    // 

    //@JsonGetter("controlMessages")
    //public List<String> controlMessages() { return this.controlMessages; }

    @JsonGetter("lastMessageTs")
    public Long lastMessageTs() { return this.lastMessageTs; }

}