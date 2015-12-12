package org.epnoi.api.thrift;

import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.epnoi.api.thrift.services.AnnotatedContentServiceHandler;
import org.epnoi.api.thrift.services.KnowledgeBaseServiceHandler;
import org.epnoi.api.thrift.services.ThriftServiceHandler;
import org.epnoi.api.thrift.services.UIAServiceHandler;
import org.epnoi.model.services.thrift.AnnotatedContentService;
import org.epnoi.model.services.thrift.KnowledgeBaseService;
import org.epnoi.model.services.thrift.UIAService;
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

    @Autowired
    AnnotatedContentServiceHandler annotatedContentServiceHandler;

    @Autowired
    UIAServiceHandler uiaServiceHandler;

    @Autowired
    KnowledgeBaseServiceHandler knowledgeBaseServiceHandler;

    @PostConstruct
    public void start() {

        logger.info("Starting the thrift server " + port + " with the following service handlers " + serviceHandlers);
        try {

            TNonblockingServerSocket serverTransport = new TNonblockingServerSocket(port);

            TMultiplexedProcessor proc = new TMultiplexedProcessor();
            _initServiceHandlers(proc);

            this.server = new TThreadedSelectorServer(
                    new TThreadedSelectorServer.Args(serverTransport).processor(proc)
                            .protocolFactory(new TBinaryProtocol.Factory())
                            .workerThreads(workers)
                            .selectorThreads(selectors));

            this.serverThread =
                    new Thread(new RunnableServer(server), "thriftServerThread");
            serverThread.start();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void _initServiceHandlers(TMultiplexedProcessor proc) {
        proc.registerProcessor(annotatedContentServiceHandler.getService(), new AnnotatedContentService.Processor<>(annotatedContentServiceHandler));
        proc.registerProcessor(uiaServiceHandler.getService(), new UIAService.Processor<>(uiaServiceHandler));
        proc.registerProcessor(knowledgeBaseServiceHandler.getService(), new KnowledgeBaseService.Processor<>(knowledgeBaseServiceHandler));

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
