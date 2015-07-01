package pro.deta.detatrak;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pro.deta.detatrak.dao.EMDAO;
import pro.deta.detatrak.dao.listener.DCNDAO;
import pro.deta.detatrak.dao.listener.DCNNotificatorCallback;
import pro.deta.detatrak.dao.listener.DCNUpdateNotifier;

import com.vaadin.server.VaadinServlet;

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
		emd = EMDAO.getInstance();
		try {
			dcnd = new DCNDAO(new DCNNotificatorCallback(emd),emd);
			DCNUpdateNotifier.init(emd);
		} catch(Throwable e) {
			log.error("Error while startin Vadmin context ",e);
		}
		lhemp = LazyHibernateEntityManagerProvider.getInstance();
		super.init(servletConfig);
	}

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if(!isStaticResourceRequest(request))
			try {
				EntityManager em = EMDAO.getInstance().createEntityManager();
				lhemp.setCurrentEntityManager(em);
				super.service(request, response);
			} finally {
				lhemp.remove();
			}
		else
			super.service(request, response);
	}

	@Override
	public void destroy() {
		super.destroy();
		if(dcnd != null)
			dcnd.close();
		DCNUpdateNotifier.destroy();
		emd.close();
		dcnd = null;
		emd = null;
	}

}
