package org.epnoi.model.clients.thrift;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.epnoi.model.exceptions.EpnoiInitializationException;

import java.net.SocketException;

/**
 * Created by rgonza on 13/12/15.
 */
public abstract class ThriftClient {
    TSocket socket;

    TTransport transport;
    TProtocol protocol;
    TMultiplexedProtocol multiplexedProtocol;


    public void init(String host, Integer port) throws EpnoiInitializationException {
        try {
            _init(host, port);
        } catch (Exception e) {

            throw new EpnoiInitializationException("There was a problem while initializing the thrift client: " + e.getMessage(), e);
        }
    }

    protected void _init(String host, int port) throws TException {
        this.socket = new TSocket(host, port);
        try {
            this.socket.getSocket().setKeepAlive(false);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        this.transport = new TFramedTransport(this.socket);
        this.protocol = new TBinaryProtocol(transport);

        //this.multiplexedProtocol = new TMultiplexedProtocol(protocol, Services.KNOWLEDGEBASE.name());
        this.multiplexedProtocol = _initMultiplexerMultiplexedProtocol(protocol);

        transport.open();
    }

    public void close() {

        this.transport.close();
        this.socket.close();
    }

    protected abstract TMultiplexedProtocol _initMultiplexerMultiplexedProtocol(TProtocol protocol);
}
