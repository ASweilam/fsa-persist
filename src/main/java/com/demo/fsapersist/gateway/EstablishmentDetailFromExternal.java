package com.demo.fsapersist.gateway;


import java.util.Optional;

/***
 * Gets a response from an external API
 */
public interface EstablishmentDetailFromExternal {

    /***
     *
     * @param           externalUrl the URL of the external API
     * @return          An optional of an Object of the contents from the API
     */
    Optional<?> getResponse(String externalUrl);


}
