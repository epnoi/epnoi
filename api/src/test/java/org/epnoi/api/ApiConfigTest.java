package org.epnoi.api;

import es.cbadenes.lab.test.IntegrationTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by rgonzalez on 3/12/15.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = org.epnoi.api.ApiConfig.class)
@ActiveProfiles("develop")
//@TestPropertySource(properties = { "epnoi.eventbus.uri = localhost", "storage.path = target/storage" })
public class ApiConfigTest {


    @Test
    public void startContext() {
        assert(true);

    }

}
