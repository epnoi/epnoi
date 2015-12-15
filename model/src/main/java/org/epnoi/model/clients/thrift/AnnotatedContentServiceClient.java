package org.epnoi.model.clients.thrift;


import gate.Document;
import org.apache.commons.lang.SerializationUtils;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.epnoi.model.Content;
import org.epnoi.model.rdf.RDFHelper;
import org.epnoi.model.services.thrift.AnnotatedContentService;
import org.epnoi.model.services.thrift.AnnotatedDocument;
import org.epnoi.model.services.thrift.Services;

/**
 * Created by rgonza on 5/12/15.
 */
public class AnnotatedContentServiceClient extends ThriftClient{

    @Override
    protected TMultiplexedProtocol _initMultiplexerMultiplexedProtocol(TProtocol protocol) {
       return new TMultiplexedProtocol(protocol, Services.ANNOTATEDCONTENT.name());
    }

    public org.epnoi.model.Content<Object> getAnnotatedDocument(String uri, String type) {
        try {

            AnnotatedContentService.Client client = new AnnotatedContentService.Client(this.multiplexedProtocol);

            AnnotatedDocument content = client.getAnnotatedContent(uri, type);


            Document doc = (Document) SerializationUtils.deserialize(content.getDoc());
            Content<Object> annotatedDocument = new Content<>(doc, content.getContentType());
            return annotatedDocument;

        } catch (TException e) {
            e.printStackTrace();
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

        }finally {
            uiaService.close();
        }


        System.out.println("(RESOURCE)====> " + resource);
        System.out.println("<--");
    }
}

