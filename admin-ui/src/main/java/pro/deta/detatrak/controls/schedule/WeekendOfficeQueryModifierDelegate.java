package pro.deta.detatrak.controls.schedule;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import pro.deta.detatrak.MyUI;
import ru.yar.vi.rm.data.ObjectDO;
import ru.yar.vi.rm.data.ObjectDO_;
import ru.yar.vi.rm.data.OfficeDO;
import ru.yar.vi.rm.data.WeekendDO;
import ru.yar.vi.rm.data.WeekendDO_;

import com.vaadin.addon.jpacontainer.util.DefaultQueryModifierDelegate;

public class WeekendOfficeQueryModifierDelegate extends	DefaultQueryModifierDelegate {

	/**
	 * 
	 */
	private static final long serialVersionUID = 840477849540288451L;
	public WeekendOfficeQueryModifierDelegate() {
	}

	@Override
	public void filtersWillBeAdded(
			CriteriaBuilder cb,
			CriteriaQuery<?> query,
			List<Predicate> predicates) {
		OfficeDO office =	MyUI.getCurrentUI().getOffice();
		if(office != null) {
			Root<WeekendDO> root = (Root<WeekendDO>) query.getRoots().iterator().next();

			Subquery<WeekendDO> subquery = query.subquery(WeekendDO.class);
			Root<WeekendDO> o = subquery.from(WeekendDO.class);
			ListJoin<WeekendDO, ObjectDO> objJoin = o.join(WeekendDO_.objects);
			subquery.select(o).where(cb.equal(objJoin.get(ObjectDO_.office),office));
			predicates.add(root.in(subquery));
		}
		super.filtersWillBeAdded(cb, query, predicates);
	}
}
