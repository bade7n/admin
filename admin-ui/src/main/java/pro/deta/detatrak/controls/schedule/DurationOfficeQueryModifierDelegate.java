package pro.deta.detatrak.controls.schedule;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import com.vaadin.addon.jpacontainer.util.DefaultQueryModifierDelegate;

import pro.deta.detatrak.MyUI;
import ru.yar.vi.rm.data.DurationDO;
import ru.yar.vi.rm.data.DurationDO_;
import ru.yar.vi.rm.data.ObjectDO;
import ru.yar.vi.rm.data.ObjectDO_;
import ru.yar.vi.rm.data.OfficeDO;


public class DurationOfficeQueryModifierDelegate extends	DefaultQueryModifierDelegate {

	/**
	 * 
	 */
	private static final long serialVersionUID = 840477849540288451L;
	public DurationOfficeQueryModifierDelegate() {
	}

	@Override
	public void filtersWillBeAdded(
			CriteriaBuilder cb,
			CriteriaQuery<?> query,
			List<Predicate> predicates) {
		OfficeDO office =	MyUI.getCurrentUI().getOffice();
		if(office != null) {
			Root<DurationDO> root = (Root<DurationDO>) query.getRoots().iterator().next();

			Subquery<DurationDO> subquery = query.subquery(DurationDO.class);
			Root<DurationDO> o = subquery.from(DurationDO.class);
			Join<DurationDO, ObjectDO> objJoin = o.join(DurationDO_.object,JoinType.LEFT);
			Path<OfficeDO> path = o.get(DurationDO_.office);
			subquery.select(o).where(
					cb.or(
							cb.equal(path, office),
							cb.equal(objJoin.get(ObjectDO_.office),office)
					));
			predicates.add(root.in(subquery));
		}
		super.filtersWillBeAdded(cb, query, predicates);
	}
}
