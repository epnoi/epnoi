package org.epnoi.harvester.mining;

import edu.upf.taln.dri.lib.exception.DRIexception;
import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.harvester.WebContextConfiguration;
import org.epnoi.harvester.mining.annotation.AnnotatedDocument;
import org.epnoi.harvester.mining.parser.Token;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by cbadenes on 07/01/16.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = WebContextConfiguration.class)
@TestPropertySource(properties = { "epnoi.upf.miner.config = harvester/src/test/resources/DRIconfig.properties"})
public class TextMinerTest {

    @Autowired
    TextMiner textMiner;

    AnnotatedDocument document;


    @Test
    public void annotatePDF() throws DRIexception {

        Path path = Paths.get("harvester/src/test/resources/pdf/sample-short.pdf");
        document = textMiner.annotate(path.toAbsolutePath().toString());

        System.out.println("Title: " + document.getTitle());

        System.out.println("Year: " + document.getYear());

//        System.out.println("Raw text: " + document.getContent());

        System.out.println("Abstract: " + document.getAbstractContent());

        System.out.println("Approach: " + document.getApproachContent());

        System.out.println("Background: " + document.getBackgroundContent());

        System.out.println("Challenge: " + document.getChallengeContent());

        System.out.println("FutureWork: " + document.getFutureWorkContent());

        System.out.println("Outcome: " + document.getOutcomeContent());

        System.out.println("test executed!");
    }

    @Test
    public void parsePDF() throws DRIexception {

        String text = "Mice are living in my houses.";

        List<Token> tokens = textMiner.parse(text);

        System.out.println(tokens);

    }

}
