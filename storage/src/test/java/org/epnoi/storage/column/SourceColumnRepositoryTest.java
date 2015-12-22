package org.epnoi.storage.column;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by cbadenes on 22/12/15.
 */
public class SourceColumnRepositoryTest extends BaseColumnRepositoryTest<SourceColumn> {

    @Autowired
    SourceColumnRepository repository;

    @Override
    public BaseColumnRepository<SourceColumn> getRepository() {
        return repository;
    }

    @Override
    public SourceColumn getEntity() {
        SourceColumn column = new SourceColumn();
        column.setUri("sources/72ce5395-6268-439a-947e-802229e7f022");
        column.setCreationTime("2015-12-21T16:18:59Z");
        column.setName("test");
        column.setDescription("for testing purposes");
        column.setUrl("http://epnoi.org");
        column.setProtocol("oaipmh");
        column.setDomain("domains/72ce5395-6268-439a-947e-802229e7f022");
        return column;
    }
}
