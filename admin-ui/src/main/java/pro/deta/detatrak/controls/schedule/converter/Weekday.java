package pro.deta.detatrak.controls.schedule.converter;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * User: andy
 * Date: 4/27/14
 * Time: 3:35 PM
 */
public enum Weekday {
    MONDAY(1), TUESDAY(2), WEDNESDAY(3), THURSDAY(4), FRIDAY(5), SATURDAY(6), SUNDAY(7);
    private Integer value;

    private Weekday(int value) {
        this.value = value;
    }

    private static final Map<Integer, Weekday> map = new HashMap<>();
    static {
        for(Weekday w : Weekday.values()) {
            map.put(w.value, w);
        }
    }

    public static Weekday getByStrValue(Character strValue) {
        try {
        return map.get(Character.getNumericValue(strValue));
        } catch (NumberFormatException ex) {
            throw new RuntimeException("incorrect week value:" + strValue);
        }

    }
}
