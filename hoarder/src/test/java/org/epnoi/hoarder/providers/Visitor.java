package org.epnoi.hoarder.providers;

import com.google.common.base.Splitter;
import es.cbadenes.lab.test.IntegrationTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;


@Category(IntegrationTest.class)
public class Visitor {

    static{
        System.setProperty("javax.xml.accessExternalDTD","all");
    }


    public static final String PUBLISHED_FIRST = "date_first";
    public static final String PUBLISHED_LAST = "date_last";
    public static final String PUBLISHED_TOTAL = "total";

    private ConcurrentHashMap<URI,VisitorDetail> reports;
    private Iterable<String> terms;
    private Iterable<String> elements;
    private List<String> servers;

    @Before
    public void setup(){
        this.reports = new ConcurrentHashMap<>();

        this.terms = Splitter.on(',')
                .trimResults()
                .omitEmptyStrings()
                .split("abstract , accessRights , accrualMethod , accrualPeriodicity , accrualPolicy , " +
                        "alternative , audience , available , bibliographicCitation , conformsTo , contributor , " +
                        "coverage , created , creator , date , dateAccepted , dateCopyrighted , dateSubmitted , " +
                        "description , educationLevel , extent , format , hasFormat , hasPart , hasVersion , identifier , " +
                        "instructionalMethod , isFormatOf , isPartOf , isReferencedBy , isReplacedBy , isRequiredBy , issued , " +
                        "isVersionOf , language , license , mediator , medium , modified , provenance , publisher , references , " +
                        "relation , replaces , requires , rights , rightsHolder , source , spatial , subject , tableOfContents , " +
                        "temporal , title , type , valid");
        this.elements = Splitter.on(',')
                .trimResults()
                .omitEmptyStrings()
                .split("contributor , coverage , creator , date , description , format , identifier , language , publisher , " +
                        "relation , rights , source , subject , title , type");


        this.servers = fromMemory();
    }

    private List<String> fromMemory(){
        return Arrays.asList("http://abacus.universidadeuropea.es/oai/request");
    }


    private List <String> fromFile(){
        List<String> tmpServers = new ArrayList<>();
        try {

            File fXmlFile = new File("src/main/resources/listOfProviders.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("baseURL");


            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    String server = eElement.getTextContent();
                    tmpServers.add(server);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tmpServers;
    }

    @Test
    public void viewServers() throws InterruptedException, IOException {

        ExecutorService pool = Executors.newFixedThreadPool(300);
        for(String server: servers){
            System.out.println("Server: "+ server);
            try{
                VisitorTask task = new VisitorTask(server,reports);
                pool.execute(task);
            }catch (Exception e){

            }

        }
        pool.awaitTermination(30,TimeUnit.SECONDS);
        generateReport();

    }

    private void generateReport() throws IOException {
        String provHeader = "provider";
        final List<String> headers = new LinkedList<>();
        headers.add(provHeader);
        Consumer<? super String> addToHeader = new Consumer<String>() {
            @Override
            public void accept(String s) {
                headers.add(s+"AS"+"text");
                headers.add(s+"AS"+"url");
                headers.add(s+"AS"+"date");
            }
        };
        elements.forEach(addToHeader);
        terms.forEach(addToHeader);
        headers.add(PUBLISHED_FIRST);
        headers.add(PUBLISHED_LAST);
        headers.add(PUBLISHED_TOTAL);

        File csv = new File("target/providers.csv");
        csv.createNewFile();
        csv.setWritable(true);

        FileWriter writer = new FileWriter(csv);
        StringBuilder headerDetail = new StringBuilder();
        for(String header: headers){
            headerDetail.append(header).append(",");
        }
        headerDetail.deleteCharAt(headerDetail.lastIndexOf(","));
        writer.write(headerDetail.toString());
        writer.write("\n");

        for (URI server: reports.keySet()){
            VisitorDetail detail = reports.get(server);
            for(String header: headers){
                String value = "";
                if (header.equals(provHeader)){
                    value = server.toString();
                }else{
                    value = detail.get(header);
                }
                writer.write(value);
                writer.write(",");
            }
            writer.write("\n");
        }
        writer.close();
    }

}
