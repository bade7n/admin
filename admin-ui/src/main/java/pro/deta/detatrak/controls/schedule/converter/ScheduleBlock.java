package pro.deta.detatrak.controls.schedule.converter;

import java.util.LinkedList;
import java.util.List;

import com.vaadin.ui.VerticalLayout;

public class ScheduleBlock {
	private List<ScheduleElement> scheduleElements = new LinkedList<>();
	private VerticalLayout scheduleElementsLayout = new VerticalLayout();
	
	
	//убрать блок расписания
    public void minus(ScheduleElement element) {
        scheduleElementsLayout.removeComponent(element);
        scheduleElements.remove(element);
    }

    //добавить блок расписания
    public void plus() {
        ScheduleElement e = ScheduleBuilder.emptyScheduleElement(this);
        scheduleElements.add(e);
        scheduleElementsLayout.addComponent(e);
    }

	public List<ScheduleElement> getScheduleElements() {
		return scheduleElements;
	}

	public void setScheduleElements(List<ScheduleElement> scheduleElements) {
		this.scheduleElements = scheduleElements;
	}

	public VerticalLayout getScheduleElementsLayout() {
		return scheduleElementsLayout;
	}

	public void setScheduleElementsLayout(VerticalLayout scheduleElementsLayout) {
		this.scheduleElementsLayout = scheduleElementsLayout;
	}
}
