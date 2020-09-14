package com.demo.fsapersist.gateway;

import com.demo.fsapersist.services.EstablishmentDetailServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
@ExtendWith(SpringExtension.class)
public class EstablishmentDetailFromExternalImplTest {

    @Value("${fsaUrl}")
    private String fsaUrl;

    @Value("${badContentFsaUrl}")
    private String badContentFsaUrl;

    @Value("${badUrl}")
    private String badUrl;

    @Autowired
    private EstablishmentDetailFromExternal establishmentDetailFromExternal;

    @MockBean
    private EstablishmentDetailServiceImpl establishmentDetailServiceImpl;


    @Test
    void testGetResponseSuccess() {
        Optional<?> response = establishmentDetailFromExternal.getResponse(fsaUrl);
        Assertions.assertTrue(response.isPresent(), "Response should be present");
        Assertions.assertTrue(response.get().toString().contains("FHRSEstablishment"), "Should contain FHRSEstablishment");
    }

    @Test
    void testGetResponseBadContents() {

        Optional<?> response = establishmentDetailFromExternal.getResponse(badContentFsaUrl);
        Assertions.assertFalse(response.isPresent(), "Response should not be present");
    }

    @Test
    void testGetResponseBadURL() {

        Optional<?> response = establishmentDetailFromExternal.getResponse(badUrl);
        Assertions.assertFalse(response.isPresent(), "Response should not be present");
    }




}
