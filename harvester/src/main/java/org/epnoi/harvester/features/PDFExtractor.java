package org.epnoi.harvester.features;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import java.io.IOException;

/**
 * Created by cbadenes on 19/06/15.
 */
public class PDFExtractor {


    public static String from(String path) throws IOException {
        PDDocument doc = PDDocument.load(path);
        String content = new PDFTextStripper("utf-8").getText(doc);
        doc.close();
        return content;
    }

}
