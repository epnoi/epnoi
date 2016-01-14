package org.epnoi.comparator.helper;

import lombok.Data;
import org.epnoi.storage.TimeGenerator;
import org.epnoi.storage.UDM;
import org.epnoi.storage.URIGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 13/01/16.
 */
@Data
@Component
public class ComparatorHelper {

    @Autowired
    UDM udm;

    @Autowired
    TimeGenerator timeGenerator;

    @Autowired
    URIGenerator uriGenerator;

    @Value("${epnoi.comparator.threshold}")
    Double threshold;

}
