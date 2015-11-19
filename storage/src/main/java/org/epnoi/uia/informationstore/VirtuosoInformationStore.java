package org.epnoi.uia.informationstore;

import org.epnoi.model.Context;
import org.epnoi.model.Resource;
import org.epnoi.model.Selector;
import org.epnoi.model.modules.InformationStore;
import org.epnoi.model.parameterization.InformationStoreParameters;
import org.epnoi.model.parameterization.VirtuosoInformationStoreParameters;
import org.epnoi.model.search.SearchContext;
import org.epnoi.model.search.SearchSelectResult;
import org.epnoi.model.search.SelectExpression;
import org.epnoi.uia.informationstore.dao.rdf.RDFDAO;
import org.epnoi.uia.informationstore.dao.rdf.RDFDAOFactory;
import org.epnoi.uia.informationstore.dao.rdf.RDFDAOQueryResolver;

import java.util.List;

public class VirtuosoInformationStore implements InformationStore {
    VirtuosoInformationStoreParameters parameters;
    RDFDAOFactory daoFactory;
    RDFDAOQueryResolver queryResolver;

    // ------------------------------------------------------------------------

    public void close() {
        // TODO Auto-generated method stub

    }

    // ------------------------------------------------------------------------

    public void init(InformationStoreParameters parameters) {

        this.parameters = (VirtuosoInformationStoreParameters) parameters;
        this.daoFactory = new RDFDAOFactory(this.parameters);
        this.queryResolver = new RDFDAOQueryResolver();
        this.queryResolver.init(this.parameters);
    }

    // ------------------------------------------------------------------------

    public boolean test() {
        return RDFDAO.test(this.parameters);
    }

    // ------------------------------------------------------------------------

    public InformationStoreParameters getParameters() {
        return this.parameters;
    }

    // ------------------------------------------------------------------------

    public void put(Resource resource, Context context) {
        RDFDAO rdfDAO = this.daoFactory.build(resource);
        rdfDAO.create(resource, context);

    }

    // ------------------------------------------------------------------------

    public Resource get(Selector selector) {
        RDFDAO dao = this.daoFactory.build(selector);

        Resource resource = dao.read(selector.getProperty(SelectorHelper.URI));
        return resource;
    }

    // ------------------------------------------------------------------------

    public void remove(Selector selector) {
        RDFDAO dao = this.daoFactory.build(selector);

        dao.remove(selector.getProperty(SelectorHelper.URI));

    }

    // ------------------------------------------------------------------------

    public List<String> query(String queryExpression) {

        return this.queryResolver.query(queryExpression);
    }

    // ------------------------------------------------------------------------

    @Override
    public SearchSelectResult query(SelectExpression selectionExpression,
                                    SearchContext searchContext) {
        // TODO Auto-generated method stub
        return null;
    }

    // ------------------------------------------------------------------------

    public void update(Resource resource) {
        RDFDAO rdfDAO = this.daoFactory.build(resource);

        rdfDAO.update(resource);
    }

    // ------------------------------------------------------------------------

    @Override
    public boolean exists(Selector selector) {

        return this.queryResolver.exists(selector);

    }

    // ------------------------------------------------------------------------

    public String getType(String URI) {

        return this.queryResolver.getType(URI);

    }

    // ------------------------------------------------------------------------
}
