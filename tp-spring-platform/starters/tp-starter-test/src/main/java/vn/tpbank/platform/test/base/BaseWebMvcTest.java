package vn.tpbank.platform.test.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

public abstract class BaseWebMvcTest {
    @Autowired
    protected MockMvc mockMvc;
}
