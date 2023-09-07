package com.devikone.processor;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.camel.Exchange;
import org.apache.camel.spi.HeaderFilterStrategy;
import org.apache.camel.http.common.HttpHeaderFilterStrategy;

/**
 * Camel has this annoying mechanism to add all the headers to HTTP-messages,
 * which cause some requests to fail.
 * This filter removes the unwanted headers.
 * 
 * HOW TO USE:
 *   1)
 *     .setHeader(Exchange.HTTP_URI,
 *         simple("{{app.url}}/api/anything?param=abc&query=${body}"))
 *   2)
 *        .setHeader("Authorization", constant(BASIC_AUTH))
 *   3) 
 *       .to("http://oldhost?headerFilterStrategy=dkHttpHeaderFilterStrategy")
 *
 *   Aseta kutsuttava URL ja siihen liittyvät parametrit headeriin Exchange.HTTP_URI
 *
 *   Voit käyttää Authorization -headeria normaalisti, koska yo. headerFilterStrategy säästää sen.
 *   Jos haluat säästää muita headereita, niin lisää ne filtteriin.
 *
 *   Filtteriä käytetään producerin (.to) optiona kuten yllä.
 *
 *   Filtteriä EI käytetä kutsuttavan URL:n osana, jos URL annetaan omassa headerissa, kuten yllä olevassa esimerkissä.
 * 
 **/
@ApplicationScoped
@Named("dkHttpHeaderFilterStrategy")
public class DKHttpHeaderFilterStrategy implements HeaderFilterStrategy {

    //
    // Remove headers that match this filter
    // Applies filtering logic to Camel Message header that is going to be copied to target message such as CXF and JMS message.
    //
    public boolean applyFilterToCamelHeaders​(String headerName, Object headerValue, Exchange exchange) {
        // System.out.println(">> APPLY HEADER FILTER :: " + headerName);
        if (headerName.equalsIgnoreCase("AUTHORIZATION")) {
            // Keep
            return false;
        }
        else if (headerName.toUpperCase().startsWith("CAMELHTTP")) {
            // Keep
            return false;
        }
        else {
            // Remove
            return true;
        }
    }

    // Applies filtering logic to an external message header such as CXF and JMS message that is going to be copied to Camel message header.
    public boolean applyFilterToExternalHeaders​(String headerName, Object headerValue, Exchange exchange) {
        // System.out.println(">> APPLY EXT-HEADER FILTER (KEEP) :: " + headerName);
        return false;
    }
}