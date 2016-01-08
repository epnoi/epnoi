package org.epnoi.model;

import com.google.common.base.Joiner;

/**
 * Created by cbadenes on 01/12/15.
 */
public class Record {
    private static Joiner joiner                                = Joiner.on(".");
    private static final String EPNOI                           = "epnoi";

    // Time
    public static final String TIME                             = joiner.join(EPNOI,"time");

    // File
    private static final String ARGUMENT                        = joiner.join(EPNOI,"argument");
    public static final String ARGUMENT_NAME                    = joiner.join(ARGUMENT,"name");
    public static final String ARGUMENT_PATH                    = joiner.join(ARGUMENT,"path");

    // Domain
    private static final String DOMAIN                          = joiner.join(EPNOI,"domain");
    public static final String DOMAIN_URI                       = joiner.join(DOMAIN, "uri");

    // Source
    private static final String SOURCE                          = joiner.join(EPNOI,"source");
    public static final String SOURCE_NAME                      = joiner.join(SOURCE,"name");
    public static final String SOURCE_URI                       = joiner.join(SOURCE, "uri");
    public static final String SOURCE_URL                       = joiner.join(SOURCE, "url");
    public static final String SOURCE_PROTOCOL                  = joiner.join(SOURCE,"protocol");

    // Publication
    private static final String PUBLICATION                     = joiner.join(EPNOI,"publication");
    public static final String PUBLICATION_UUID                 = joiner.join(PUBLICATION,"uuid");
    public static final String PUBLICATION_TITLE                = joiner.join(PUBLICATION,"title");
    public static final String PUBLICATION_DESCRIPTION          = joiner.join(PUBLICATION,"description");
    public static final String PUBLICATION_PUBLISHED            = joiner.join(PUBLICATION,"published");
    public static final String PUBLICATION_PUBLISHED_DATE       = joiner.join(PUBLICATION_PUBLISHED,"date");
    public static final String PUBLICATION_PUBLISHED_MILLIS     = joiner.join(PUBLICATION_PUBLISHED,"millis");
    public static final String PUBLICATION_AUTHORED             = joiner.join(PUBLICATION,"authored");
    public static final String PUBLICATION_URI                  = joiner.join(PUBLICATION,"uri");
    public static final String PUBLICATION_LANGUAGE             = joiner.join(PUBLICATION,"lang");
    public static final String PUBLICATION_RIGHTS               = joiner.join(PUBLICATION,"rights");
    public static final String PUBLICATION_FORMAT               = joiner.join(PUBLICATION,"format");
    public static final String PUBLICATION_TYPE               = joiner.join(PUBLICATION,"type");
    public static final String PUBLICATION_SUBJECT               = joiner.join(PUBLICATION,"subject");

    //  -> urls
    public static final String PUBLICATION_URL                  = joiner.join(PUBLICATION,"url");
    public static final String PUBLICATION_URL_LOCAL            = joiner.join(PUBLICATION_URL,"local");

    //  -> reference
    private static final String PUBLICATION_REFERENCE           = joiner.join(PUBLICATION,"reference");
    public static final String PUBLICATION_METADATA_FORMAT      = joiner.join(PUBLICATION_REFERENCE,"format");
    public static final String PUBLICATION_REFERENCE_URL        = joiner.join(PUBLICATION_REFERENCE,"url");
    //  -> creators
    public static final String PUBLICATION_CREATORS             = joiner.join(PUBLICATION,"creators"); //CSV
    public static final String PUBLICATION_CONTRIBUTORS         = joiner.join(PUBLICATION,"contributors"); //CSV
}
