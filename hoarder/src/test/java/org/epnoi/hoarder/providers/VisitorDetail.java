package org.epnoi.hoarder.providers;

import java.net.URI;
import java.util.Map;
import java.util.Set;

public class VisitorDetail {


    private Map<String,Set<String>> map;
    private URI url;

    public VisitorDetail(URI url){
        this.url = url;
    }

    public void add(Map map){
        this.map = map;
    }

    @Override
    public String toString() {
        Set<String> metadatas = map.keySet();
        StringBuilder reportPrint = new StringBuilder();
        reportPrint.append("[").append(url).append("]\n");
        for(String metadata: metadatas){
            reportPrint.append(metadata).append(":\t").append(map.get(metadata)).append("\n");
        }
        return reportPrint.toString();
    }

    public String get(String key){
        Set<String> values = this.map.get(key);
        if (values == null){
            if (key.equals(Visitor.PUBLISHED_FIRST)||(key.equals(Visitor.PUBLISHED_LAST))){
                return "";
            }else{
                return "0";
            }
        }else{
            if (key.equals(Visitor.PUBLISHED_FIRST)||(key.equals(Visitor.PUBLISHED_LAST))){
                return values.iterator().next();
            }else{
                return String.valueOf(values.size());
            }
        }

    }
}
