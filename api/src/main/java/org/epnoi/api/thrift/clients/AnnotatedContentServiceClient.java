package org.epnoi.api.thrift.clients;

import gate.util.compilers.eclipse.jdt.internal.compiler.lookup.SourceTypeBinding;
import org.apache.commons.lang.SerializationUtils;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.epnoi.api.thrift.services.AnnotatedContentServiceHandler;
import org.epnoi.model.rdf.RDFHelper;
import org.epnoi.model.services.thrift.AnnotatedContentService;
import org.epnoi.model.services.thrift.AnnotatedDocument;

import java.io.Serializable;

/**
 * Created by rgonza on 5/12/15.
 */
public class AnnotatedContentServiceClient {
    public static void main(String[] args) {
        try {
        TTransport transport =
                new TFramedTransport(new TSocket("localhost", 8585));


        TProtocol protocol = new TBinaryProtocol(transport);


        TMultiplexedProtocol serviceProtocol =
                new TMultiplexedProtocol(protocol, AnnotatedContentServiceHandler.service);
        transport.open();
        AnnotatedContentService.Client client = new AnnotatedContentService.Client(serviceProtocol);

            AnnotatedDocument content = client.getAnnotatedContent("http://en.wikipedia.org/wiki/Ammon/first/object/gate", RDFHelper.WIKIPEDIA_PAGE_CLASS);
            System.out.println("This is the result > "+ content);
            System.out.println("-> " +SerializationUtils.deserialize(content.getDoc()));
        } catch (TException e) {
            e.printStackTrace();
        }

    }
}
