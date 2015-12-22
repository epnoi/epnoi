package org.epnoi.storage.column;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.storage.Config;
import org.epnoi.storage.model.Resource;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.repository.MapId;
import org.springframework.data.cassandra.repository.support.BasicMapId;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by cbadenes on 21/12/15.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
@TestPropertySource(properties = { "epnoi.cassandra.contactpoints = zavijava.dia.fi.upm.es", "epnoi.cassandra.port = 5011", "epnoi.cassandra.keyspace = research" })
public abstract class BaseColumnRepositoryTest<T extends Resource> {

    public abstract BaseColumnRepository<T> getRepository();

    public abstract T getEntity();

    @Test
    public void crud(){
        long count1 = getRepository().count();

        T entity = getEntity();
        T column = getRepository().save(entity);
        Assert.assertEquals(entity, column);

        long count2 = getRepository().count();
        Assert.assertEquals(count1 + 1l, count2);

        getRepository().delete(entity);

        long count3 = getRepository().count();
        Assert.assertEquals(count1, count3);
    }


    @Test
    public void findOne(){
        T entity = getEntity();

        T found = getRepository().findOne(BasicMapId.id("uri",entity.getUri()));
        Assert.assertNull(found);

        getRepository().save(entity);

        found = getRepository().findOne(BasicMapId.id("uri",entity.getUri()));
        Assert.assertNotNull(found);

        getRepository().delete(found);
    }


}
