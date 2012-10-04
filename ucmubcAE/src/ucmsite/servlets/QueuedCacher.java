package ucmsite.servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ucmsite.pagegeneration.GoogleAccessor;
import ucmsite.pagegeneration.PageCacher;
import ucmsite.pagegeneration.PageGenerator;
import ucmsite.util.AppEngineLogger;

public class QueuedCacher extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		doPost(req, resp);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) {
		//set up google docs if necessary
		if (GoogleAccessor.getGoogleAccessor() == null) {
			String password = this.getServletContext().getInitParameter("googlepassword");
			String username = this.getServletContext().getInitParameter("googleusername");
			GoogleAccessor.getGoogleAccessor(password, username);
		}
		if (PageGenerator.getPageGenerator() == null) {
			PageGenerator.getPageGenerator(req);
		}

		PageCacher cacher = PageCacher.getPageCacher(this);
		
		String key = req.getParameter("componentkey");
		if (key != null) {
			cacher.recacheComponent(key, false);
		}
		
		key = req.getParameter("dockey");
		if (key != null) {
			cacher.recacheDoc(key, false);
		}
		
		key = req.getParameter("componentkeyForce");
		if (key != null) {
			cacher.recacheComponent(key, true);
		}
		
		key = req.getParameter("dockeyForce");
		if (key != null) {
			cacher.recacheDoc(key, true);
		}
	}
	
}
