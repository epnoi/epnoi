package org.epnoi.storage.column;

import org.epnoi.storage.column.domain.RelationColumn;
import org.epnoi.storage.column.repository.BaseColumnRepository;
import org.epnoi.storage.column.repository.RelationColumnRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by cbadenes on 22/12/15.
 */
public class RelationColumnRepositoryTest extends BaseColumnRepositoryTest<RelationColumn> {

    @Autowired
    RelationColumnRepository repository;

    @Override
    public BaseColumnRepository<RelationColumn> getRepository() {
        return repository;
    }

    @Override
    public RelationColumn getEntity() {
        RelationColumn column = new RelationColumn();
        column.setUri("relations/72ce5395-6268-439a-947e-802229e7f022");
        column.setCreationTime("2015-12-21T16:18:59Z");
        column.setType("semantic");
        column.setDescribes("antonymy");
        column.setContent("white black");
        column.setAnalysis("analysis/72ce5395-6268-439a-947e-802229e7f022");
        return column;
    }
}
