package edu.ucmo.fightingmongeese.pinapp;

import edu.ucmo.fightingmongeese.pinapp.controllers.PinController;
import edu.ucmo.fightingmongeese.pinapp.models.Pin;
import edu.ucmo.fightingmongeese.pinapp.repository.PinRepository;
import edu.ucmo.fightingmongeese.pinapp.services.PinService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static edu.ucmo.fightingmongeese.pinapp.TestUtils.asJsonString;
import static edu.ucmo.fightingmongeese.pinapp.TestUtils.getCompletePin;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = PinController.class)
public class PinValidatorTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    PinService pinServiceMock;

    @MockBean
    PinRepository pinRepository;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }


    @Test()
    public void test_add_pin_with_missing_account() throws Exception {

        Pin pin = new Pin();
        pin.setCreate_user("user");
        when(pinServiceMock.add(any())).thenReturn(getCompletePin());
        when(pinRepository.findActivePin(any())).thenReturn(Optional.empty());
        mockMvc.perform(
                post("/api/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(pin)))
                .andExpect(status().isBadRequest());
    }

    @Test()
    public void test_add_pin_with_missing_create_user() throws Exception {

        Pin pin = new Pin();
        pin.setAccount("account");
        when(pinServiceMock.add(any())).thenReturn(getCompletePin());
        when(pinRepository.findActivePin(any())).thenReturn(Optional.empty());
        mockMvc.perform(
                post("/api/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(pin)))
                .andExpect(status().isBadRequest())
        .andDo(print());
    }


}
