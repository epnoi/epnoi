package org.epnoi.storage.column;

import com.google.common.collect.Lists;
import com.google.common.primitives.Doubles;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cbadenes on 22/12/15.
 */
public class AnalysisColumnRepositoryTest extends BaseColumnRepositoryTest<AnalysisColumn> {

    @Autowired
    AnalysisColumnRepository repository;

    @Override
    public BaseColumnRepository<AnalysisColumn> getRepository() {
        return repository;
    }

    @Override
    public AnalysisColumn getEntity() {

        Map<String,List<Double>> scheme = new HashMap<>();
        scheme.put("items/72ce5395-6268-439a-947e-802229e7f022", Doubles.asList(0.4,0.3,0.3));
        scheme.put("items/72ce5395-6268-439a-947e-802229e7f455", Doubles.asList(0.1,0.7,0.3));


        AnalysisColumn column = new AnalysisColumn();
        column.setUri("relations/72ce5395-6268-439a-947e-802229e7f022");
        column.setCreationTime("2015-12-21T16:18:59Z");
        column.setType("topicModel");
        column.setConfiguration("alpha=16.1, beta=1.1, topics=8");
        column.setReport(scheme);
        column.setDomain("domains/72ce5395-6268-439a-947e-802229e7f022");
        return column;
    }
}
