/*******************************************************************************
 * Copyright (c) 2010 Torsten Zesch.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 * 
 * Contributors:
 *     Torsten Zesch - initial API and implementation
 ******************************************************************************/
package org.epnoi.uia.harvester.wikipedia.parse.de.tudarmstadt.ukp.wikipedia.api.exception;


public class WikiRelatednessException extends WikiException {

    static final long serialVersionUID = 1L;
    
    public WikiRelatednessException(){
        super();
    }

    public WikiRelatednessException(String txt) {
        super(txt);
    }

    public WikiRelatednessException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public WikiRelatednessException(Throwable cause) {
        super(cause);
    }
}
