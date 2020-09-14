package com.demo.fsapersist.services;

import com.demo.fsapersist.gateway.EstablishmentDetailFromExternalImpl;
import com.demo.fsapersist.models.EstablishmentDetail;
import com.demo.fsapersist.models.LocalAuthority;
import com.google.gson.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EstablishmentDetailServiceImpl implements EstablishmentDetailService {

    private static final Logger logger = LogManager.getLogger(EstablishmentDetailServiceImpl.class);

    @Value("${fsaUrl}")
    private String fsaUrl;

    SessionFactory factory = new Configuration()
            .configure()
            .addAnnotatedClass(EstablishmentDetail.class)
            .addAnnotatedClass(LocalAuthority.class)
            .buildSessionFactory();

    Session session = factory.getCurrentSession();


    @Autowired
    private EstablishmentDetailFromExternalImpl EstablishmentDetailFromExternalImpl;



    @Override
    public boolean persist() {

        if(getEstablishmentDetailArray(fsaUrl).isPresent()){

            //Get the Establishment Detail body as a JsonArray
            JsonArray establishmentDetailArray = getEstablishmentDetailArray(fsaUrl).get();

            //Map the JsonArray into a list of Establishment Details
            List<EstablishmentDetail> establishmentDetails = getEstablishmentDetailList(establishmentDetailArray);

            //Get the Local Authority from the Establishment Detail
            LocalAuthority localAuthority = getLocalAuthority(establishmentDetailArray);

            //Persist into the MySQL Database
            saveEstablishments(establishmentDetails, localAuthority, session);

            return true;
        }
        return false;
    }


    private Optional<JsonArray> getEstablishmentDetailArray(String url) {

        Gson gson = new Gson();

        if(EstablishmentDetailFromExternalImpl.getResponse(url).isPresent()) {

            Object inputObject = EstablishmentDetailFromExternalImpl.getResponse(url).get();

            JsonElement jsonTreeReader = gson.toJsonTree(inputObject);

            try {
                JsonArray EstablishmentDetailArray = jsonTreeReader.getAsJsonObject()
                        .getAsJsonObject("FHRSEstablishment")
                        .getAsJsonObject("EstablishmentCollection")
                        .getAsJsonArray("EstablishmentDetail");

                return Optional.of(EstablishmentDetailArray);

            } catch (java.lang.NullPointerException e) {
                return Optional.empty();
            }
        } else {
            //No object present.
            return Optional.empty();
        }
    }


    @Override
    public boolean saveEstablishments(List<EstablishmentDetail> establishmentDetailList, LocalAuthority localAuthority, Session sessionParam) {

        if (!(establishmentDetailList.isEmpty() || localAuthority == null) ) {

            try {
                sessionParam.beginTransaction();

                //Check if this local authority exists in the database first
                if(sessionParam.get(LocalAuthority.class, localAuthority.getCode()) == null)
                    sessionParam.save(localAuthority);

                for (EstablishmentDetail establishmentDetail : establishmentDetailList) {

                    if (establishmentDetail.getFhrs_id() != null && localAuthority.getCode()!=null){

                        if(sessionParam.get(EstablishmentDetail.class, establishmentDetail.getFhrs_id()) == null){

                            establishmentDetail.setLocalAuthority(localAuthority);

                            localAuthority.addEstablishmentDetail(establishmentDetail);

                            sessionParam.save(establishmentDetail);
                        }else {
                            logger.warn("This establishment detail already exists in database: " + establishmentDetail.toString());
                        }

                    } else {
                        logger.warn("Incompatible object without Id:  "+ establishmentDetail.toString());
                    }
                }


                // Try to commit
                try {
                    sessionParam.getTransaction().commit();
                } catch (ConstraintViolationException e) {
                    e.printStackTrace();
                    return false;
                }

            } catch (Exception e) {
                e.printStackTrace();
                return false;

            } finally {
                sessionParam.close();
                factory.close();
            }
            return true;
        }

        return false;
    }


    @Override
    public List<EstablishmentDetail> getEstablishmentDetailList(JsonArray EstablishmentDetailArray) {

        List<EstablishmentDetail> establishmentDetailList = new ArrayList<>();


        for (JsonElement EstablishmentDetailElement : EstablishmentDetailArray) {

            JsonObject EstablishmentDetailObject = EstablishmentDetailElement.getAsJsonObject();

            if (EstablishmentDetailObject.has("FHRSID")) {

                Long FHRSID = EstablishmentDetailObject.get("FHRSID").getAsLong();

                String LocalAuthorityBusinessID = EstablishmentDetailObject.get("LocalAuthorityBusinessID").getAsString();

                String BusinessName = EstablishmentDetailObject.get("BusinessName").getAsString();

                String BusinessType = EstablishmentDetailObject.get("BusinessType").getAsString();

                Long BusinessTypeID = EstablishmentDetailObject.get("BusinessTypeID").getAsLong();

                //Address lines and postcode don't exist for every record. This needs to be checked.
                String AddressLine1 = null;
                if (EstablishmentDetailObject.has("AddressLine1"))
                    AddressLine1 = EstablishmentDetailObject.get("AddressLine1").getAsString();

                String AddressLine2 = null;
                if (EstablishmentDetailObject.has("AddressLine2"))
                    AddressLine2 = EstablishmentDetailObject.get("AddressLine2").getAsString();

                String AddressLine3 = null;
                if (EstablishmentDetailObject.has("AddressLine3"))
                    AddressLine3 = EstablishmentDetailObject.get("AddressLine3").getAsString();

                String AddressLine4 = null;
                if (EstablishmentDetailObject.has("AddressLine4"))
                    AddressLine4 = EstablishmentDetailObject.get("AddressLine4").getAsString();

                String PostCode = null;
                if (EstablishmentDetailObject.has("PostCode"))
                    PostCode = EstablishmentDetailObject.get("PostCode").getAsString();


                String RatingValue = EstablishmentDetailObject.get("RatingValue").getAsString();

                String RatingKey = EstablishmentDetailObject.get("RatingKey").getAsString();

                //RatingDate can be a JsonObject containing value ["xsi:nil": "true"] (maybe parsing from XML) or can be just a date.
                String RatingDate;
                if (EstablishmentDetailObject.get("RatingDate") instanceof JsonObject) {
                    RatingDate = null;
                } else {
                    RatingDate = EstablishmentDetailObject.get("RatingDate").getAsString();
                }

                String Scores = EstablishmentDetailObject.get("Scores").getAsString();

                String SchemeType = EstablishmentDetailObject.get("SchemeType").getAsString();

                Boolean NewRatingPending = EstablishmentDetailObject.get("NewRatingPending").getAsBoolean();

                //Normalising the Geocode into Longitude and Latitude
                Double Longitude = null;
                Double Latitude = null;
                if (EstablishmentDetailObject.get("Geocode") instanceof JsonObject) {
                    JsonObject geoCodeObject = EstablishmentDetailObject.get("Geocode").getAsJsonObject();
                    Longitude = geoCodeObject.get("Longitude").getAsDouble();
                    Latitude = geoCodeObject.get("Latitude").getAsDouble();
                }

                EstablishmentDetail establishmentDetail = new EstablishmentDetail(FHRSID, LocalAuthorityBusinessID, BusinessName, BusinessType, BusinessTypeID,
                        AddressLine1, AddressLine2, AddressLine3, AddressLine4, PostCode, RatingValue, RatingKey, RatingDate, Scores, SchemeType,
                        NewRatingPending, Longitude, Latitude);

                establishmentDetailList.add(establishmentDetail);

            } else {
                System.out.println("\n\n getEstablishmentDetailList: Bad Json or doesn't contain FHRSID. Empty establishmentDetailList will be returned");
            }
        }

        System.out.println(establishmentDetailList);

        return establishmentDetailList;
    }


    @Override
    public LocalAuthority getLocalAuthority(JsonArray establishmentDetailArray) {

        //get first json object to map the Local Authority
        //Assuming that every JSON has only one local authority!
        JsonObject EstDetailObject = establishmentDetailArray.get(1).getAsJsonObject();

        Long LocalAuthorityCode = EstDetailObject.get("LocalAuthorityCode").getAsLong();

        String LocalAuthorityName = EstDetailObject.get("LocalAuthorityName").getAsString();

        String LocalAuthorityWebSite = EstDetailObject.get("LocalAuthorityWebSite").getAsString();

        String LocalAuthorityEmailAddress = EstDetailObject.get("LocalAuthorityEmailAddress").getAsString();

        LocalAuthority localAuthority = new LocalAuthority(LocalAuthorityCode, LocalAuthorityName, LocalAuthorityWebSite, LocalAuthorityEmailAddress);

        return localAuthority;
    }

}
