package org.epnoi.modeler;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.storage.TimeGenerator;
import org.epnoi.storage.UDM;
import org.epnoi.storage.URIGenerator;
import org.epnoi.storage.model.Document;
import org.epnoi.storage.model.Domain;
import org.epnoi.storage.model.Source;
import org.epnoi.storage.model.Topic;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by cbadenes on 13/01/16.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
@TestPropertySource(properties = {"epnoi.modeler.delay = 2000"})
public class ModelingTest {

    private static final Logger LOG = LoggerFactory.getLogger(ModelingTest.class);

    @Autowired
    UDM udm;

    @Autowired
    URIGenerator uriGenerator;

    @Autowired
    TimeGenerator timeGenerator;

    @Test
    public void simulate() throws InterruptedException {

        udm.deleteAll();

        // Domain
        Domain domain = new Domain();
        domain.setUri(uriGenerator.newDomain());
        domain.setName("test-domain");
        udm.saveDomain(domain);

        // Source
        Source source = new Source();
        source.setUri(uriGenerator.newSource());
        source.setCreationTime(timeGenerator.getNowAsISO());
        udm.saveSource(source);


        // Document 1
        Document document1 = new Document();
        document1.setUri(uriGenerator.newDocument());
        document1.setCreationTime(timeGenerator.getNowAsISO());
        document1.setTitle("title-1");
        document1.setAuthoredOn("20160112T12:07");
        document1.setAuthoredBy("");
        document1.setTokens("After graduating nearly 3,000 pilots, it was disbanded in late 1944, when there was no further need to train Australian aircrews for service in Europe. The school was re-established in 1946 at Uranquinty, New South Wales, and transferred to Point Cook the following year. To cope with the demands of the Korean War and Malayan Emergency, it was re-formed as No. 1 Applied Flying Training School in 1952 and moved to Pearce, Western Australia, in 1958. Another school was meanwhile formed at Uranquinty, No. 1 Basic Flying Training School (No. 1 BFTS), which transferred to Point Cook in 1958. In 1969, No. 1 AFTS was re-formed as No. 2 Flying Training School and No. 1 BFTS was re-formed as No.");

        udm.saveDocument(document1,source.getUri());
        udm.relateDocumentToDomain(document1.getUri(),domain.getUri(),document1.getCreationTime());

        // Document 2
        Document document2 = new Document();
        document2.setUri(uriGenerator.newDocument());
        document2.setCreationTime(timeGenerator.getNowAsISO());
        document2.setTitle("title-2");
        document2.setAuthoredOn("20160112T12:07");
        document2.setAuthoredBy("");
        document2.setTokens("The arts is a vast subdivision of culture, composed of many creative endeavors and disciplines. It is a broader term than \"art\", which, as a description of a field, usually means only the visual arts. The arts encompass the visual arts, the literary arts and the performing arts – music, theatre, dance and film, among others. This list is by no means comprehensive, but only meant to introduce the concept of the arts. For all intents and purposes, the history of the arts begins with the history of art. The arts might have origins in early human evolutionary prehistory. Ancient Greek art saw the veneration of the animal form and the development of equivalent skills to show musculature, poise, beauty and anatomically correct proportions. Ancient Roman art depicted gods as idealized humans, shown with characteristic distinguishing features (e.g. Jupiter's thunderbolt). In Byzantine and Gothic art of the Middle Ages, the dominance of the church insisted on the expression of biblical and not material truths. Eastern art has generally worked in a style akin to Western medieval art, namely a concentration on surface patterning and local colour (meaning the plain colour of an object, such as basic red for a red robe, rather than the modulations of that colour brought about by light, shade and reflection). A characteristic of this style is that the local colour is often defined by an outline (a contemporary equivalent is the cartoon). This is evident in, for example, the art of India, Tibet and Japan. Religious Islamic art forbids iconography, and expresses religious ideas through geometry instead. The physical and rational certainties depicted by the 19th-century Enlightenment were shattered not only by new discoveries of relativity by Einstein and of unseen psychology by Freud, but also by unprecedented technological development. Paradoxically the expressions of new technologies were greatly influenced by the ancient tribal arts of Africa and Oceania, through the works of Paul Gauguin and the Post-Impressionists, Pablo Picasso and the Cubists, as well as the Futurists and others.");

        udm.saveDocument(document2,source.getUri());
        udm.relateDocumentToDomain(document2.getUri(),domain.getUri(),document2.getCreationTime());

        // Document 3
        Document document3 = new Document();
        document3.setUri(uriGenerator.newDocument());
        document3.setCreationTime(timeGenerator.getNowAsISO());
        document3.setTitle("title-3");
        document3.setAuthoredOn("20160112T12:07");
        document3.setAuthoredBy("");
        document3.setTokens("Mysticism is the pursuit of communion with, identity with, or conscious awareness of an ultimate reality, divinity, spiritual truth, or God through direct experience, intuition, instinct or insight. Mysticism usually centers on practices intended to nurture those experiences. Mysticism may be dualistic, maintaining a distinction between the self and the divine, or may be nondualistic. Such pursuit has long been an integral part of the religious life of humanity. Within established religion it has been explicitly expressed within monasticism, where rules governing the everyday life of monks and nuns provide a framework conducive to the cultivation of mystical states of consciousness.");

        udm.saveDocument(document3,source.getUri());
        udm.relateDocumentToDomain(document3.getUri(),domain.getUri(),document3.getCreationTime());


        LOG.info("Sleep");
        Thread.sleep(120000);
        LOG.info("wake up");

    }


    @Test
    public void saveTopic(){

        String domainURI    = "http://epnoi.org/domains/9d3cda8b-06ed-4ca8-bb10-9f1775a6077b";
        String analysisURI  = "http://epnoi.org/analyses/55269e40-e839-43b9-a535-43f94faec08d";

        Topic topic = new Topic();
        topic.setUri("http://epnoi.org/topics/2217f111-50c7-40da-8a0f-64a1a9db14af");
        topic.setContent("content");
        topic.setCreationTime("2016-01-14T09:37+0100");
        topic.setAnalysis(analysisURI);


        udm.saveTopic(topic,domainURI, analysisURI);



    }
}
