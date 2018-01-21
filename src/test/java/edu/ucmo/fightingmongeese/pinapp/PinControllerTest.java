package edu.ucmo.fightingmongeese.pinapp;

import edu.ucmo.fightingmongeese.pinapp.controllers.PinController;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(PinController.class)
public class PinControllerTest {

    @Autowired
    MockMvc mockMvc;

    /*
     * TODOs
     * Valid new without expire time
     * Valid new with expire time
     * Invalid new early expire time (throws correct exception)
     * Invalid new no user ''
     * Valid claim
     * Invalid claim (various permutations of user not found, wrong PIN, wrong user, etc)
     */

}
