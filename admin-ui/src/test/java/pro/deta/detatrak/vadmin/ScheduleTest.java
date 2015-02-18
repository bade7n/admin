package pro.deta.detatrak.vadmin;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Compare;

import org.junit.Ignore;
import org.junit.Test;

import pro.deta.detatrak.util.JPAUtils;
import ru.yar.vi.rm.data.ScheduleDO;

import java.util.Collection;


public class ScheduleTest extends BaseTest {

	//@Test
    @Ignore
    public void scheduleFilterTest() {
    	Filter filter = new Compare.Equal("object.office", 1);
    	JPAContainer<ScheduleDO> scheduleContainer = JPAUtils.createCachingJPAContainer(ScheduleDO.class);
		scheduleContainer.removeAllContainerFilters();
		scheduleContainer.addContainerFilter(filter);
		scheduleContainer.addNestedContainerProperty("object.office");
		Collection<Object> ids = scheduleContainer.getItemIds();
		for (Object object : ids) {
			ScheduleDO sdo = em.find(ScheduleDO.class, object);
		}
		
    }
}
