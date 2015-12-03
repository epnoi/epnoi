package org.epnoi.api.thrift;

import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.epnoi.api.thrift.services.ThriftServiceHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.logging.Logger;

@Component
public class ThriftServer {
    private static final Logger logger = Logger.getLogger(ThriftServer.class
            .getName());

    @Autowired
    List<ThriftServiceHandler> serviceHandlers;

    @Value("${epnoi.api.thrift.port}")
    int port;

    @Value("${epnoi.api.thrift.port}")
    int workers;

    @Value("${epnoi.api.thrift.port}")
    int selectors;

    TServer server;

    Thread serverThread;


    @PostConstruct
    public void start(){

        logger.info("Starting the thrift server " + port + " with the following service handlers " + serviceHandlers);
        try {

            TNonblockingServerSocket serverTransport = new TNonblockingServerSocket(8585);

            TMultiplexedProcessor proc = new TMultiplexedProcessor();
           /*REGISTER
            proc.registerProcessor(ParametersServiceHandler.service,
                    new ParametersService.Processor<>(parametersServiceHandler));

            proc.registerProcessor(HelloWorldServiceHandler.service,

                    new HelloWorldService.Processor<>(new HelloWorldServiceHandler()));
*/
            this.server = new TThreadedSelectorServer(
                    new TThreadedSelectorServer.Args(serverTransport).processor(proc)
                            .protocolFactory(new TBinaryProtocol.Factory())
                            .workerThreads(workers)
                            .selectorThreads(selectors));

            this.serverThread =
                    new Thread(new RunnableServer(server), "serverThread");
            serverThread.start();

            System.out.println("Starting the simple server...");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void stop() {
        logger.info("Stopping the thrift server");
        this.server.stop();
        try {
            this.serverThread.join();
        } catch (InterruptedException e) {
            logger.severe("Something went wrong stopping the thrift server");
            e.printStackTrace();
        }
    }

    class RunnableServer implements Runnable {
        private TServer server;

        public RunnableServer(TServer server) {
            this.server = server;
        }

        @Override
        public void run() {
            server.serve();
        }


    }
}
