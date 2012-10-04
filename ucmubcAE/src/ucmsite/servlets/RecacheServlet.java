package ucmsite.servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ucmsite.pagegeneration.GoogleAccessor;
import ucmsite.pagegeneration.PageCacher;
import ucmsite.pagegeneration.PageGenerator;
import ucmsite.util.AppEngineLogger;

public class RecacheServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		try {
			//set up google docs if necessary
			if (GoogleAccessor.getGoogleAccessor() == null) {
				String password = this.getServletContext().getInitParameter("googlepassword");
				String username = this.getServletContext().getInitParameter("googleusername");
				GoogleAccessor.getGoogleAccessor(password, username);
			}
			
			//need to create the page generator
			if (PageGenerator.getPageGenerator() == null) {
				PageGenerator.getPageGenerator(req);
			}
			
			GoogleAccessor.getGoogleAccessor().resetPhotoMaps();
			
			String mode = req.getParameter("mode");
			if (mode != null) {
				if (mode.trim().equals("force")) {
					PageCacher.getPageCacher(this).recacheFolders(true);
				}
			}
			else {
				PageCacher.getPageCacher(this).recacheFolders(false);
			}
			
			resp.getWriter().print("Site Update Complete :)");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp) {
		doGet(req, resp);
	}
	
}
