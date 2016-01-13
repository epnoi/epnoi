package org.epnoi.storage.column;

import org.epnoi.storage.column.domain.WordColumn;
import org.epnoi.storage.column.repository.BaseColumnRepository;
import org.epnoi.storage.column.repository.WordColumnRepository;
import org.epnoi.storage.model.ResourceUtils;
import org.epnoi.storage.model.Word;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.repository.MapId;
import org.springframework.data.cassandra.repository.support.BasicMapId;

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

    @Test
    public void findByLemma(){

        String wordURI = "words/72ce5395-6268-439a-947e-802229e7f022";
        repository.delete(BasicMapId.id(ResourceUtils.URI,wordURI));

        Iterable<WordColumn> res1 = repository.findByLemma("sample");
        Assert.assertFalse(res1.iterator().hasNext());

        WordColumn sample = new WordColumn();
        sample.setUri(wordURI);
        sample.setCreationTime("20160112T1533");
        sample.setContent("samples");
        sample.setLemma("sample");
        sample.setPos("n");
        sample.setStem("sampl");
        repository.save(sample);

        Iterable<WordColumn> res2 = repository.findByLemma("sample");
        Assert.assertTrue(res2.iterator().hasNext());

    }

    @Test
    public void findByContent(){

        String wordURI = "words/72ce5395-6268-439a-947e-802229e7f022";
        repository.delete(BasicMapId.id(ResourceUtils.URI,wordURI));

        Iterable<WordColumn> res1 = repository.findByContent("samples");
        Assert.assertFalse(res1.iterator().hasNext());

        WordColumn sample = new WordColumn();
        sample.setUri(wordURI);
        sample.setCreationTime("20160112T1533");
        sample.setContent("samples");
        sample.setLemma("sample");
        sample.setPos("n");
        sample.setStem("sampl");
        repository.save(sample);

        Iterable<WordColumn> res2 = repository.findByContent("samples");
        Assert.assertTrue(res2.iterator().hasNext());

    }

}
