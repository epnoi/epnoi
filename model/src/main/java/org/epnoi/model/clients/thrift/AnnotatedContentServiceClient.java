package org.epnoi.model.clients.thrift;


import gate.Document;
import org.apache.commons.lang.SerializationUtils;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.epnoi.model.Content;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.rdf.RDFHelper;
import org.epnoi.model.services.thrift.AnnotatedContentService;
import org.epnoi.model.services.thrift.AnnotatedDocument;
import org.epnoi.model.services.thrift.Services;

/**
 * Created by rgonza on 5/12/15.
 */
public class AnnotatedContentServiceClient {
    TTransport transport;
    TProtocol protocol;
    TMultiplexedProtocol multiplexedProtocol;


    private void _init(String host, int port) throws TException {
        transport = new TFramedTransport(new TSocket(host, port));
        this.protocol = new TBinaryProtocol(transport);

        this.multiplexedProtocol = new TMultiplexedProtocol(protocol, Services.ANNOTATEDCONTENT.name());
        transport.open();
    }

    public void init(String host, Integer port) throws EpnoiInitializationException {
        try {
            _init(host, port);
        } catch (Exception e) {

            throw new EpnoiInitializationException("There was a problem while initializing the thrift client: " + e.getMessage(), e);
        }
    }


    public org.epnoi.model.Content<Object> getAnnotatedDocument(String uri, String type) {
        try {

            AnnotatedContentService.Client client = new AnnotatedContentService.Client(this.multiplexedProtocol);

            AnnotatedDocument content = client.getAnnotatedContent(uri, type);
            System.out.println("This is the result > " + content);
            Document doc = (Document) SerializationUtils.deserialize(content.getDoc());
            Content<Object> annotatedDocument = new Content<>(doc, content.getContentType());
            return annotatedDocument;

        } catch (TException e) {
            e.printStackTrace();
        } finally {
            transport.close();
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println("-->");

        AnnotatedContentServiceClient uiaService = new AnnotatedContentServiceClient();
        org.epnoi.model.Content<Object> resource = null;
        try {
            uiaService.init("localhost", 8585);

            resource = uiaService.getAnnotatedDocument("http://en.wikipedia.org/wiki/Ammon/first/object/gate", RDFHelper.WIKIPEDIA_PAGE_CLASS);
        } catch (Exception e) {
            e.printStackTrace();

        }


        System.out.println("(RESOURCE)====> " + resource);
        System.out.println("<--");
    }
}

