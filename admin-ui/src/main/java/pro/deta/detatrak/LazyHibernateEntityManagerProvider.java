package pro.deta.detatrak;

import javax.persistence.EntityManager;

import com.vaadin.addon.jpacontainer.EntityManagerProvider;

public class LazyHibernateEntityManagerProvider implements EntityManagerProvider {
	private ThreadLocal<EntityManager> entityManagerThreadLocal = new ThreadLocal<EntityManager>();

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
		return entityManagerThreadLocal.get();
	}

	public void setCurrentEntityManager(EntityManager em) {
		entityManagerThreadLocal.set(em);
	}
	
	public void remove() {
		entityManagerThreadLocal.remove();
	}

}