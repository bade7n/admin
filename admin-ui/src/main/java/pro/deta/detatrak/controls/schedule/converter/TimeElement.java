package pro.deta.detatrak.controls.schedule.converter;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbstractSelect.NewItemHandler;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;

import java.util.Arrays;
import java.util.List;

public class TimeElement extends HorizontalLayout {
    private Button plusButton = new Button("+");
    private Button minusButton = new Button("-");
    private ComboBox beginCombo;
    private ComboBox endCombo;
    private ScheduleElement parent;


    private TimeElement(Timing t, final ScheduleElement parent) {
        this.parent = parent;
        beginCombo = createComboWithTimings("с:", t.begin);
        addComponent(beginCombo);
        endCombo = createComboWithTimings("по:", t.end);
        addComponent(endCombo);
        addComponent(minusButton);
        addComponent(plusButton);
        minusButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) { minus(); }
        });
        plusButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) { plus(); }
        });
    }

    private void plus() {
        parent.plus(this);
    }

    private void minus() {
        parent.minus(this);
    }


    public static TimeElement createFirst(Timing t, boolean single, ScheduleElement parent) {
        TimeElement te = new TimeElement(t, parent);
        te.minusButton.setVisible(false);
        if (!single) {
            te.plusButton.setVisible(false);
        }
        return te;
    }

    public static TimeElement createMiddle(Timing t, ScheduleElement parent) {
        TimeElement te = new TimeElement(t, parent);
        te.plusButton.setVisible(false);
        return te;
    }

    public static TimeElement createLast(Timing t, ScheduleElement parent) {
        TimeElement te = new TimeElement(t, parent);
        return te;
    }

    private static final List<String> ComboProvider = Arrays.asList("00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00",
            "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "24:00");

    private static ComboBox createComboWithTimings(String caption, String selectedTiming) {
        final ComboBox result = new ComboBox(caption);
        for (String itel : ComboProvider) {
			result.addItem(itel);
		};

//        result.setNullSelectionAllowed(false);
		if(!result.containsId(selectedTiming))
			result.addItem(selectedTiming);
		
        result.setValue(selectedTiming);
        result.setTextInputAllowed(true);
        result.setNewItemsAllowed(true);
//        result.setFilteringMode(FilteringMode.OFF);
//        result.setInvalidAllowed(true);
        result.setImmediate(true);

        result.setNewItemHandler(new NewItemHandler() {
			
			@Override
			public void addNewItem(String newItemCaption) {
				if (!result.containsId(newItemCaption)) {
		            result.addItem(newItemCaption);
		            result.setValue(newItemCaption);
		        }
			}
		});
        return result;
    }

    String getString() {
        return beginCombo.getValue().toString() + "-" + endCombo.getValue().toString();
    }

    void setMiddle() {
        plusButton.setVisible(false);
    }
    void setLast() {
        plusButton.setVisible(true);
    }
    void setFirst() {
        minusButton.setVisible(false);
    }

}
