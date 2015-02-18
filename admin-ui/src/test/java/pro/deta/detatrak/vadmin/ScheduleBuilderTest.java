package pro.deta.detatrak.vadmin;

import org.junit.Test;
import pro.deta.detatrak.controls.schedule.converter.ScheduleBuilder;
import pro.deta.detatrak.controls.schedule.converter.SchedulePattern;

import java.util.List;

/**
 * User: andy
 * Date: 4/27/14
 * Time: 2:04 PM
 */
public class ScheduleBuilderTest {


    @Test
    public void tokenizerTest() throws Exception {
        List<SchedulePattern> patterns = ScheduleBuilder
                .stringToModel("10:00-13:00+14:00-15:00|6;10:00-13:00+14:00-17:00|245&39,41;10:00-12:00|135&1-23,3,2");
        for(SchedulePattern p : patterns)
            System.out.println(p);

    }

}
