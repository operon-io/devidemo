package com.acme.processor;

import java.util.Map;
import java.util.HashMap;
import java.io.InputStream;

import org.apache.camel.Exchange;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import com.mapforce.*;

//
// Map data
// - the mapping was built using Altova Mapforce Enterprise Edition v2022, rel 2.
// - outputformat Java
// - to update the mapping, replace file "MappingMapToworktimeJson.java"
//    - if source/target types were changed, then update files in the com.mapforce.worktimeCsv
//
@ApplicationScoped
@Named("worktimeMapper")
public class Mapper {

	public java.io.Writer csv_to_json(InputStream inputStream) throws Exception {
        try {
            MappingMapToworktimeJson MappingMapToworktimeJsonObject = new MappingMapToworktimeJson();

            java.io.Writer writer = new java.io.StringWriter();
            com.altova.io.Input Ty_ajanseuranta_CSVSource = new com.altova.io.StreamInput(inputStream);
            com.altova.io.Output worktimeJson2Target = new com.altova.io.WriterOutput(writer);

            try {
                MappingMapToworktimeJsonObject.run(
                        Ty_ajanseuranta_CSVSource,
                        worktimeJson2Target);
                return writer;
            } finally {
                (Ty_ajanseuranta_CSVSource).close();
                worktimeJson2Target.close();
            }

        } finally {
        }
	}

}