package org.epnoi.storage.column;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.config.java.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.convert.CassandraConverter;
import org.springframework.data.cassandra.convert.MappingCassandraConverter;
import org.springframework.data.cassandra.mapping.BasicCassandraMappingContext;
import org.springframework.data.cassandra.mapping.CassandraMappingContext;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by cbadenes on 21/12/15.
 */
@Configuration
@EnableCassandraRepositories(basePackages = {"org.epnoi.storage.column.repository"})
@EnableTransactionManagement
public class ColumnConfig extends AbstractCassandraConfiguration{

    @Autowired
    private Environment env;

    @Bean
    public CassandraClusterFactoryBean cluster(){
        CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();
        cluster.setContactPoints(env.getProperty("epnoi.cassandra.contactpoints"));
        cluster.setPort(Integer.parseInt(env.getProperty("epnoi.cassandra.port")));
        return cluster;
    }

    @Bean
    public CassandraMappingContext mappingContext() {
        return new BasicCassandraMappingContext();
    }

    @Bean
    public CassandraConverter converter() {
        return new MappingCassandraConverter(mappingContext());
    }

    @Override
    protected String getKeyspaceName() {
        return env.getProperty("epnoi.cassandra.keyspace");
    }


}
