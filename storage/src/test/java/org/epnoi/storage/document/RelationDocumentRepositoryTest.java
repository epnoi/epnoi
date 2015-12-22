package org.epnoi.storage.document;

import org.epnoi.storage.document.domain.RelationDocument;
import org.epnoi.storage.document.repository.BaseDocumentRepository;
import org.epnoi.storage.document.repository.RelationDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by cbadenes on 22/12/15.
 */
public class RelationDocumentRepositoryTest extends BaseDocumentRepositoryTest<RelationDocument> {

    @Autowired
    RelationDocumentRepository repository;

    @Override
    public BaseDocumentRepository<RelationDocument> getRepository() {
        return repository;
    }

    @Override
    public RelationDocument getEntity() {
        RelationDocument document = new RelationDocument();
        document.setUri("relations/72ce5395-6268-439a-947e-802229e7f022");
        document.setCreationTime("2015-12-21T16:18:59Z");
        document.setType("semantic");
        document.setDescribes("antonymy");
        document.setContent("white black");
        document.setAnalysis("analysis/72ce5395-6268-439a-947e-802229e7f022");
        return document;
    }
}
