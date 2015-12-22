package org.epnoi.storage.document;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by cbadenes on 22/12/15.
 */
public class WordDocumentRepositoryTest extends BaseDocumentRepositoryTest<WordDocument> {

    @Autowired
    WordDocumentRepository repository;

    @Override
    public BaseDocumentRepository<WordDocument> getRepository() {
        return repository;
    }

    @Override
    public WordDocument getEntity() {
        WordDocument document = new WordDocument();
        document.setUri("words/72ce5395-6268-439a-947e-802229e7f022");
        document.setCreationTime("2015-12-21T16:18:59Z");
        document.setContent("molecular");
        document.setLemma("molecula");
        document.setStem("molecula");
        document.setPos("NN");
        document.setType("term");
        return document;
    }
}
