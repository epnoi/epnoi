package org.epnoi.model.clients.thrift;

import org.apache.commons.lang.SerializationUtils;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.epnoi.model.RelationHelper;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.rdf.RDFHelper;
import org.epnoi.model.services.thrift.KnowledgeBaseService;
import org.epnoi.model.services.thrift.Resource;
import org.epnoi.model.services.thrift.Services;
import org.epnoi.model.services.thrift.UIAService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by rgonza on 5/12/15.
 */
public class KnowledgeBaseServiceClient extends ThriftClient{

    @Override
    protected TMultiplexedProtocol _initMultiplexerMultiplexedProtocol(TProtocol protocol) {
        return new TMultiplexedProtocol(protocol, Services.KNOWLEDGEBASE.name());
    }

    public Map<String, List<String>> getRelated(List<String> sources, String type) {
        try {
            KnowledgeBaseService.Client client = new KnowledgeBaseService.Client(this.multiplexedProtocol);

            //  Resource content = client.getResource("http://en.wikipedia.org/wiki/Ammon", RDFHelper.WIKIPEDIA_PAGE_CLASS);
            Map<String, List<String>> targets = client.getRelated(sources, type);
            return targets;
        } catch (TException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, List<String>> stem(List<String> terms) {
        try {
            KnowledgeBaseService.Client client = new KnowledgeBaseService.Client(this.multiplexedProtocol);
            Map<String, List<String>> targets = client.stem(terms);
            return targets;
        } catch (TException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println("-->");

        KnowledgeBaseServiceClient uiaService = new KnowledgeBaseServiceClient();

        try {
            uiaService.init("localhost", 8585);
            System.out.println("It has been properly initialized!");
            System.out.println("Related--------------------------------------");
            List<String> sources = Arrays.asList("cat", "houses", "dogs");
            System.out.println("This are the related " + uiaService.getRelated(sources, RelationHelper.HYPERNYM));
            System.out.println("Stem--------------------------------------");
            System.out.println("These are  the stemmed " + uiaService.getRelated(sources, RelationHelper.HYPERNYM));
        } catch (Exception e) {
            e.printStackTrace();

        }


        System.out.println("<--");
    }
}