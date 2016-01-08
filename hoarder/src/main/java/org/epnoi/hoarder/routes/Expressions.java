package org.epnoi.hoarder.routes;

import org.apache.camel.Expression;
import org.apache.camel.builder.ValueBuilder;
import org.apache.camel.builder.xml.Namespaces;
import org.apache.camel.model.language.ConstantExpression;
import org.apache.camel.model.language.HeaderExpression;
import org.apache.camel.model.language.SimpleExpression;
import org.apache.camel.model.language.XPathExpression;

import java.util.Map;

/**
 * Created by cbadenes on 27/11/15.
 */
public class Expressions {

    public static XPathExpression xpath(String expression, Map<String,String> namespaces){
        XPathExpression xPathExpression = new XPathExpression(expression);
        xPathExpression.setNamespaces(namespaces);
        xPathExpression.setResultType(String.class);
        return xPathExpression;
    }

    public static XPathExpression xpath(String expression, Namespaces namespaces){
        XPathExpression xPathExpression = new XPathExpression(expression);
        xPathExpression.setNamespaces(namespaces.getNamespaces());
        xPathExpression.setResultType(String.class);
        return xPathExpression;
    }

    public static ConstantExpression constant(String expression){
        ConstantExpression constantExpression = new ConstantExpression(expression);
        return constantExpression;
    }

    public static SimpleExpression simple(String expression){
        SimpleExpression simpleExpression = new SimpleExpression(expression);
        return simpleExpression;
    }

    public static HeaderExpression header(String expression){
        HeaderExpression headerExpression = new HeaderExpression(expression);
        return headerExpression;
    }

    public static ValueBuilder value (Expression expression){
        ValueBuilder valueExpression = new ValueBuilder(expression);
        return valueExpression;
    }

}


