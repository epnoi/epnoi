package org.epnoi.storage.column;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by cbadenes on 22/12/15.
 */
public class TopicColumnRepositoryTest extends BaseColumnRepositoryTest<TopicColumn> {

    @Autowired
    TopicColumnRepository repository;

    @Override
    public BaseColumnRepository<TopicColumn> getRepository() {
        return repository;
    }

    @Override
    public TopicColumn getEntity() {
        TopicColumn column = new TopicColumn();
        column.setUri("topics/72ce5395-6268-439a-947e-802229e7f022");
        column.setCreationTime("2015-12-21T16:18:59Z");
        column.setContent("molecular color graphic rendering");
        column.setAnalysis("analysis/72ce5395-6268-439a-947e-802229e7f022");
        return column;
    }
}
