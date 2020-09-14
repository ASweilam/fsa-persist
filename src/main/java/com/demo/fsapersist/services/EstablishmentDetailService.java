package com.demo.fsapersist.services;

import com.demo.fsapersist.models.EstablishmentDetail;
import com.demo.fsapersist.models.LocalAuthority;
import com.google.gson.JsonArray;
import org.hibernate.Session;

import java.util.List;

/***
 * The Establishment Details Service. Process, Map and Persist into the DB
 */
public interface EstablishmentDetailService {

    boolean persist();
    boolean saveEstablishments(List<EstablishmentDetail> establishmentDetailList, LocalAuthority localAuthority, Session session);
    List<EstablishmentDetail> getEstablishmentDetailList(JsonArray EstablishmentDetailArray);
    LocalAuthority getLocalAuthority(JsonArray EstablishmentDetailArray);

}
