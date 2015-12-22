package org.epnoi.storage.graph;

import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.server.Neo4jServer;
import org.springframework.data.neo4j.server.RemoteServer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by cbadenes on 21/12/15.
 */
@Configuration
@EnableNeo4jRepositories(basePackages = {"org.epnoi.storage.graph.repository"})
@EnableTransactionManagement
public class GraphConfig extends Neo4jConfiguration{

    @Autowired
    private Environment env;


    @Override
    @Bean
    public Neo4jServer neo4jServer() {
        // Credentials
        // return new RemoteServer("http://localhost:7474",username,password);
        return new RemoteServer("http://"+env.getProperty("epnoi.neo4j.contactpoints")+":"+env.getProperty("epnoi.neo4j.port"));
    }

    @Override
    @Bean
    public SessionFactory getSessionFactory() {
        // with domain entity base package(s)
        return new SessionFactory("org.epnoi.storage.graph.domain");
    }

    // needed for session in view in web-applications
//    @Bean
//    @Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
//    public Session getSession() throws Exception {
//        return super.getSession();
//    }
}
