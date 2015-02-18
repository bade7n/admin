package pro.deta.detatrak.controls.schedule.converter;

import com.vaadin.data.Property;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextField;

/**
 * User: andy
 * Date: 4/29/14
 * Time: 1:12 AM
 */
public class WeeksElement extends HorizontalLayout {
    private static final String odd = "четные";
    private static final String even = "нечетные";
    private static final String custom = "задать номера";
    OptionGroup o;
    private TextField tx;

    public WeeksElement(SchedulePattern p) {
        //TODO: сделать радиобаттоны горизонтально при помощи стилей
         o = new OptionGroup();
        o.addItem(odd);
        o.addItem(even);
        o.addItem(custom);
        o.setImmediate(true);
        o.addListener(new Property.ValueChangeListener(){

            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                String field = (String)valueChangeEvent.getProperty().getValue();
                boolean txVisible = custom.equals(field);
                tx.setVisible(txVisible);
            }
        });
        addComponent(o);
        tx = new TextField();
        addComponent(tx);
        tx.setVisible(false);

        if(p.allOdd) {
            o.select(odd);
        } else if(p.allEven) {
            o.select(even);
        } else if(!p.weeks.isEmpty() || !p.weekPeriods.isEmpty()){
            o.select(custom);
            tx.setVisible(true);
            tx.setValue(p.getWeekStr());
        }
    }


    public Boolean getAllSelected() {
        return o.getValue() == null
                || (custom.equals(o.getValue()) && tx.getValue().trim() == "");
    }

    public String getString() {
        if(o.getValue().equals(odd))
            return "o";
        if(o.getValue().equals(even))
            return "e";
        return tx.getValue().trim();
    }
}
