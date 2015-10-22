package org.epnoi.app;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Created by cbadenes on 22/10/15.
 */
public class TomcatLauncher {

    private final static Logger LOGGER = Logger.getLogger(TomcatLauncher.class.getName());

    private static final String BASE_DIR    = ".";

    private final static String WEBAPP_DIR  = "modules";

    private static final int PORT           = 8080;


    public static void main(String[] args) throws ServletException, LifecycleException, IOException {

        Tomcat tomcat = new Tomcat();
        //TODO externalize config
        tomcat.setPort(PORT);
        tomcat.setBaseDir(new File(BASE_DIR).getAbsolutePath());

        Path dir = new File(WEBAPP_DIR).getAbsoluteFile().toPath();
        tomcat.getHost().setAppBase(dir.toString());
        tomcat.getHost().setAutoDeploy(true);
        tomcat.getHost().setDeployOnStartup(true);



        LOGGER.info("looking for web apps in " + dir);
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:*.war" );

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {

            for (Path file: stream) {
                if (!matcher.matches(file.getFileName())) continue;

                String fileName = file.getFileName().toString();
                String context = "/" + StringUtils.substringBefore(StringUtils.substringAfter(fileName, "epnoi-"), "-");
                LOGGER.info("adding " + file + " to context:" + context);
                tomcat.addWebapp(context,file.toString());
            }

            tomcat.start();

            LOGGER.info("Tomcat started on " + tomcat.getHost());

            tomcat.getServer().await();

        } catch (IOException | DirectoryIteratorException x) {
            LOGGER.severe("Tomcat could not be started.");
            System.err.println(x);
        }






    }
}
