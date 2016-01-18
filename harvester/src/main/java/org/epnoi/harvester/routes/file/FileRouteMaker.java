package org.epnoi.harvester.routes.file;

import org.apache.camel.builder.SimpleBuilder;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.language.ConstantExpression;
import org.apache.camel.model.language.SimpleExpression;
import org.apache.commons.lang.StringUtils;
import org.epnoi.harvester.routes.RouteMaker;
import org.epnoi.harvester.routes.common.CommonRouteBuilder;
import org.epnoi.harvester.routes.oaipmh.OAIPMHRouteBuilder;
import org.epnoi.model.Record;
import org.epnoi.storage.model.Source;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by cbadenes on 01/12/15.
 */
@Component
public class FileRouteMaker implements RouteMaker{

    private static final Logger LOG = LoggerFactory.getLogger(FileRouteMaker.class);

    @Value("${epnoi.harvester.folder.input}")
    protected String inputFolder;

    @Override
    public boolean accept(String protocol) {
        return protocol.equalsIgnoreCase("file");
    }

    @Override
    public RouteDefinition build(Source source) {

        Path folder = Paths.get(inputFolder, StringUtils.substringAfter(source.getUrl(),"//"));

        String uri = new StringBuilder().
                append("file:").
                append(folder.toFile().getAbsolutePath()).
                append("?recursive=true&noop=true&delete=false&idempotent=true&idempotentKey=${file:name}-${file:size}").
                toString();

        LOG.debug("URI created for harvesting purposes: " + uri);

        return new RouteDefinition().
                from(uri).
                setProperty(Record.DOMAIN_URI,                 new ConstantExpression(source.getDomain())).
                setProperty(Record.SOURCE_URL,                 new ConstantExpression(source.getUrl())).
                setProperty(Record.SOURCE_URI,                 new ConstantExpression(source.getUri())).
                setProperty(Record.SOURCE_PROTOCOL,            new ConstantExpression(source.getProtocol())).
                setProperty(Record.SOURCE_NAME,                new ConstantExpression(source.getName())).
                setProperty(Record.PUBLICATION_FORMAT,         new ConstantExpression("pdf")). //TODO get from file extension
                setProperty(Record.PUBLICATION_URL_LOCAL,      new SimpleExpression("${header.CamelFileAbsolutePath}")).
                setProperty(Record.PUBLICATION_REFERENCE_URL,  new SimpleExpression("${header.CamelFileAbsolutePath}")).
                to(CommonRouteBuilder.URI_RO_BUILD);
    }
}
