package org.epnoi.uia.harvester.wikipedia.parse.de.tudarmstadt.ukp.wikipedia.api.sweble;

/**
 * Derived from the TextConverter class which was published in the
 * Sweble example project provided on
 * http://http://sweble.org by the Open Source Research Group,
 * University of Erlangen-N����rnberg under the Apache License, Version 2.0
 * (http://www.apache.org/licenses/LICENSE-2.0)
 */

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.sweble.wikitext.engine.utils.SimpleWikiConfiguration;
import org.sweble.wikitext.lazy.preprocessor.Template;

import de.fau.cs.osr.ptk.common.AstVisitor;
import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.Text;
import org.epnoi.uia.harvester.wikipedia.parse.de.tudarmstadt.ukp.wikipedia.api.WikiConstants;

/**
 * A visitor that extracts template names (no parameters) from an article AST.
 *
 * @author Oliver Ferschke
 */
public class TemplateNameExtractor extends AstVisitor
{
	private final SimpleWikiConfiguration config;


	private List<String> templates;

	// =========================================================================


	/**
	 * Creates a new visitor that extracts anchors of internal links from a
	 * parsed Wikipedia article using the default Sweble config as defined
	 * in WikiConstants.SWEBLE_CONFIG.
	 */
	public TemplateNameExtractor()
	{
		SimpleWikiConfiguration config=null;
		try{
			config = new SimpleWikiConfiguration(WikiConstants.SWEBLE_CONFIG);
		}catch(IOException e){
			//TODO logger
			e.printStackTrace();
		}catch(JAXBException e){
			//TODO logger
			e.printStackTrace();
		}
		this.config=config;
	}

	/**
	 * Creates a new visitor that extracts anchors of internal links from a
	 * parsed Wikipedia article.
	 *
	 * @param config the Sweble configuration
	 */
	public TemplateNameExtractor(SimpleWikiConfiguration config)
	{
		this.config = config;
	}

	@Override
	protected boolean before(AstNode node)
	{
		// This method is called by go() before visitation starts
		templates = new LinkedList<String>();
		return super.before(node);
	}

	@Override
	protected Object after(AstNode node, Object result)
	{
		return templates;
	}

	// =========================================================================

	public void visit(AstNode n)
	{
		iterate(n);
	}

	public void visit(Template tmpl) throws IOException
	{
		for(AstNode n:tmpl.getName()){
			if(n instanceof Text){
				add(((Text)n).getContent());
			}
		}
	}

	private void add(String s)
	{
		s=s.replace("\n", "").replace("\r", "");
		if (s.trim().isEmpty()) {
			return;
		}
		templates.add(s);
	}

}
