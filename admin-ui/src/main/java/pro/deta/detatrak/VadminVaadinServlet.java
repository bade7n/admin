package pro.deta.detatrak;

import java.io.IOException;
import java.net.URL;

import javax.persistence.EntityManager;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.server.VaadinServlet;

import pro.deta.detatrak.dao.EMDAO;
import pro.deta.detatrak.dao.listener.DCNDAO;
import pro.deta.detatrak.dao.listener.DCNNotificatorCallback;

@WebServlet(initParams={
		@WebInitParam(name="UI",value="pro.deta.detatrak.MyUI"),
		@WebInitParam(name="legacyPropertyToString",value="false"),
		@WebInitParam(name="widgetset",value="pro.deta.detatrak.MyAppWidgetSet"),
		@WebInitParam(name="productionMode",value="false"),
},urlPatterns="/*",loadOnStartup=1)
public class VadminVaadinServlet extends VaadinServlet {
	private final Logger log = LoggerFactory.getLogger(VadminVaadinServlet.class); 
	/**
	 * 
	 */
	private static final long serialVersionUID = -6206147454598986441L;
	private static DCNDAO dcnd = null;
	private EMDAO emd;
	private LazyHibernateEntityManagerProvider lhemp;

	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		try {
			String log4jFile = servletConfig.getServletContext().getInitParameter("log4jFileName");
			URL logConfig = servletConfig.getServletContext().getResource(log4jFile);
			System.out.println("Logger initializing with " + logConfig);
			LogInitializer.initialize(logConfig);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		emd = EMDAO.getInstance();
		try {
			dcnd = DCNDAO.getInstance(new DCNNotificatorCallback(emd),emd);
		} catch(Throwable e) {
			log.error("Error while startin Vadmin context ",e);
		}
		lhemp = LazyHibernateEntityManagerProvider.getInstance();
		super.init(servletConfig);
	}

	
	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if(!isStaticResourceRequest(request)) {
			EntityManager em = null;
			try {
				em = EMDAO.getInstance().createEntityManager();
				lhemp.setCurrentEntityManager(em);
				super.service(request, response);
			} finally {
				if(em != null)
					try {
						em.close();
					} catch (Exception e) {
						log.error("Error while closing entityManager", e);
					}
				lhemp.remove();
			}
		} else
			super.service(request, response);
	}

	@Override
	public void destroy() {
		super.destroy();
		if(dcnd != null)
			dcnd.close();
		emd.close();
		dcnd = null;
		emd = null;
	}

}
