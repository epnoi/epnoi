package org.epnoi.api.thrift.clients;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.epnoi.api.thrift.services.AnnotatedContentServiceHandler;
import org.epnoi.model.services.thrift.AnnotatedContentService;

/**
 * Created by rgonza on 5/12/15.
 */
public class AnnotatedContentServiceClient {
    public static void main(String[] args) {
        TTransport transport =
                new TFramedTransport(new TSocket("localhost", 8585));


        TProtocol protocol = new TBinaryProtocol(transport);


        TMultiplexedProtocol serviceProtocol =
                new TMultiplexedProtocol(protocol, AnnotatedContentServiceHandler.service);

        AnnotatedContentService.Client client = new AnnotatedContentService.Client(serviceProtocol);

    }
}
