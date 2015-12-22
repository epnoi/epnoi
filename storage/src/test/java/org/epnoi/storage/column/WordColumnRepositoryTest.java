package org.epnoi.storage.column;

import org.epnoi.storage.column.domain.WordColumn;
import org.epnoi.storage.column.repository.BaseColumnRepository;
import org.epnoi.storage.column.repository.WordColumnRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by cbadenes on 22/12/15.
 */
public class WordColumnRepositoryTest extends BaseColumnRepositoryTest<WordColumn> {

    @Autowired
    WordColumnRepository repository;

    @Override
    public BaseColumnRepository<WordColumn> getRepository() {
        return repository;
    }

    @Override
    public WordColumn getEntity() {
        WordColumn column = new WordColumn();
        column.setUri("words/72ce5395-6268-439a-947e-802229e7f022");
        column.setCreationTime("2015-12-21T16:18:59Z");
        column.setContent("molecular");
        column.setLemma("molecula");
        column.setStem("molecula");
        column.setPos("NN");
        column.setType("term");
        return column;
    }
}
