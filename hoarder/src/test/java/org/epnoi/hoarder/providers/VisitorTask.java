package org.epnoi.hoarder.providers;


import es.upm.oeg.camel.oaipmh.component.OAIPMHHttpClient;
import es.upm.oeg.camel.oaipmh.dataformat.OAIPMHConverter;
import es.upm.oeg.camel.oaipmh.model.*;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.ISODateTimeFormat;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class VisitorTask implements Runnable {

    private final URI url;
    private final ConcurrentHashMap map;

    private HashMap<String,Set<String>> report;

    private Long total = 0L;

    public VisitorTask(String url, ConcurrentHashMap map){
        this.url = URI.create(url);
        this.map = map;
    }

    @Override
    public void run() {
        this.report = new HashMap<>();
        OAIPMHHttpClient httpClient = new OAIPMHHttpClient();
        try {
            request(httpClient,null);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        Set totalValue = new TreeSet<>();
        totalValue.add(total);
        this.report.put(Visitor.PUBLISHED_TOTAL, totalValue);
        VisitorDetail detail = new VisitorDetail(url);
        detail.add(report);
        this.map.put(url,detail);
    }


    private int request(OAIPMHHttpClient httpClient, ResumptionTokenType token) throws IOException, URISyntaxException, JAXBException {


        // request 'verb' to remote data provider
        String responseXML = httpClient.doRequest(this.url,"ListRecords",null,null,"oai_dc",token);

        // build a java object from xml
        OAIPMHtype responseObject = OAIPMHConverter.xmlToOaipmh(responseXML);

        // Check if error
        //TODO Retries Policy
        if (handleErrors(responseObject)){
            return 0;
        }

        // Split and send records to camel route (background mode)
        process(responseObject);

        // Check if incomplete list
        ResumptionTokenType newToken = responseObject.getListRecords().getResumptionToken();
        return ((newToken != null) && (newToken.getValue() != null) && !(newToken.getValue().startsWith(" ")))? request(httpClient, newToken) : 1;
    }

    private void process(OAIPMHtype response){

        List<RecordType> records = response.getListRecords().getRecord();
        URI resolver = URI.create("");
        for (RecordType record: records){
            // Record
            Map<String,List<String>> recordReport = new HashMap<>();

            MetadataType metadata = record.getMetadata();
            if (metadata != null){
                List<JAXBElement<ElementType>> dc = metadata.getDc().getTitleOrCreatorOrSubject();
                if (dc != null){
                    this.total += 1;
                    Iterator<JAXBElement<ElementType>> it = dc.iterator();
                    while(it.hasNext()){
                        JAXBElement<ElementType> element = it.next();

                        String name = element.getName().getLocalPart();
                        String value = element.getValue().getValue();
                        String type = "text";
                        if (value != null){

                            if (name.contains("date")){
                                try {

                                    DateTimeZone timezone = DateTimeZone.forID("Zulu");//UTC

                                    DateTime date;
                                    try{
                                        date = ISODateTimeFormat.dateTimeNoMillis().withZone(timezone).parseDateTime(value);
                                    }catch (Exception e){
                                        try{
                                            date = ISODateTimeFormat.dateOptionalTimeParser().parseDateTime(value);
                                        }catch (Exception e1){
                                            try{
                                                date = ISODateTimeFormat.basicDate().parseDateTime(value);
                                            }catch (Exception e2){
                                                date = ISODateTimeFormat.basicDateTime().parseDateTime(value);
                                            }
                                        }
                                    }

//                                    DateTime date = ISODateTimeFormat.dateParser().parseDateTime(value);

                                    // First Date
                                    List<String> first = recordReport.get(Visitor.PUBLISHED_FIRST);
                                    if (first == null){
                                        first = new ArrayList<>();
                                    }
                                    if (first.isEmpty()){
                                        first.add(date.toString());
                                    }else{
                                        String firstString = first.get(0);
                                        DateTime firstDate = ISODateTimeFormat.dateParser().parseDateTime(firstString);
                                        if (date.isBefore(firstDate)){
                                            first.remove(0);
                                            first.add(date.toString());
                                        }
                                    }
                                    recordReport.put(Visitor.PUBLISHED_FIRST,first);

                                    // Last Date
                                    List<String> last = recordReport.get(Visitor.PUBLISHED_LAST);
                                    if (last == null){
                                        last = new ArrayList<>();
                                    }
                                    if (last.isEmpty()){
                                        last.add(date.toString());
                                    }else{
                                        String lastString = last.get(0);
                                        DateTime lastDate = ISODateTimeFormat.dateParser().parseDateTime(lastString);
                                        if (date.isAfter(lastDate)){
                                            last.remove(0);
                                            last.add(date.toString());
                                        }
                                    }
                                    recordReport.put(Visitor.PUBLISHED_LAST,last);



                                    type = "date";

                                }catch (Exception e){

                                }
                            }

                            // is URL
                            try {
                                URI path = resolver.resolve(value);
                                if ((path.getHost() != null) && (!path.getHost().trim().equals(""))){
                                    type = "url";
                                }
                            }catch (Exception e){

                            }
                        }

                        String term = name+"AS"+type;

                        List<String> values = recordReport.get(term);

                        if (values == null){
                            values = new ArrayList<>();
                        }
                        values.add(type);
                        recordReport.put(term,values);
                    }
                }
            }
            // Add record report to global report private HashMap<String,Set<String>> report;

            Set<String> terms = recordReport.keySet();
            for(String term: terms){
                Set<String> types = report.get(term);
                if (types == null){
                    types = new TreeSet<>();
                }
                List<String> typesRecorded = recordReport.get(term);

                int recordSize = typesRecorded.size();
                int globalSize = types.size();

                if (recordSize>globalSize){
                    types = new TreeSet<>();
                    int index = 1;
                    for (String typeRecorded: typesRecorded){
                        types.add(""+typeRecorded+index);
                        index += 1;
                    }
                }
                report.put(term,types);
            }
        }
    }


    private boolean handleErrors(OAIPMHtype message){
        List<OAIPMHerrorType> errors = message.getError();
        if ((errors != null) && (!errors.isEmpty())){
            for (OAIPMHerrorType error: errors){

                switch(error.getCode()){
                    case NO_RECORDS_MATCH:
                    case NO_METADATA_FORMATS:
                    case NO_SET_HIERARCHY:
//                        LOG.info("{} / {}",error.getCode(),error.getValue());
                        System.out.println("Error: "+ error);
                        break;
                    default:
                        System.out.println("Default Error: "+ error);
//                        LOG.error("Error on [{}] getting records: {}-{}", endpoint.getUrl(),error.getCode(), error.getValue());
                }
            }
            return true;
        }
        return false;
    }

}
