package org.epnoi.uia.domains;

import org.epnoi.model.Domain;
import org.epnoi.model.ResearchObject;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.modules.Core;
import org.epnoi.model.modules.DomainsHandler;
import org.epnoi.model.rdf.RDFHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
public class DomainsHandlerImpl implements DomainsHandler {
    private static final Logger logger = Logger.getLogger(DomainsHandlerImpl.class
            .getName());
    @Autowired
    private Core core;

    // -----------------------------------------------------------------------------------

    @Override
    @PostConstruct
    public void init() throws EpnoiInitializationException {
        logger.info("Initializing the domains handler");

    }

    // -----------------------------------------------------------------------------------

    @Override
    public List<String> gather(Domain domain) {
        logger.info("Gathering the domain URIs");

        logger.info("Gathering the domain " + domain);
        // First we calculate those defined extensionally (i.e. resources that
        // have been explicitly stated as belonging to the domain)
        List<String> foundURIs = _extractExtensionallySpecifiedResources(domain);
/*

        for (String uri : _extractIntensionallySpecifiedResources(domain)) {
            if (!foundURIs.contains(uri)) {
                foundURIs.add(uri);
            }
        }

        List<String> cleanedURI = _cleanMissingAndRepeatedResources(foundURIs, domain);
        return cleanedURI;
*/
        return  foundURIs;
    }

    // -----------------------------------------------------------------------------------

    private List<String> _extractIntensionallySpecifiedResources(Domain domain) {
        logger.info("Domain type "+domain.getType()+" and domain label "+domain.getLabel());
        logger.info("DOMAIN "+domain);
        List<String> foundURIs = core.getAnnotationHandler().getLabeledAs(
                domain.getUri(), domain.getType());

        logger.info("Found initially " + foundURIs.size()
                + " elements in the domain " + domain.getUri());
        return foundURIs;
    }

    // -----------------------------------------------------------------------------------

    private List<String> _extractExtensionallySpecifiedResources(Domain domain) {

        ResearchObject resources = (ResearchObject) core
                .getInformationHandler().get(domain.getResources(),
                        RDFHelper.RESEARCH_OBJECT_CLASS);

        System.out.println("( " + domain.getResources() + " )RESOUUCE OBJSCT " + resources);
        if (resources != null) {
            List<String> foundURIs = resources.getAggregatedResources();
            if (foundURIs != null) {
                logger.info("Initially " + foundURIs.size()
                        + " are defined as belonging to the domain " + domain.getUri());

                return foundURIs;

            }
        }
        return new ArrayList<String>();

    }

    // -----------------------------------------------------------------------------------

    /**
     * Method that removes from a list of resources URIs those that are not
     * stored in the UIA.
     *
     * @param foundURIs List of URIs that were initially found for the domain
     * @param domain    The current domain
     * @return
     */
    private List<String> _cleanMissingAndRepeatedResources(List<String> foundURIs,
                                                           Domain domain) {
        List<String> cleanedURIs = new ArrayList<>();

        for (String uri : foundURIs.stream().distinct().collect(Collectors.toList())) {
            //System.out.println(">>> "+domain);
            if (core.getInformationHandler().contains(uri, domain.getType())) {
                cleanedURIs.add(uri);
            }
        }

        return cleanedURIs;
    }
}
