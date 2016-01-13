package org.epnoi.modeler.builder;

import es.cbadenes.lab.test.IntegrationTest;
import es.upm.oeg.epnoi.matching.metrics.domain.entity.RegularResource;
import org.epnoi.modeler.Config;
import org.epnoi.modeler.model.W2VModel;
import org.epnoi.modeler.model.WordDistribution;
import org.epnoi.storage.model.User;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cbadenes on 13/01/16.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
public class WordEmbeddingBuilderTest {

    private static final Logger LOG = LoggerFactory.getLogger(WordEmbeddingBuilderTest.class);

    @Autowired
    WordEmbeddingBuilder wordEmbeddingBuilder;

    @Autowired
    RegularResourceBuilder regularResourceBuilder;

    @Test
    public void simulate(){

        String domainURI = "http://epnoi.org/domains/1223123";


        List<RegularResource> rrs = new ArrayList<>();
        rrs.add(regularResourceBuilder.from("doc1-uri","doc1-title", "doc1-date", new ArrayList<User>(), "After graduating nearly 3,000 pilots, it was disbanded in late 1944, when there was no further need to train Australian aircrews for service in Europe. The school was re-established in 1946 at Uranquinty, New South Wales, and transferred to Point Cook the following year. To cope with the demands of the Korean War and Malayan Emergency, it was re-formed as No. 1 Applied Flying Training School in 1952 and moved to Pearce, Western Australia, in 1958. Another school was meanwhile formed at Uranquinty, No. 1 Basic Flying Training School (No. 1 BFTS), which transferred to Point Cook in 1958. In 1969, No. 1 AFTS was re-formed as No. 2 Flying Training School and No. 1 BFTS was re-formed as No."));
        rrs.add(regularResourceBuilder.from("doc2-uri","doc2-title", "doc2-date", new ArrayList<User>(), "The arts is a vast subdivision of culture, composed of many creative endeavors and disciplines. It is a broader term than \"art\", which, as a description of a field, usually means only the visual arts. The arts encompass the visual arts, the literary arts and the performing arts – music, theatre, dance and film, among others. This list is by no means comprehensive, but only meant to introduce the concept of the arts. For all intents and purposes, the history of the arts begins with the history of art. The arts might have origins in early human evolutionary prehistory. Ancient Greek art saw the veneration of the animal form and the development of equivalent skills to show musculature, poise, beauty and anatomically correct proportions. Ancient Roman art depicted gods as idealized humans, shown with characteristic distinguishing features (e.g. Jupiter's thunderbolt). In Byzantine and Gothic art of the Middle Ages, the dominance of the church insisted on the expression of biblical and not material truths. Eastern art has generally worked in a style akin to Western medieval art, namely a concentration on surface patterning and local colour (meaning the plain colour of an object, such as basic red for a red robe, rather than the modulations of that colour brought about by light, shade and reflection). A characteristic of this style is that the local colour is often defined by an outline (a contemporary equivalent is the cartoon). This is evident in, for example, the art of India, Tibet and Japan. Religious Islamic art forbids iconography, and expresses religious ideas through geometry instead. The physical and rational certainties depicted by the 19th-century Enlightenment were shattered not only by new discoveries of relativity by Einstein and of unseen psychology by Freud, but also by unprecedented technological development. Paradoxically the expressions of new technologies were greatly influenced by the ancient tribal arts of Africa and Oceania, through the works of Paul Gauguin and the Post-Impressionists, Pablo Picasso and the Cubists, as well as the Futurists and others."));
        rrs.add(regularResourceBuilder.from("doc3-uri","doc3-title", "doc3-date", new ArrayList<User>(), "Mysticism is the pursuit of communion with, identity with, or conscious awareness of an ultimate reality, divinity, spiritual truth, or God through direct experience, intuition, instinct or insight. Mysticism usually centers on practices intended to nurture those experiences. Mysticism may be dualistic, maintaining a distinction between the self and the divine, or may be nondualistic. Such pursuit has long been an integral part of the religious life of humanity. Within established religion it has been explicitly expressed within monasticism, where rules governing the everyday life of monks and nuns provide a framework conducive to the cultivation of mystical states of consciousness."));


        W2VModel model = wordEmbeddingBuilder.build(domainURI, rrs);

        List<String> vocabulary = model.getVocabulary();

        vocabulary.stream().forEach(word -> LOG.info("Word: " + word));

    }
}
