package pro.deta.detatrak;

import com.vaadin.event.DataBoundTransferable;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.event.dd.acceptcriteria.ServerSideCriterion;
import com.vaadin.server.PaintException;
import com.vaadin.server.PaintTarget;
import com.vaadin.shared.ui.dd.VerticalDropLocation;
import com.vaadin.ui.AbstractSelect.AbstractSelectTargetDetails;

public class CorrectCriteria extends ServerSideCriterion {

	@Override
	public boolean accept(DragAndDropEvent dragEvent) {
		// only accept drags within the table
        if ( !(dragEvent.getTransferable() instanceof DataBoundTransferable)) {
            return false;
        }

        // AbstractSelectDropTargetDetails as in a Table
        final AbstractSelectTargetDetails dropData = (AbstractSelectTargetDetails) dragEvent
                .getTargetDetails();
        // only allow drop over a row, not between rows
        if (!VerticalDropLocation.MIDDLE.equals(dropData.getDropLocation())) {
            return false;
        }

        final DataBoundTransferable t = (DataBoundTransferable) dragEvent
                .getTransferable();

        // check that two different persons whose last names match
        final Object sourceItemId = t.getItemId();
        final Object targetItemId = dropData.getItemIdOver();
        if (sourceItemId.equals(targetItemId)) {
            return false;
        }
        
        return true;
	}

}
