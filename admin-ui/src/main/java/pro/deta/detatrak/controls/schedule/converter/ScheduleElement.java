package pro.deta.detatrak.controls.schedule.converter;

import java.util.LinkedList;
import java.util.List;

import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class ScheduleElement extends FormLayout {
    private final WeeksElement weeks;
    List<TimeElement> timings = new LinkedList<>();
    WeekdayElement weekdays;

    VerticalLayout timingsLayout;
    Button minusButton;
    final ScheduleBlock view;

    public ScheduleElement(SchedulePattern pattern, ScheduleBlock view) {
        this.view = view;

        addComponent(new Label("По времени работы:"));
        int timingsCount = pattern.timings.size();
        timingsLayout = new VerticalLayout();
        for(int i = 0; i < timingsCount; ++i) {
            Timing t = pattern.timings.get(i);
            TimeElement timeElement;
            if(i == 0) {
                timeElement = TimeElement.createFirst(t, timingsCount == 1, this);
            } else if (i == timingsCount - 1) {
                timeElement = TimeElement.createLast(t, this);
            } else {
                timeElement = TimeElement.createMiddle(t, this);
            }
            timingsLayout.addComponent(timeElement);
            timings.add(timeElement);
        }
        addComponent(timingsLayout);
        addComponent(new Label("По дням недели:"));
        weekdays = new WeekdayElement(pattern.weekdays);
        addComponent(weekdays);
        addComponent(new Label("По неделям:"));
        weeks = new WeeksElement(pattern);
        addComponent(weeks);
        minusButton = new Button("-");
        minusButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                closeThis();
            }
        });
        addComponent(minusButton);
    }

    public String getString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < timings.size();++i) {
            sb.append(timings.get(i).getString());
            if(i != timings.size() - 1)
                sb.append('+');
        }
        sb.append('|');
        sb.append(weekdays.getString());
        if(!weeks.getAllSelected()) {
            sb.append('&');
            sb.append((weeks.getString()));
        }
        return sb.toString();
    }

    void minus(TimeElement t) {
        timings.remove(t);
        timingsLayout.removeComponent(t);
        timings.get(timings.size()-1).setLast();
        if(timings.size() == 1) {
            timings.get(0).setFirst();
        }
    }
    void plus(TimeElement t) {
        t.setMiddle();
        TimeElement timeElement = TimeElement.createLast(new Timing("00:00", "00:00"), this);
        timings.add(timeElement);
        timingsLayout.addComponent(timeElement);
    }

    private void closeThis(){
        view.minus(this);
    }

    public void setFirst() {
        minusButton.setVisible(false);
    }


}

