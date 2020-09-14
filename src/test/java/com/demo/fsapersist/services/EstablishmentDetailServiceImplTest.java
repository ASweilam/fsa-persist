package com.demo.fsapersist.services;

import com.demo.fsapersist.gateway.EstablishmentDetailFromExternalImpl;
import com.demo.fsapersist.models.EstablishmentDetail;
import com.demo.fsapersist.models.LocalAuthority;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
class EstablishmentDetailServiceImplTest {

    private final SessionFactory sessionFactory;
    private Session session = null;

    @Value("${fsaUrl}")
    private String fsaUrl;

    @Autowired
    private EstablishmentDetailService establishmentDetailService;

    @MockBean
    private EstablishmentDetailFromExternalImpl establishmentDetailFromExternal;

    EstablishmentDetailServiceImplTest() {
        sessionFactory = new Configuration()
                .configure("hibernate.cfg-test.xml")
                .addAnnotatedClass(EstablishmentDetail.class)
                .addAnnotatedClass(LocalAuthority.class)
                .buildSessionFactory();
    }

    @After
    public void after() {
        session.close();
        sessionFactory.close();
    }


    @Test
    void saveEstablishmentsNullInputs() {

        List<EstablishmentDetail> establishmentDetails = new ArrayList<>();
        LocalAuthority localAuthority = null;

        //Start a new session
        session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        Assertions.assertFalse(establishmentDetailService.saveEstablishments(establishmentDetails, localAuthority, session), "Should be false");

    }

    @Test
    void saveEstablishmentsBadInputs() {

        List<EstablishmentDetail> establishmentDetails = new ArrayList<>();
        LocalAuthority localAuthority = null;

        //Start a new session
        session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        Assertions.assertFalse(establishmentDetailService.saveEstablishments(establishmentDetails, localAuthority, session), "Should be false");
    }

    @Test
    @Transactional
    public void givenESTArrSaveAndCompareEstimationDetails_DB() throws FileNotFoundException {

        BufferedReader br = new BufferedReader(new FileReader("src\\test\\resources\\json-test\\estDet-test.json"));

        JsonArray jsonArray = JsonParser.parseReader(br).getAsJsonArray();

        List<EstablishmentDetail> establishmentDetailList = establishmentDetailService.getEstablishmentDetailList(jsonArray);
        LocalAuthority localAuthority = establishmentDetailService.getLocalAuthority(jsonArray);

        session = sessionFactory.getCurrentSession();

        establishmentDetailService.saveEstablishments(establishmentDetailList, localAuthority, session);

        //Start a new session to get objects from DB for assertion
        session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        Long idOne = Long.valueOf(1098019);
        Long idTwo = Long.valueOf(960429);

        EstablishmentDetail expectedOneFromDb = session.get(EstablishmentDetail.class, idOne);
        EstablishmentDetail expectedOTwoFromDb = session.get(EstablishmentDetail.class, idTwo);

        session.getTransaction().commit();

        //Assertions
        Assertions.assertEquals(expectedOneFromDb.getFhrs_id(), establishmentDetailList.get(0).getFhrs_id(), "Should match");
        Assertions.assertEquals(expectedOneFromDb.getLocal_authority_business_id(), establishmentDetailList.get(0).getLocal_authority_business_id(), "Should match");
        Assertions.assertEquals(expectedOneFromDb.getBusiness_name(), establishmentDetailList.get(0).getBusiness_name(), "Should match");
        Assertions.assertEquals(expectedOneFromDb.getBusiness_type(), establishmentDetailList.get(0).getBusiness_type(), "Should match");
        Assertions.assertEquals(expectedOneFromDb.getBusiness_type_id(), establishmentDetailList.get(0).getBusiness_type_id(), "Should match");
        Assertions.assertEquals(expectedOneFromDb.getAddress_line_1(), establishmentDetailList.get(0).getAddress_line_1(), "Should match");
        Assertions.assertEquals(expectedOneFromDb.getAddress_line_2(), establishmentDetailList.get(0).getAddress_line_2(), "Should match");
        Assertions.assertEquals(expectedOneFromDb.getAddress_line_3(), establishmentDetailList.get(0).getAddress_line_3(), "Should match");
        Assertions.assertEquals(expectedOneFromDb.getAddress_line_4(), establishmentDetailList.get(0).getAddress_line_4(), "Should match");

        Assertions.assertEquals(expectedOneFromDb.getPostcode(), establishmentDetailList.get(0).getPostcode(), "Should match");
        //continue....

    }


    @Test
    @Transactional
    public void givenESTArrSaveAndCompareLocalAuthority_DB() throws FileNotFoundException {

        BufferedReader br = new BufferedReader(new FileReader("src\\test\\resources\\json-test\\estDet-test.json"));

        JsonArray jsonArray = JsonParser.parseReader(br).getAsJsonArray();

        List<EstablishmentDetail> establishmentDetailList = establishmentDetailService.getEstablishmentDetailList(jsonArray);
        LocalAuthority localAuthority = establishmentDetailService.getLocalAuthority(jsonArray);

        session = sessionFactory.getCurrentSession();

        establishmentDetailService.saveEstablishments(establishmentDetailList, localAuthority, session);

        //Start a new session to get objects from DB for assertion
        session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        // Local Authority Code to grab from DB
        Long idOne = Long.valueOf(774);

        LocalAuthority expectedLocalAuthorityFromDB = session.get(LocalAuthority.class, idOne);

        session.getTransaction().commit();

        //Assertions
        Assertions.assertEquals(expectedLocalAuthorityFromDB.getCode(), localAuthority.getCode(), "Should match");
        Assertions.assertEquals(expectedLocalAuthorityFromDB.getName(), localAuthority.getName(), "Should match");

    }




    @Test
    void testGetEstablishmentDetailList_Success() {

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("src\\test\\resources\\json-test\\estDet-test.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        JsonArray jsonArray = JsonParser.parseReader(br).getAsJsonArray();

        Assertions.assertTrue(establishmentDetailService.getEstablishmentDetailList(jsonArray) instanceof List);

        Assertions.assertTrue(establishmentDetailService.getEstablishmentDetailList(jsonArray).get(0) instanceof EstablishmentDetail);

        List<EstablishmentDetail> establishmentDetails = establishmentDetailService.getEstablishmentDetailList(jsonArray);

        Assertions.assertFalse(establishmentDetails.get(0).toString().contains("\"Geocode\" :")
                , "Should not contain a Geocode Object");

        Assertions.assertEquals(56.017, establishmentDetails.get(0).getLatitude(), 0.1, "Should be equal");

        Assertions.assertNull(establishmentDetails.get(0).getRating_date(), "Should be null as it contains xsi:nil");

        Assertions.assertNull(establishmentDetails.get(1).getLatitude(), "Should be null as it had empty Geocode");
        Assertions.assertNull(establishmentDetails.get(1).getLongitude(), "Should be null as it had empty Geocode");
    }

    @Test
    void testGetEstablishmentDetailListFailBadInput() throws Exception {

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("src\\test\\resources\\json-test\\not-estDetList-test.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        assert br != null;
        JsonArray jsonArray = JsonParser.parseReader(br).getAsJsonArray();

        Assertions.assertEquals(establishmentDetailService.getEstablishmentDetailList(jsonArray), new ArrayList<EstablishmentDetail>()
                , "Should return an empty List<EstablishmentDetail>");

    }


    @Test
    void testLocalAuthorityReturnLA_Success(){

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("src\\test\\resources\\json-test\\estDet-test.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        JsonArray jsonArray = JsonParser.parseReader(br).getAsJsonArray();

        Assertions.assertTrue(establishmentDetailService.getLocalAuthority(jsonArray) instanceof LocalAuthority);
    }


    @Test
    void testLocalAuthorityReturnMatching_Success(){

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("src\\test\\resources\\json-test\\estDet-test.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        JsonArray jsonArray = JsonParser.parseReader(br).getAsJsonArray();

        LocalAuthority actualLA =  establishmentDetailService.getLocalAuthority(jsonArray);

        Assertions.assertEquals(  Long.valueOf(774), actualLA.getCode(), "Should be the same");
        Assertions.assertEquals(  "Falkirk", actualLA.getName(), "Should be the same");


    }





}