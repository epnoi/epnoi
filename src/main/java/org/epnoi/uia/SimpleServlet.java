package org.epnoi.uia;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class SimpleServlet extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		out.println("SimpleServlet Executed");
		System.out.println(".............................................> "
				+ System.currentTimeMillis());
		out.flush();
		out.close();
	}
}