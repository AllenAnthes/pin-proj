package edu.ucmo.fightingmongeese.pinapp;


import edu.ucmo.fightingmongeese.pinapp.config.RequestInterceptor;
import edu.ucmo.fightingmongeese.pinapp.controllers.PinController;
import edu.ucmo.fightingmongeese.pinapp.models.Pin;
import edu.ucmo.fightingmongeese.pinapp.repository.PinRepository;
import edu.ucmo.fightingmongeese.pinapp.services.PinService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static edu.ucmo.fightingmongeese.pinapp.TestUtils.asJsonString;
import static edu.ucmo.fightingmongeese.pinapp.TestUtils.getCompletePin;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class RequestValidationIntegrationTests {


    private MockMvc mockMVc;


    @Configuration
    static class ContextConfiguration {

        //         this bean will be injected into the OrderServiceTest class
        @Bean("RequestInterceptor")
        public RequestInterceptor requestInterceptor() {
            return new RequestInterceptor();
        }
    }


    @MockBean
    private PinService pinService;

    @MockBean
    private PinRepository pinRepository;

    @InjectMocks
    private PinController pinController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMVc = MockMvcBuilders
                .standaloneSetup(pinController)
                .build();
        when(pinRepository.findActivePin(any(String.class))).thenReturn(Optional.empty());
        when(pinService.add(any(Pin.class))).thenReturn(getCompletePin());
        System.setProperty("JDBC_DATABASE_URL", "jdbc:h2:mem:pindbtest");
        System.setProperty("JDBC_DATABASE_USERNAME", "sa");
        System.setProperty("JDBC_DATABASE_PASSWORD", "");
    }

    @Test
    public void test_add_validator_fails_on_missing_accout() throws Exception {
        Pin pin = new Pin();
        pin.setClaim_user("user");

        when(pinService.add(any(Pin.class))).thenReturn(getCompletePin());


        mockMVc.perform(post("/api/new")
                .content(asJsonString(pin))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(pinService, times(1)).add(any(Pin.class));
        verify(pinController, times(1)).add(any(), any());
    }


}
