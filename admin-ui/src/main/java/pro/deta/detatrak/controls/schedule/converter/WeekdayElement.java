package pro.deta.detatrak.controls.schedule.converter;

import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;

import java.util.EnumSet;

/**
 * User: andy
 * Date: 4/29/14
 * Time: 1:01 AM
 */
public class WeekdayElement extends HorizontalLayout{
    CheckBox monday,tuesday, wednesday,  thursday, friday, saturday, sunday;
    public WeekdayElement(EnumSet<Weekday> wd) {
        monday = new CheckBox("пн", wd.contains(Weekday.MONDAY));
        tuesday = new CheckBox("вт", wd.contains(Weekday.TUESDAY));
        wednesday = new CheckBox("ср", wd.contains(Weekday.WEDNESDAY));
        thursday = new CheckBox("чт", wd.contains(Weekday.THURSDAY));
        friday = new CheckBox("пт", wd.contains(Weekday.FRIDAY));
        saturday = new CheckBox("сб", wd.contains(Weekday.SATURDAY));
        sunday = new CheckBox("вс", wd.contains(Weekday.SUNDAY));

        addComponent(monday);
        addComponent(tuesday);
        addComponent(wednesday);
        addComponent(thursday);
        addComponent(friday);
        addComponent(saturday);
        addComponent(sunday);
    }

    public String getString() {
        StringBuilder sb = new StringBuilder();
        if(monday.getValue()) sb.append('1');
        if(tuesday.getValue()) sb.append('2');
        if(wednesday.getValue()) sb.append('3');
        if(thursday.getValue()) sb.append('4');
        if(friday.getValue()) sb.append('5');
        if(saturday.getValue()) sb.append('6');
        if(sunday.getValue()) sb.append('7');
        return sb.toString();
    }
}
