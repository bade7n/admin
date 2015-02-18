package pro.deta.detatrak.controls.schedule;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.VerticalLayout;

import pro.deta.detatrak.controls.schedule.converter.ScheduleBuilder;
import pro.deta.detatrak.controls.schedule.converter.ScheduleElement;
import pro.deta.detatrak.presenter.JPAEntityViewBase;
import ru.yar.vi.rm.data.ScheduleDO;

import java.util.LinkedList;
import java.util.List;

public class ScheduleView2 extends JPAEntityViewBase<ScheduleDO> {
    public static final String NAV_KEY = "scheduleView";
    private List<ScheduleElement> scheduleElements = new LinkedList<>();
    VerticalLayout scheduleElementsLayout;

    public ScheduleView2() {
        super(ScheduleDO.class);
    }

    @Override
    protected void initForm(FieldGroup binder,ScheduleDO schedule) {
        form.addComponent(new ComboBox("Объект"));
        form.addComponent(new ComboBox("Тип клиента"));
//        Panel p = new Panel("График");
        //Строим блоки расписания по строке
        scheduleElements = ScheduleBuilder
                .getUIByString("10:00-13:00+14:00-15:00|6;10:00-13:00+14:00-17:00|245&39,41;10:00-12:00|135&1-23,3,2", null);

        scheduleElementsLayout = new VerticalLayout();
        for(ScheduleElement e : scheduleElements) {
            scheduleElementsLayout.addComponent(e);
        }
        //убираем "-" у первого блока
        scheduleElements.get(0).setFirst();
        addComponent(scheduleElementsLayout);
        Button plusButton = new Button("+");
        plusButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                plus();
            }
        });
        addComponent(plusButton);
    }

    //сформировать строку по UI
    private String getScheduleString() {
        StringBuilder sb = new StringBuilder();
        for(ScheduleElement e : scheduleElements) {
            sb.append(e.getString() + ";");
        }
        String result = sb.toString();
        return result.substring(0, result.length() - 1);
    }

    //убрать блок расписания
    public void minus(ScheduleElement element) {
        scheduleElementsLayout.removeComponent(element);
        scheduleElements.remove(element);
    }

    //добавить блок расписания
    public void plus() {
        ScheduleElement e = ScheduleBuilder.emptyScheduleElement(null);
        scheduleElements.add(e);
        scheduleElementsLayout.addComponent(e);
    }
	@Override
	public String getNavKey() {
		return NAV_KEY;
	}

}
