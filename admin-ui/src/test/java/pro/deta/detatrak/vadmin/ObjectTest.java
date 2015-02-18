package pro.deta.detatrak.vadmin;

import org.junit.Test;
import ru.yar.vi.rm.data.ObjectDO;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ObjectTest extends BaseTest {
	@Test
	public void officeSortChild() {
		ObjectDO object = em.find(ObjectDO.class, 79);
		for(ObjectDO o1 : object.getChilds() ) {
			System.out.println("Child objects: "+format(o1));
		}
		List<ObjectDO> childs = object.getChilds();
		Collections.sort(childs, new Comparator<ObjectDO>(){
		     public int compare(ObjectDO o1, ObjectDO o2){
		         return o1.getName().compareTo(o2.getName());
		     }
		});
		em.getTransaction().begin();
		object.setChilds(childs);
		em.getTransaction().commit();
	}
}
