package pro.deta.detatrak.vadmin;

import java.util.Collection;

import org.junit.Test;

import pro.deta.detatrak.controls.schedule.WeekendOfficeQueryModifierDelegate;
import pro.deta.detatrak.util.JPAUtils;
import ru.yar.vi.rm.data.ObjectDO;
import ru.yar.vi.rm.data.OfficeDO;
import ru.yar.vi.rm.data.WeekendDO;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.filter.JoinFilter;
import com.vaadin.addon.jpacontainer.util.HibernateLazyLoadingDelegate;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Compare;

public class WeekendTest extends BaseTest {
	@Test
	public void officeFilterTest() {
		OfficeDO office = new OfficeDO();
		office.setId(5);
		Filter filter = new JoinFilter("objects", new Compare.Equal("office", office));
		JPAContainer<WeekendDO> container = JPAUtils.createCachingJPAContainer(WeekendDO.class);
		container.getEntityProvider().setLazyLoadingDelegate(new HibernateLazyLoadingDelegate());

		container.addContainerFilter(filter);
		Collection<Object> ids = container.getItemIds();



		for (Object object : ids) {
			WeekendDO o = em.find(WeekendDO.class, object);
//			System.out.println("Weekend: " +format(o));
			if(o.getObjects() != null && !o.getObjects().isEmpty())
				for(ObjectDO o1 : o.getObjects()) {
//					System.out.println("Object: " +format(o1));
					if(o1.getChilds() != null && !o1.getChilds().isEmpty())
						for(Object o2 : o1.getChilds()) {
							String s = format(o2);
//							System.out.println("Object child: " +format(o2));
						}
				}
		}

	}

	@Test
	public void weekendFilterTest() {
		OfficeDO office = new OfficeDO();
		office.setId(1);
		JPAContainer<WeekendDO> container = JPAUtils.createCachingJPAContainer(WeekendDO.class);

		container.getEntityProvider().setQueryModifierDelegate(new WeekendOfficeQueryModifierDelegate());


//		container.removeAllContainerFilters();
//		container.addContainerFilter(
//				Filters.joinFilter("objects", 
//						Filters.eq("office", office))
//				);
		System.out.println("Weekend: " +container.size());
		
		int i=0;
		Collection<Object> ids = container.getItemIds();
		for (Object object : ids) {
			WeekendDO o = em.find(WeekendDO.class, object);
			System.out.println("" + i++ + "Weekend: " +o);
		}

	}

}
