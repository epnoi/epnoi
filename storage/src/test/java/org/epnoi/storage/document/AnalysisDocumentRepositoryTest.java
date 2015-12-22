package org.epnoi.storage.document;

import com.google.common.primitives.Doubles;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Map<String,List<Double>> scheme = new HashMap<>();
        scheme.put("items/72ce5395-6268-439a-947e-802229e7f022", Doubles.asList(0.4,0.3,0.3));
        scheme.put("items/72ce5395-6268-439a-947e-802229e7f455", Doubles.asList(0.1,0.7,0.3));
        
        AnalysisDocument document = new AnalysisDocument();
        document.setUri("relations/72ce5395-6268-439a-947e-802229e7f022");
        document.setCreationTime("2015-12-21T16:18:59Z");
        document.setType("topicModel");
        document.setConfiguration("alpha=16.1, beta=1.1, topics=8");
        document.setReport(scheme);
        document.setDomain("domains/72ce5395-6268-439a-947e-802229e7f022");
        return document;
    }
}
