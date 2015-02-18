package pro.deta.detatrak;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import pro.deta.detatrak.dao.EMDAO;
import pro.deta.detatrak.dao.listener.DCNDAO;
import pro.deta.detatrak.dao.listener.DCNNotificatorCallback;
import pro.deta.detatrak.dao.listener.DCNUpdateNotifier;


/**
 * @deprecated Use VadminVaadinServlet instead. The Thread local variable can't be reached if they are set in filter possible due to different classloaders. 
 * 
 * @author VIlmov
 *
 */
@Deprecated
public class LazyHibernateServletFilter implements Filter {
	private static DCNDAO dcnd = null;
	private EMDAO emd;
	private LazyHibernateEntityManagerProvider lhemp;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		emd = EMDAO.getInstance();
		dcnd = new DCNDAO(new DCNNotificatorCallback(emd),emd);
		DCNUpdateNotifier.init(emd);
		lhemp = LazyHibernateEntityManagerProvider.getInstance();
	}

	@Override
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) servletRequest;
		try {
			EntityManager em = EMDAO.getInstance().createEntityManager();
			// Create and set the entity manager
			lhemp.setCurrentEntityManager(em);
			
			// Handle the request
			filterChain.doFilter(servletRequest, servletResponse);
		} finally {
			// Reset the entity manager
			lhemp.setCurrentEntityManager(null);
		}
	}

	@Override
	public void destroy() {
		dcnd.close();
		DCNUpdateNotifier.destroy();
		emd.close();
		dcnd = null;
		emd = null;
	}
}