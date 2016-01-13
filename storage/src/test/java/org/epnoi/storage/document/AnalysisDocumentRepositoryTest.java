package org.epnoi.storage.document;

import org.epnoi.storage.document.domain.AnalysisDocument;
import org.epnoi.storage.document.repository.AnalysisDocumentRepository;
import org.epnoi.storage.document.repository.BaseDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by cbadenes on 22/12/15.
 */
public class AnalysisDocumentRepositoryTest extends BaseDocumentRepositoryTest<AnalysisDocument> {

    @Autowired
    AnalysisDocumentRepository repository;

    @Override
    public BaseDocumentRepository<AnalysisDocument> getRepository() {
        return repository;
    }

    @Override
    public AnalysisDocument getEntity() {
        AnalysisDocument document = new AnalysisDocument();
        document.setUri("relations/72ce5395-6268-439a-947e-802229e7f022");
        document.setCreationTime("2015-12-21T16:18:59Z");
        document.setType("topicModel");
        document.setConfiguration("alpha=16.1, beta=1.1, topics=8");
        document.setDomain("domains/72ce5395-6268-439a-947e-802229e7f022");
        return document;
    }
}
