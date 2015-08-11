package pro.deta.detatrak;

import java.io.Serializable;

import javax.persistence.EntityManager;

import com.vaadin.addon.jpacontainer.EntityManagerProvider;

public class LazyHibernateEntityManagerProvider implements EntityManagerProvider,Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -716191104935406008L;

	transient private ThreadLocal<EntityManager> entityManagerThreadLocal = new ThreadLocal<EntityManager>();

	private static LazyHibernateEntityManagerProvider provider = null;
	private static Object _lock = new Object();

	private LazyHibernateEntityManagerProvider() {
		
	}
	
	public static LazyHibernateEntityManagerProvider getInstance() {
		if(provider == null ) {
			synchronized (_lock) {
				if(provider == null)
					provider = new LazyHibernateEntityManagerProvider();
			}
		}
		return provider;
	}
	
	@Override
	public EntityManager getEntityManager() {
		return getEntityManagerThreadLocal().get();
	}

	private ThreadLocal<EntityManager> getEntityManagerThreadLocal() {
		if(entityManagerThreadLocal == null) {
			synchronized (_lock) {
				if(entityManagerThreadLocal== null)
					entityManagerThreadLocal = new ThreadLocal<>();
			}
		}
		return entityManagerThreadLocal;
	}

	public void setCurrentEntityManager(EntityManager em) {
		getEntityManagerThreadLocal().set(em);
	}
	
	public void remove() {
		getEntityManagerThreadLocal().remove();
	}

}