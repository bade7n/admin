package pro.deta.detatrak.controls.schedule.converter;

import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

/**
 * User: andy
 * Date: 4/27/14
 * Time: 3:35 PM
 */
public class SchedulePattern {
    public List<Timing> timings;
    public EnumSet<Weekday> weekdays;
    public List<Integer> weeks = new LinkedList<>(); //недели одной цифрой типа 2,3,5
    public List<Timing> weekPeriods = new LinkedList<>(); //периоды недель вида 1-4
    public boolean allOdd;//все четные недели года
    public boolean allEven;//все нечетные недели года

    public SchedulePattern(List<Timing> timings, EnumSet<Weekday> weekdays, List<Integer> weeks, List<Timing> weekPeriods) {
        this.timings = timings;
        this.weekdays = weekdays;
        this.weeks = weeks;
        this.weekPeriods = weekPeriods;
    }

    public SchedulePattern(List<Timing> timings, EnumSet<Weekday> weekdays, boolean allOdd, boolean allEven) {
        this.timings = timings;
        this.weekdays = weekdays;
        this.allOdd = allOdd;
        this.allEven = allEven;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("timings: ");
        for (Timing t : timings) {
            sb.append(t + " ");
        }
        sb.append("weekdays:");
        for (Weekday w : weekdays) {
            sb.append(w + " ");
        }
        sb.append("weeks:");
        if (weeks.isEmpty() && weekPeriods.isEmpty()) {
            if (allEven)
                sb.append(" all even");
            else if (allOdd)
                sb.append(" all odd");
            else
                sb.append(" all");
        } else {
            weekStr(sb);
        }

        return sb.toString();
    }

    private void weekStr(StringBuilder sb) {
        for (Number n : weeks)
            sb.append(n + ",");

        for (Timing t : weekPeriods) {
            sb.append(t + ",");
        }

    }

    public String getWeekStr() {
        StringBuilder sb = new StringBuilder();
        weekStr(sb);
        String res = sb.toString();
        return res.substring(0, res.length() - 1);
    }
}
