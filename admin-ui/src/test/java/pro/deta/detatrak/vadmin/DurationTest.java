package pro.deta.detatrak.vadmin;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Compare;

import org.junit.Ignore;
import org.junit.Test;

import pro.deta.detatrak.util.JPAUtils;
import ru.yar.vi.rm.data.DurationDO;
import ru.yar.vi.rm.data.OfficeDO;

import java.util.Collection;

public class DurationTest extends BaseTest {
	//@Test
    @Ignore
	public void officeFilterTest() {
		OfficeDO office = new OfficeDO();
		office.setId(5);
		Filter filter = new Compare.Equal("object.office", office);
		JPAContainer<DurationDO> container = JPAUtils.createCachingJPAContainer(DurationDO.class);
		container.removeAllContainerFilters();
		container.addContainerFilter(filter);
		Collection<Object> ids = container.getItemIds();
		for (Object object : ids) {
			DurationDO o = em.find(DurationDO.class, object);
//			System.out.println("Duration: " +format(o));
			
		}

	}

}
