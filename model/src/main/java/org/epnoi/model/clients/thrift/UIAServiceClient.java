package org.epnoi.model.clients.thrift;

import org.apache.commons.lang.SerializationUtils;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.epnoi.model.rdf.RDFHelper;
import org.epnoi.model.services.thrift.Resource;
import org.epnoi.model.services.thrift.Services;
import org.epnoi.model.services.thrift.UIAService;

/**
 * Created by rgonza on 5/12/15.
 */
public class UIAServiceClient extends ThriftClient {

    @Override
    protected TMultiplexedProtocol _initMultiplexerMultiplexedProtocol(TProtocol protocol){
        return new TMultiplexedProtocol(protocol, Services.UIA.name());
    }


    public org.epnoi.model.Resource getResource(String uri, String type) {
        try {


            UIAService.Client client = new UIAService.Client(this.multiplexedProtocol);

            //  Resource content = client.getResource("http://en.wikipedia.org/wiki/Ammon", RDFHelper.WIKIPEDIA_PAGE_CLASS);
            Resource content = client.getResource(uri, type);
            return ((org.epnoi.model.Resource) SerializationUtils.deserialize(content.getResource()));
        } catch (TException e) {
            e.printStackTrace();
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

        } finally {
            uiaService.close();
        }


        System.out.println("(RESOURCE)====> " + resource);
        System.out.println("<--");
    }
}