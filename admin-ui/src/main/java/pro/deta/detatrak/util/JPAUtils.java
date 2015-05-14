package pro.deta.detatrak.util;

import javax.persistence.EntityManager;

import pro.deta.detatrak.HibernateLazyLoadingDelegate;
import pro.deta.detatrak.LazyHibernateEntityManagerProvider;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.addon.jpacontainer.provider.CachingMutableLocalEntityProvider;
import com.vaadin.addon.jpacontainer.provider.LocalEntityProvider;

public class JPAUtils {
	static public<T extends Object> JPAContainer<T> createCachingJPAContainer(Class<T> class1) {
		JPAContainer<T> cont = JPAContainerFactory.make(class1, (EntityManager) null);
		CachingMutableLocalEntityProvider<T> entityProvider = new CachingMutableLocalEntityProvider<T>(class1);
		entityProvider.setEntitiesDetached(false);
		entityProvider.setCloneCachedEntities(false);
		entityProvider.setEntityManagerProvider(LazyHibernateEntityManagerProvider.getInstance());
//		entityProvider.setEntitiesDetached(true);
		entityProvider.setLazyLoadingDelegate(new HibernateLazyLoadingDelegate());
		
//		LocalEntityProvider<T> entityProvider1 = new LocalEntityProvider<T>(class1);
//		entityProvider1.setEntityManagerProvider(LazyHibernateEntityManagerProvider.getInstance());
//		entityProvider1.setLazyLoadingDelegate(new HibernateLazyLoadingDelegate());
//		entityProvider1.setEntitiesDetached(false);
		cont.setEntityProvider(entityProvider);
        return cont;
	}
	
	public static EntityManager getEntityManager() {
		return LazyHibernateEntityManagerProvider.getInstance().getEntityManager();
	}
	
	public static void save(Object o) {
		try {
			if(!getEntityManager().getTransaction().isActive())
				getEntityManager().getTransaction().begin();
			o = getEntityManager().merge(o);
			getEntityManager().persist(o);
			getEntityManager().getTransaction().commit();
		} finally {
			if(getEntityManager().getTransaction().isActive())
				getEntityManager().getTransaction().rollback();
		}
	}

	public static void remove(Object target) {
		try {
			if(!getEntityManager().getTransaction().isActive())
				getEntityManager().getTransaction().begin();
			getEntityManager().remove(target);
			getEntityManager().flush();
			getEntityManager().getTransaction().commit();
		} finally {
			if(getEntityManager().getTransaction().isActive())
				getEntityManager().getTransaction().rollback();
		}
	}
}
