package org.epnoi.uia.informationstore.dao.map;

import org.epnoi.model.Content;
import org.epnoi.model.Selector;
import org.epnoi.model.parameterization.MapInformationStoreParameters;
import org.epnoi.uia.informationstore.SelectorHelper;

public class WikipediaPageMapDAO extends MapDAO {

    // --------------------------------------------------------------------------------

    @Override
    public Content<Object> getAnnotatedContent(Selector selector) {

        Content<Object> annotatedContent = (Content<Object>) map.get(selector
                .getProperty(SelectorHelper.ANNOTATED_CONTENT_URI));
        return annotatedContent;
    }

    // --------------------------------------------------------------------------------

    @Override
    public void setAnnotatedContent(Selector selector,
                                    Content<Object> annotatedContent) {

        map.put(selector.getProperty(SelectorHelper.ANNOTATED_CONTENT_URI),
                annotatedContent);
        database.commit();

    }

    public static void main(String[] args) {
        WikipediaPageMapDAO dao = new WikipediaPageMapDAO();
        MapInformationStoreParameters parameters = new MapInformationStoreParameters();
        parameters.setPath("/opt/epnoi/epnoideployment/mapDB/epnoi/epnoi");
        dao.init(parameters);
        for (String key : dao.map.keySet()) {
            System.out.println("-------> " + key);
        }

    }
}
