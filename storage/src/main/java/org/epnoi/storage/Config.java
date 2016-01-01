package org.epnoi.storage;

import org.epnoi.storage.column.ColumnConfig;
import org.epnoi.storage.document.DocumentConfig;
import org.epnoi.storage.graph.GraphConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by cbadenes on 21/12/15.
 */
@Configuration
@ComponentScan("org.epnoi.storage")
//@Import({ ColumnConfig.class, DocumentConfig.class, GraphConfig.class})
public class Config {

}
