package pro.deta.detatrak.controls.schedule.converter;

/**
 * User: andy
 * Date: 4/27/14
 * Time: 3:30 PM
 */
public class Timing {
    public String begin;
    public String end;

    public Timing(String begin, String end) {
        this.begin = begin;
        this.end = end;
    }

    @Override
    public String toString() {
        return begin + '-' + end;
    }
}
