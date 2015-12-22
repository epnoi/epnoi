package  org.epnoi.harvester.filesystem;

import org.epnoi.model.Domain;
import org.epnoi.model.Paper;
import org.epnoi.model.ResearchObject;
import org.epnoi.model.modules.Core;
import org.epnoi.model.rdf.RDFHelper;
import org.epnoi.uia.core.CoreUtility;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DemoDataLoader {
    Core core;
    public static final String DOMAIN_URI="http://www.epnoi.org/CGTestCorpusDomain";
    private static final Logger logger = Logger.getLogger(DemoDataLoader.class
            .getName());

    FilesystemHarvester harvester = new FilesystemHarvester();

    // --------------------------------------------------------------------------------------------

    public void init(Core core) {
        this.core = core;
        FilesystemHarvesterParameters parameters = _generateHarvesterParameters();
/*
        try {
            harvester.init(core, parameters);
        } catch (EpnoiInitializationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
*/
    }

    private FilesystemHarvesterParameters _generateHarvesterParameters() {
        FilesystemHarvesterParameters parameters = new FilesystemHarvesterParameters();

        parameters.setParameter(FilesystemHarvesterParameters.CORPUS_LABEL,
                "CGTestCorpus");

        parameters.setParameter(FilesystemHarvesterParameters.CORPUS_URI,
                "http://CGTestCorpus");
        parameters.setParameter(FilesystemHarvesterParameters.VERBOSE, true);

        parameters.setParameter(FilesystemHarvesterParameters.OVERWRITE, false);

        parameters.setParameter(FilesystemHarvesterParameters.FILEPATH,
                "/opt/epnoi/epnoideployment/firstReviewResources/CGCorpus");
        return parameters;
    }

    // --------------------------------------------------------------------------------------------

    public void load() {
       List<Paper> papers= _loadComputerGraphicsCorpus();
        _createTheSimpleDomain(papers);
    }

    private void _createTheSimpleDomain(List<Paper> papers) {
        Domain domain = new Domain();
        domain.setUri(DOMAIN_URI);
        domain.setExpression("sparqlexpression");
        domain.setLabel("simple demo domain");
        domain.setType(RDFHelper.PAPER_CLASS);
        domain.setResources(DOMAIN_URI+"/resources");
      //  _eraseDomainsAndResearchObjects();
        ResearchObject resources = new ResearchObject();
        resources.setUri(DOMAIN_URI+"/resources");

        resources.setAggregatedResources(papers.stream().map(element -> element.getUri()).collect(Collectors.toList()));


        this.core.getInformationHandler().put(resources,
                org.epnoi.model.Context.getEmptyContext());

        this.core.getInformationHandler().put(domain,
                org.epnoi.model.Context.getEmptyContext());



        System.out.println("The retrieved uris of the domain are ");
        List<String> uris = core.getDomainsHandler().gather(domain);
        System.out.println(uris);
        System.out.println("There are "+uris.size());
    }

    private void _eraseDomainsAndResearchObjects() {
        List<String> domainURIs = this.core.getInformationHandler().getAll(
                RDFHelper.DOMAIN_CLASS);
        for (String domainURI : domainURIs) {
            this.core.getInformationHandler().remove(domainURI,
                    RDFHelper.DOMAIN_CLASS);
            this.core.getInformationHandler().remove(domainURI + "/resources",
                    RDFHelper.RESEARCH_OBJECT_CLASS);
        }
    }

    // --------------------------------------------------------------------------------------------

    private List<Paper> _loadComputerGraphicsCorpus() {
        logger.info("Loading the computer graphics corpus");
    /*
		List<String> paperURIs = this.core.getInformationHandler().getAll(
				RDFHelper.PAPER_CLASS);
		for (String paperURI : paperURIs) {
			this.core.getInformationHandler().remove(paperURI,
					RDFHelper.PAPER_CLASS);
			r
		}
		*/


        return this.harvester.run();
    }

    public static void main(String[] args) {
        Core core = CoreUtility.getUIACore();
        DemoDataLoader demoDataLoader = new DemoDataLoader();
        demoDataLoader.init(core);
        demoDataLoader.load();

    }
}
