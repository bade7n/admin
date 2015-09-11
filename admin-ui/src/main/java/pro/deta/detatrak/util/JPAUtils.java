package pro.deta.detatrak.util;

import javax.persistence.EntityManager;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.addon.jpacontainer.provider.CachingMutableLocalEntityProvider;
import com.vaadin.addon.jpacontainer.provider.MutableLocalEntityProvider;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;

import pro.deta.detatrak.HibernateLazyLoadingDelegate;
import pro.deta.detatrak.LazyHibernateEntityManagerProvider;
import ru.yar.vi.rm.data.OfficeDO;

public class JPAUtils {
	static public<T extends Object> JPAContainer<T> createCachingJPAContainer(Class<T> class1) {
		JPAContainer<T> cont = JPAContainerFactory.make(class1, (EntityManager) null);
		CachingMutableLocalEntityProvider<T> entityProvider = new CachingMutableLocalEntityProvider<T>(class1);
		entityProvider.setEntitiesDetached(false);
		entityProvider.setCloneCachedEntities(false);
		entityProvider.setEntityManagerProvider(LazyHibernateEntityManagerProvider.getInstance());
		entityProvider.setLazyLoadingDelegate(new HibernateLazyLoadingDelegate());
		cont.setEntityProvider(entityProvider);
        return cont;
	}
	
	public static <E> E getBeanByItem(Item item) {
		E bean = null;
		if(item instanceof EntityItem)
			bean = ((EntityItem<E>) item).getEntity();
		else if (item instanceof BeanItem) {
			bean = ((BeanItem<E>) item).getBean();
		}
		if(bean != null)
			bean = getEntityManager().merge(bean);
		return bean;
	}
	
	static public<T extends Object> JPAContainer<T> createJPAContainer(Class<T> class1) {
		JPAContainer<T> cont = JPAContainerFactory.make(class1, (EntityManager) null);
		MutableLocalEntityProvider<T> entityProvider = new MutableLocalEntityProvider<T>(class1);
		entityProvider.setEntitiesDetached(false);
		entityProvider.setEntityManagerProvider(LazyHibernateEntityManagerProvider.getInstance());
		entityProvider.setLazyLoadingDelegate(new HibernateLazyLoadingDelegate());
		cont.setEntityProvider(entityProvider);
        return cont;
	}
	
	public static EntityManager getEntityManager() {
		return LazyHibernateEntityManagerProvider.getInstance().getEntityManager();
	}
	
	public static<T> T save(T o) {
		T merged = null;
		try {
			if(!getEntityManager().getTransaction().isActive())
				getEntityManager().getTransaction().begin();
			merged = getEntityManager().merge(o);
			getEntityManager().persist(merged);
			getEntityManager().getTransaction().commit();
		} finally {
			if(getEntityManager().getTransaction().isActive())
				getEntityManager().getTransaction().rollback();
		}
		return merged;
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
