package ucmsite.servlets;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ucmsite.pagegeneration.PageCacher;

public class PageServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		try {
			//get the requested page
			String pageURL = req.getRequestURI();
			
			String page = null;

			resp.setContentType("text/html");
			page = PageCacher.getPageCacher(this).getCachedHTML(pageURL);
			
			if (page == null || page.trim().isEmpty()) {
				RecacheServlet emergencyRecache = new RecacheServlet();
				emergencyRecache.doGet(req, resp);
				page = PageCacher.getPageCacher().getCachedHTML(pageURL);
			}
			
			
			if (page != null) {
				resp.getWriter().println(page);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) {
		doGet(req, resp);
	}
	
}
