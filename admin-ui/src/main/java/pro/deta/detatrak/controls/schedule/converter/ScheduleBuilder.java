package pro.deta.detatrak.controls.schedule.converter;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;

import pro.deta.detatrak.controls.schedule.ScheduleView;

/**
 * User: andy
 * Date: 4/27/14
 * Time: 2:01 PM
 */
public class ScheduleBuilder {

    private static final String DEFAULT_PATTERN = "08:00-12:00|12345";

	public static List<SchedulePattern> stringToModel(String input) {
        List<SchedulePattern> result = new LinkedList<>();

        String[] patterns = input.split(";");
        for (String pattern : patterns) {
        	if("".equalsIgnoreCase(pattern))
        		continue;
        	
            String[] patternParts = pattern.split("\\|");
            String[] timings = patternParts[0].split("\\+");

            //получаем интервалы времени
            List<Timing> t = new LinkedList<>();
            for (String timing : timings) {
                String[] hours = timing.split("-");
                if(hours.length > 1)
                	t.add(new Timing(hours[0], hours[1]));
            }
            //получаем дни недели
            String weeks = (patternParts.length > 1) ? patternParts[1] : "";
            String[] weekParts = weeks.split("\\&");
            EnumSet<Weekday> wd = EnumSet.noneOf(Weekday.class);

            String dayOfWeeks = weekParts[0];
            for (int i = 0; i < dayOfWeeks.length(); ++i) {
                wd.add(Weekday.getByStrValue(dayOfWeeks.charAt(i)));
            }

            if (weekParts.length == 1) {
                result.add(new SchedulePattern(t, wd, false, false));
                continue;
            }
            //получаем недели
            List<Integer> w = new LinkedList<>();
            List<Timing> wp = new LinkedList<>();
            String[] weekNumberPatterns = weekParts[1].split(",");
            if (weekNumberPatterns.length == 1) {
                if (weekNumberPatterns[0].equals("o")) {
                    result.add(new SchedulePattern(t, wd, true, false));
                } else if (weekNumberPatterns[0].equals("e")) {
                    result.add(new SchedulePattern(t, wd, true, false));
                }
            }
            for (String weekNumberPattern : weekNumberPatterns) {
                //just week number
                try {
                    w.add(Integer.parseInt(weekNumberPattern));
                } catch (NumberFormatException ex) {
                    //period, ex.: 3-5
                    String[] p = weekNumberPattern.split("-");
                    wp.add(new Timing(p[0], p[1]));
                }
            }
            result.add(new SchedulePattern(t, wd, w, wp));
        }
        return result;
    }

    public static ScheduleElement emptyScheduleElement(ScheduleBlock parent) {
        List<SchedulePattern> p = stringToModel(DEFAULT_PATTERN);
        return modelToUI(p, parent).get(0);
    }

    public static List<ScheduleElement> modelToUI(List<SchedulePattern> patterns, ScheduleBlock parent) {
        List<ScheduleElement> result = new LinkedList<>();
        for(SchedulePattern p : patterns) {
            result.add(new ScheduleElement(p, parent));
        }
        return result;
    }

    public static List<ScheduleElement> getUIByString(String input, ScheduleBlock view) {
        List<SchedulePattern> p = stringToModel(input);
        return modelToUI(p, view);
    }
    
    public static ScheduleBlock getScheduleByString(String input) {
    	final ScheduleBlock sb = new ScheduleBlock();
    	sb.setScheduleElements(ScheduleBuilder.getUIByString(input, sb));
        for(ScheduleElement e : sb.getScheduleElements()) {
            sb.getScheduleElementsLayout().addComponent(e);
        }
        if(sb.getScheduleElements().size() > 0)
        //убираем "-" у первого блока
        	sb.getScheduleElements().get(0).setFirst();
        Button plusButton = new Button("+");
        plusButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                sb.plus();
            }
        });
        sb.getScheduleElementsLayout().addComponent(plusButton);
    	return sb;
    }
    
    public static String getScheduleString(ScheduleBlock sblock) {
        StringBuilder sb = new StringBuilder();
        for(ScheduleElement e : sblock.getScheduleElements()) {
            sb.append(e.getString() + ";");
        }
        String result = sb.toString();
        return result.substring(0, result.length() - 1);
    }
}
