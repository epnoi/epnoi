package org.epnoi.storage;

import org.epnoi.storage.column.ColumnConfig;
import org.epnoi.storage.document.DocumentConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by cbadenes on 21/12/15.
 */
@Configuration
@Import({ ColumnConfig.class, DocumentConfig.class })
public class Config {



}
