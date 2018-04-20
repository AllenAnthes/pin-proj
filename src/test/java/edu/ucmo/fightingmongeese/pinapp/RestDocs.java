package edu.ucmo.fightingmongeese.pinapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.ucmo.fightingmongeese.pinapp.components.DateTime;
import edu.ucmo.fightingmongeese.pinapp.models.Pin;
import edu.ucmo.fightingmongeese.pinapp.repository.PinRepository;
import edu.ucmo.fightingmongeese.pinapp.services.PinService;
import edu.ucmo.fightingmongeese.pinapp.validators.UnclaimedValidator;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static edu.ucmo.fightingmongeese.pinapp.TestUtils.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedRequestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:test.properties")
@SpringBootTest
public class RestDocs {

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private PinRepository pinRepository;

    @InjectMocks
    private PinService pinService;

    @Autowired
    private ObjectMapper objectMapper;

    @SpyBean
    UnclaimedValidator unclaimedValidator;

    @Mock
    private DateTime dateTime;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .build();
        when(pinRepository.findByPin(any(String.class))).thenReturn(Optional.empty());
        when(pinRepository.findActivePin(any(String.class))).thenReturn(Optional.empty());
        when(pinRepository.save(any(Pin.class))).thenReturn(getAddDBResponse());
        when(dateTime.now()).thenReturn(mockedTime);
    }

    @Test
    public void add() throws Exception {
        when(pinRepository.save(any(Pin.class))).thenReturn(getAddDBResponse());
        Pin pin = new Pin();
        pin.setCreate_user("someUser");
        pin.setAccount("someAccount");

        this.mockMvc.perform(post("/api/new")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(pin)))
                .andExpect(status().isCreated())
                .andDo(document("{methodName}", relaxedRequestFields(RestDocsUtils.addPinDescriptors()),
                        relaxedResponseFields(RestDocsUtils.addPinResponseDescriptors())));
    }

    @Test
    public void claim() throws Exception {
        when(pinRepository.save(any(Pin.class))).thenReturn(getClaimResponsePin());
        when(pinRepository.findByPin(any(String.class))).thenReturn(Optional.of(getClaimRequestPin()));
        Pin pin = new Pin();
        pin.setClaim_user("someUser");
        pin.setAccount("someUser");
        pin.setPin("1234566");

        MvcResult result = this.mockMvc.perform(post("/api/claim")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(pin)))
                .andExpect(status().isOk())
                .andDo(document("{methodName}", relaxedRequestFields(RestDocsUtils.claimPinDescriptors()),
                        relaxedResponseFields(RestDocsUtils.claimPinResponseDescriptors()))).andReturn();
        System.out.println("result: " + result.getResponse().getContentAsString());
    }

    @Test
    public void cancel() throws Exception {
        when(pinRepository.save(any(Pin.class))).thenReturn(getClaimResponsePin());
        when(pinRepository.findByPin(any(String.class))).thenReturn(Optional.of(getClaimRequestPin()));
        Pin pin = new Pin();
        pin.setClaim_user("someUser");
        pin.setAccount("someUser");
        pin.setPin("1234566");

        MvcResult result = this.mockMvc.perform(post("/api/cancel")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(pin)))
                .andExpect(status().isOk())
                .andDo(document("{methodName}", relaxedRequestFields(RestDocsUtils.cancelPinDescriptors()),
                        relaxedResponseFields(RestDocsUtils.claimPinResponseDescriptors()))).andReturn();
        System.out.println("result: " + result.getResponse().getContentAsString());
    }

}
