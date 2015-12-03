package org.epnoi.api.thrift.services;

import org.springframework.stereotype.Component;

/**
 * Created by rgonzalez on 3/12/15.
 */
@Component
public class UIAService extends ThriftServiceHandler {
    private static final String name = "UIAService";
    @Override
    public String getService() {
        return name;
    }

    public UIAService() {

    }
}
