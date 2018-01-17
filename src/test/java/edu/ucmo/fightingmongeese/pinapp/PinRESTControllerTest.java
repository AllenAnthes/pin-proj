package edu.ucmo.fightingmongeese.pinapp;

import edu.ucmo.fightingmongeese.pinapp.controllers.PinRESTController;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(PinRESTController.class)
public class PinRESTControllerTest {

    @Autowired
    MockMvc mockMvc;

}
