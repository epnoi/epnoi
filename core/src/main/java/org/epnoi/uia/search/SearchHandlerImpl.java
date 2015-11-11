package org.epnoi.uia.search;

import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.modules.Core;
import org.epnoi.model.modules.SearchHandler;
import org.epnoi.model.search.*;
import org.epnoi.uia.search.organize.SearchOrganizer;
import org.epnoi.uia.search.project.SearchProjector;
import org.epnoi.uia.search.select.SearchSelector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.logging.Logger;

@Component
public class SearchHandlerImpl implements SearchHandler {
    private static final Logger logger = Logger.getLogger(SearchHandlerImpl.class
            .getName());
    @Autowired
    private Core core;

    private SearchSelector selector;
    private SearchOrganizer organizer;
    private SearchProjector projector;

    // --------------------------------------------------------------------------------------

    public SearchHandlerImpl() {
    }

    @PostConstruct
    public void init() throws EpnoiInitializationException {
        logger.info("Initializing the search handler");

        this.selector = new SearchSelector(this.core);
        this.organizer = new SearchOrganizer(this.core);
        this.projector = new SearchProjector(this.core);
    }

    // --------------------------------------------------------------------------------------

    public SearchResult search(SelectExpression selectExpression,
                               SearchContext searchContext) {
        logger.info("Handling a search request with the following parameters:");
        logger.info("SelectExpression: " + selectExpression);
        logger.info("SearchContext: " + searchContext);
        SearchSelectResult searchSelectResult = this.selector.select(
                selectExpression, searchContext);
        SearchOrganizationResult searchOrganizationResult = this.organizer
                .organize(searchSelectResult);
        SearchResult searchResult = this.projector
                .project(searchOrganizationResult);

        return searchResult;
    }

}
