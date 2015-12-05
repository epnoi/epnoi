package org.epnoi;

import es.cbadenes.lab.test.IntegrationTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by rgonzalez on 3/12/15.
 */
/*COMMENTED SINCE NEEDS A LOT OF LOCAL DATA
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = org.epnoi.EpnoiConfig.class)
@ActiveProfiles("develop")
@TestPropertySource(properties = { "epnoi.config.path = testUIA.xml" })
*/
public class CoreTest {


    @Test
    public void startContext() {
        assert(true);

    }

}
