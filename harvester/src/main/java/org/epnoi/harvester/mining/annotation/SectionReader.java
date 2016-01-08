package org.epnoi.harvester.mining.annotation;

import edu.upf.taln.dri.lib.model.ext.Section;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by cbadenes on 07/01/16.
 */
public class SectionReader {

    private static final Logger logger = LoggerFactory.getLogger(SectionReader.class);

    private static final Pattern text_pattern       = Pattern.compile("[a-zA-Z -]+");
    private static final Pattern level_pattern      = Pattern.compile("\\d[.0-9]*");
    private static final Pattern root_level_pattern = Pattern.compile("^\\d [a-zA-Z ]*");

    private static final List<String> NO_LEVEL_SECTIONS = Arrays.asList(new String[]{AnnotatedDocument.ABSTRACT_SECTION,"Acknowledgments","References"});

    private final List<Section> sections;

    private Map<String,Section> levels;

    public SectionReader(List<Section> sections){
        this.sections   = sections;
        this.levels     = new HashMap<>();

        sections.stream().
                filter(s -> isHierarchicalSection(s)).
                forEach(s -> levels.put(numberOf(s), s));
    }


    public boolean isIntroduction(Section section){
        return isValid(section, Arrays.asList(new String[]{"introduction", "related work", "previous work", "background"}));
    }

    public boolean isConclusion(Section section){
        return isValid(section, Arrays.asList(new String[]{"results","conclusion", "future work"}));
    }

    public boolean isContainedIn(Section section, String root){
        Matcher matcher = text_pattern.matcher(root);
        String token = "";
        if (matcher.find()) {
            token = matcher.group().toLowerCase();
        }
        return isValid(section, Arrays.asList(new String[]{token}));
    }

    public boolean isHierarchicalSection(Section section){
        return level_pattern.matcher(section.getName()).find();
    }

    public static Integer levelOf(Section section){
        return levelOf(section.getName());
    }

    public static Integer levelOf(String section){
        Matcher matcher = level_pattern.matcher(section);
        Integer number   =  (!matcher.find())? (NO_LEVEL_SECTIONS.contains(section)? 1: -1) : StringUtils.countMatches(matcher.group(),".") +1;
        return number;
    }

    public String numberOf(Section section){
        Matcher matcher = level_pattern.matcher(section.getName());
        return  (!matcher.find())? "" :matcher.group();
    }

    private boolean isValid(Section section, List<String> expressions ){

        if (section == null) return false;
        return (!match(section.getName(),expressions))? isValid(parentOf(section),expressions):true;
    }

    protected static boolean match(String sectionName, List<String> expressions){
        Matcher matcher = text_pattern.matcher(sectionName);
        if (matcher.find()){
            String token = matcher.group().toLowerCase();
            return expressions.stream().filter(e -> token.contains(e)).collect(Collectors.toList()).size() > 0;
        }
        return false;
    }


    private Section parentOf(Section section){

        Integer currentLevel = levelOf(section);
        if (currentLevel <= 1) return null; // root level
        return levels.getOrDefault(StringUtils.substringBeforeLast(numberOf(section), "."), null);
    }
}
