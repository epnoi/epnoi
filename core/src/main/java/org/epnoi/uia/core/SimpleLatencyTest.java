package org.epnoi.uia.core;

import org.epnoi.model.Context;
import org.epnoi.model.Paper;
import org.epnoi.model.modules.Core;
import org.epnoi.model.rdf.RDFHelper;
import org.epnoi.uia.informationstore.dao.cassandra.PaperCassandraDAO;

import java.util.Arrays;

public class SimpleLatencyTest {
	public static final String paperURI = "http://papertest";

	public static void main(String[] args) {

		Core core = CoreUtility.getUIACore();
		PaperCassandraDAO dao = new PaperCassandraDAO();
		dao.init();
		Paper paper = new Paper();
		paper.setUri("http://papertest");
		paper.setAuthors(Arrays.asList("A", "B", "D", "E"));
		paper.setDescription("BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA BLA");
		paper.setPubDate("10-10-2014");
		paper.setTitle("Whatever!");

		long putEstimatedTime = 0;
		long containsEstimatedTime = 0;

		for (int i = 0; i < 50; i++) {
			long startTime = System.currentTimeMillis();
			core.getInformationHandler().put(paper, Context.getEmptyContext());
			putEstimatedTime += System.currentTimeMillis() - startTime;
			
			startTime = System.currentTimeMillis();
			core.getInformationHandler().contains(paperURI,
					RDFHelper.PAPER_CLASS);

			containsEstimatedTime += System.currentTimeMillis() - startTime;
		}
		
		System.out.println("Time spend in the put " + ((float)putEstimatedTime)/50);
		System.out.println("Time spend in the contains " + ((float)containsEstimatedTime)/50);
		
	}
}
