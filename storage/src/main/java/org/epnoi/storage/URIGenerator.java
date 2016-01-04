package org.epnoi.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created by cbadenes on 04/01/16.
 */
@Component
public class URIGenerator {

    private static final String BASE = "http://epnoi.org/";

    @Autowired
    UDM udm;

    public String newSource(){
        String uri;
        do {
            uri = new StringBuilder(BASE).append("sources/").append(UUID.randomUUID().toString()).toString();
        } while (udm.existSource(uri));
        return uri;
    }

    public String newDomain(){
        String uri;
        do {
            uri = new StringBuilder(BASE).append("domains/").append(UUID.randomUUID().toString()).toString();
        } while (udm.existDomain(uri));
        return uri;
    }


    public String newDocument(){
        String uri;
        do {
            uri = new StringBuilder(BASE).append("documents/").append(UUID.randomUUID().toString()).toString();
        } while (udm.existDocument(uri));
        return uri;
    }

    public String newItem(){
        String uri;
        do {
            uri = new StringBuilder(BASE).append("items/").append(UUID.randomUUID().toString()).toString();
        } while (udm.existItem(uri));
        return uri;
    }

    public String newPart(){
        String uri;
        do {
            uri = new StringBuilder(BASE).append("parts/").append(UUID.randomUUID().toString()).toString();
        } while (udm.existPart(uri));
        return uri;
    }

    public String newWord(){
        String uri;
        do {
            uri = new StringBuilder(BASE).append("words/").append(UUID.randomUUID().toString()).toString();
        } while (udm.existWord(uri));
        return uri;
    }

    public String newAnalysis(){
        String uri;
        do {
            uri = new StringBuilder(BASE).append("analyses/").append(UUID.randomUUID().toString()).toString();
        } while (udm.existAnalysis(uri));
        return uri;
    }

    public String newRelation(){
        String uri;
        do {
            uri = new StringBuilder(BASE).append("relations/").append(UUID.randomUUID().toString()).toString();
        } while (udm.existRelation(uri));
        return uri;
    }

    public String newTopic(){
        String uri;
        do {
            uri = new StringBuilder(BASE).append("topics/").append(UUID.randomUUID().toString()).toString();
        } while (udm.existTopic(uri));
        return uri;
    }

}
