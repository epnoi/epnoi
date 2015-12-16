package org.epnoi.api.thrift.services;

import org.apache.thrift.TException;
import org.epnoi.model.RelationHelper;
import org.epnoi.model.modules.Core;
import org.epnoi.model.services.thrift.KnowledgeBaseService;
import org.epnoi.model.services.thrift.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.logging.Logger;

/**
 * Created by rgonza on 12/12/15.
 */
@Component
public class KnowledgeBaseServiceHandler extends ThriftServiceHandler implements KnowledgeBaseService.Iface {
    private static final Logger logger = Logger.getLogger(KnowledgeBaseServiceHandler.class
            .getName());

    private static Map<String, String> resourceTypesTable = new HashMap<String, String>();
    private static Set<String> validRelationTypes = new HashSet<String>();

    static {
        resourceTypesTable.put("hypernymy", RelationHelper.HYPERNYMY);
        resourceTypesTable.put("mereology", RelationHelper.MEREOLOGY);
        validRelationTypes.add("hypernymy");
        validRelationTypes.add("mereology");
    }

    public KnowledgeBaseServiceHandler() {

    }


    @Override
    public String getService() {
        return Services.KNOWLEDGEBASE.name();
    }


    @Autowired
    Core core;


    @Override
    public Map<String, List<String>> getRelated(List<String> sources, String type) throws TException {
        logger.info("--------------------------------------------------------------_____> "+sources+" type> "+type);
        Map<String, List<String>> sourcesTargets = new HashMap<>();

        if (sources != null) {
logger.info("The type is valid");
            type = resourceTypesTable.get(type);

            for (String source : sources) {
                try {
                    List<String> targets = new ArrayList<>(core.getKnowledgeBaseHandler().getKnowledgeBase().getHypernyms(source));
                    sourcesTargets.put(source, targets);


                } catch (Exception e) {
                    e.printStackTrace();
                    logger.severe(e.getMessage());
                    sourcesTargets.put(source, new ArrayList<>());
                }
            }
        }
       logger.info("(hypernyms)---> "+sourcesTargets);
        return sourcesTargets;
    }

        @Override
        public Map<String, List<String>> stem (List < String > terms)throws TException {

            Map<String, List<String>> termsStems = new HashMap<>();
            if (terms != null) {
                for (String term : terms) {
                    try {
                        List<String> stemmedTerms = new ArrayList(core.getKnowledgeBaseHandler().getKnowledgeBase().stem(term));

                        termsStems.put(term, stemmedTerms);
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.severe(e.getMessage());
                        termsStems.put(term, new ArrayList<>());
                    }
                }
            }
            System.out.println("(stem)---> "+termsStems);
            return termsStems;
        }
    }