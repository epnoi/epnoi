package org.epnoi.api.thrift.clients;

import org.apache.commons.lang.SerializationUtils;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.epnoi.api.thrift.services.AnnotatedContentServiceHandler;
import org.epnoi.api.thrift.services.UIAServiceHandler;
import org.epnoi.model.rdf.RDFHelper;
import org.epnoi.model.services.thrift.AnnotatedContentService;
import org.epnoi.model.services.thrift.AnnotatedDocument;
import org.epnoi.model.services.thrift.Resource;
import org.epnoi.model.services.thrift.UIAService;

/**
 * Created by rgonza on 5/12/15.
 */
public class UIAServiceClient {
    public static void main(String[] args) {
        try {
        TTransport transport =
                new TFramedTransport(new TSocket("localhost", 8585));


        TProtocol protocol = new TBinaryProtocol(transport);


        TMultiplexedProtocol serviceProtocol =
                new TMultiplexedProtocol(protocol, UIAServiceHandler.service);
        transport.open();
        UIAService.Client client = new UIAService.Client(serviceProtocol);

            Resource content = client.getResource("http://en.wikipedia.org/wiki/Ammon", RDFHelper.WIKIPEDIA_PAGE_CLASS);
            System.out.println("This is the result > "+ content);
            System.out.println("-> " + SerializationUtils.deserialize(content.getResource()));
        } catch (TException e) {
            e.printStackTrace();
        }

    }
}
