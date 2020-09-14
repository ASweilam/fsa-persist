package com.demo.fsapersist.gateway;

import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.UnknownContentTypeException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@Repository
public class EstablishmentDetailFromExternalImpl implements EstablishmentDetailFromExternal {

    @Override
    public Optional<Object> getResponse(String externalUrl) {

        if(externalUrl != null){

            try{
                RestTemplate restTemplate = new RestTemplate();

                URI url = null;

                try {
                    url = new URI(externalUrl);
                }
                catch (URISyntaxException ex) {
                    ex.printStackTrace();
                    // An exception occurred, so return Optional.empty()
                    return Optional.empty();
                }

                return Optional.of(restTemplate.getForObject(url, Object.class));

            } catch (UnknownContentTypeException e) {
                e.printStackTrace();
                // An exception occurred, so return Optional.empty()
                return Optional.empty();
            }
        }

        return Optional.empty();
    }

}
