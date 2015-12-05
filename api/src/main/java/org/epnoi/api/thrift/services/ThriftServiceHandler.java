package org.epnoi.api.thrift.services;

/**
 * Created by rgonzalez on 3/12/15.
 */
public abstract class ThriftServiceHandler {
    public  abstract String getService();

    @Override
    public  String toString() {
        return this.getService();
    }
}
