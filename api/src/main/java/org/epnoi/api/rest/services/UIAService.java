package org.epnoi.api.rest.services;

import org.epnoi.model.modules.Core;
import org.epnoi.model.parameterization.ParametersModel;
import org.epnoi.uia.core.CoreUtility;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.util.logging.Logger;
@Deprecated
public abstract class UIAService {

    protected Logger logger = null;

    private String UIA_CORE_ATTRIBUTE = "UIA_CORE";

    protected ParametersModel parametersModel;
    @Context
    protected ServletContext context;
    @Context
    protected UriInfo uriInfo;

    protected Core core = null;

    // ----------------------------------------------------------------------------------------

    protected synchronized Core getUIACore() {

        this.core = (Core) this.context.getAttribute(UIA_CORE_ATTRIBUTE);
        if (this.core == null) {
            System.out.println("Loading the model!");
            long time = System.currentTimeMillis();
            String configFileURL = context.getRealPath("/WEB-INF/uia.xml");


            this.core = CoreUtility.getUIACore(configFileURL);

            this.context.setAttribute(UIA_CORE_ATTRIBUTE, core);
            long afterTime = System.currentTimeMillis();
            System.out.println("It took " + (Long) (afterTime - time) / 1000.0
                    + "to load the model");
        }
        return this.core;

    }

    // ----------------------------------------------------------------------------------------


}
