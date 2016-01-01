package org.epnoi.storage.model;

import org.epnoi.storage.column.domain.AnalysisColumn;
import org.junit.Test;

/**
 * Created by cbadenes on 01/01/16.
 */
public class ResourceUtilsTest {

    @Test
    public void copy(){

        Analysis analysis = new Analysis();
        analysis.setConfiguration("conf1");
        analysis.setDescription("descr1");
        analysis.setDomain("domain1");
        analysis.setReport(new Integer(1));
        analysis.setType("type1");
        analysis.setCreationTime("time1");
        analysis.setUri("uri1");

        System.out.println("Analysis: "+ analysis);

        AnalysisColumn wrapper = ResourceUtils.map(analysis, AnalysisColumn.class);

        System.out.println(wrapper);


    }

}
