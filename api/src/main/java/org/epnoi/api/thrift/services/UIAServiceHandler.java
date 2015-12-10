package org.epnoi.api.thrift.services;

import org.apache.commons.lang.SerializationUtils;
import org.apache.thrift.TException;
import org.epnoi.model.modules.Core;
import org.epnoi.model.services.thrift.Resource;
import org.epnoi.model.services.thrift.Services;
import org.epnoi.model.services.thrift.UIAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;

/**
 * Created by rgonzalez on 3/12/15.
 */
@Component
public class UIAServiceHandler extends ThriftServiceHandler implements UIAService.Iface {

    @Autowired
    Core core;

    @Override
    public String getService() {
        return Services.UIA.name();
    }

    public UIAServiceHandler() {

    }

    @Override
    public Resource getResource(String uri, String type) throws TException {
       // System.out.println("________________________> " + uri + "|> " + type);
        Resource resource = new Resource();
        try {

            org.epnoi.model.Resource content = this.core.getInformationHandler().get(uri, type);

            if (content != null) {

                byte[] serializedDocument = null;
                try {
                    serializedDocument = SerializationUtils.serialize(content);
                } catch (Exception e) {
                    e.printStackTrace();

                }

                resource.setResource(ByteBuffer.wrap(serializedDocument));
                resource.setType(type);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return resource;
    }
}
