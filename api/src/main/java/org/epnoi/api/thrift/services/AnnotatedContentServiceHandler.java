package org.epnoi.api.thrift.services;

import gate.corpora.DocumentImpl;
import org.apache.commons.lang.SerializationUtils;
import org.apache.thrift.TException;
import org.epnoi.model.Content;
import org.epnoi.model.Selector;
import org.epnoi.model.modules.Core;
import org.epnoi.model.services.thrift.AnnotatedContentService;
import org.epnoi.model.services.thrift.AnnotatedDocument;
import org.epnoi.model.services.thrift.Services;
import org.epnoi.uia.informationstore.SelectorHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * Created by rgonzalez on 3/12/15.
 */
@Component
public class AnnotatedContentServiceHandler extends ThriftServiceHandler implements AnnotatedContentService.Iface {

    @Autowired
    Core core;


    public AnnotatedContentServiceHandler() {

    }


    @Override
    public String getService() {
        return Services.ANNOTATEDCONTENT.name();
    }


    @Override
    public AnnotatedDocument getAnnotatedContent(String uri, String type) throws TException {

        AnnotatedDocument annotatedDocument = new AnnotatedDocument();
        try {
            Selector selector = new Selector();
            selector.setProperty(SelectorHelper.ANNOTATED_CONTENT_URI, uri);
            selector.setProperty(SelectorHelper.TYPE, type);
            Content<Object> content = this.core.getInformationHandler().getAnnotatedContent(selector);

            if (content != null) {

                content.getContent();
                byte[] serializedDocument = null;

                try {
                    serializedDocument = SerializationUtils.serialize((Serializable) content.getContent());
                } catch (Exception e) {
                    e.printStackTrace();

                }

                annotatedDocument.setDoc(ByteBuffer.wrap(serializedDocument));
                annotatedDocument.setContentType(content.getType());
            } else {
                System.out.println("uri "+uri+" was null ");
                annotatedDocument.setContentType(type);
                byte[] serializedDocument = SerializationUtils.serialize(new DocumentImpl());
                annotatedDocument.setDoc(serializedDocument);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return annotatedDocument;
    }
}
