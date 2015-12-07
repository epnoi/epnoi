package org.epnoi.model.clients.thrift;

import org.apache.commons.lang.SerializationUtils;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.rdf.RDFHelper;
import org.epnoi.model.services.thrift.Resource;
import org.epnoi.model.services.thrift.Services;
import org.epnoi.model.services.thrift.UIAService;

/**
 * Created by rgonza on 5/12/15.
 */
public class UIAServiceClient {
    TTransport transport;
    TProtocol protocol;
    TMultiplexedProtocol multiplexedProtocol;


    private void _init(String host, int port) throws TException {
        transport = new TFramedTransport(new TSocket(host, port));
        this.protocol = new TBinaryProtocol(transport);

        this.multiplexedProtocol = new TMultiplexedProtocol(protocol, Services.UIA.name());
        transport.open();
    }

    public void init(String host, Integer port) throws EpnoiInitializationException {
        try {
            _init(host, port);
        } catch (Exception e) {

            throw new EpnoiInitializationException("There was a problem while initializing the thrift client: " + e.getMessage(), e);
        }
    }


    public org.epnoi.model.Resource getResource(String uri, String type) {
        try {


            UIAService.Client client = new UIAService.Client(this.multiplexedProtocol);

            //  Resource content = client.getResource("http://en.wikipedia.org/wiki/Ammon", RDFHelper.WIKIPEDIA_PAGE_CLASS);
            Resource content = client.getResource(uri, type);
            return ((org.epnoi.model.Resource) SerializationUtils.deserialize(content.getResource()));
        } catch (TException e) {
            e.printStackTrace();
        } finally {
            transport.close();
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println("-->");

        UIAServiceClient uiaService = new UIAServiceClient();
        org.epnoi.model.Resource resource = null;
        try {
            uiaService.init("localhost", 8585);
            System.out.println("It has been properly initialized!");
            resource = uiaService.getResource("http://en.wikipedia.org/wiki/Ammon", RDFHelper.WIKIPEDIA_PAGE_CLASS);
        } catch (Exception e) {
            e.printStackTrace();

        }


        System.out.println("(RESOURCE)====> " + resource);
        System.out.println("<--");
    }
}