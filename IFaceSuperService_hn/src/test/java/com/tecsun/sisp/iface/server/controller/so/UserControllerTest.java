package com.tecsun.sisp.iface.server.controller.so;

import com.tecsun.sisp.iface.common.util.Constants;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;

public class UserControllerTest {
    @Test public void rejectsMissingPlaceholderAndUnknownChannel() {
        Map<String, String> config = new HashMap<>();
        String channel = Constants.TSB;
        assertFalse(UserController.validCredentials(channel, "name", "secret", config::get));
        config.put("auth.TSB.username", "name");
        config.put("auth.TSB.password", "CHANGE_ME");
        assertFalse(UserController.validCredentials(channel, "name", "CHANGE_ME", config::get));
        config.put("auth.TSB.password", "secret");
        assertFalse(UserController.validCredentials("unknown", "name", "secret", config::get));
        assertFalse(UserController.validCredentials(channel, null, "secret", config::get));
        assertFalse(UserController.validCredentials(channel, "name", "wrong", config::get));
        assertTrue(UserController.validCredentials(channel, "name", "secret", config::get));
    }
}
