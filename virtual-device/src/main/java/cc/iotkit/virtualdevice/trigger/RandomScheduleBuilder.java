package cc.iotkit.virtualdevice.trigger;

import org.quartz.ScheduleBuilder;
import org.quartz.spi.MutableTrigger;

public class RandomScheduleBuilder extends ScheduleBuilder<RandomTrigger> {

    private final String unit;

    public RandomScheduleBuilder(String unit) {
        this.unit = unit;
    }

    public MutableTrigger build() {
        return new RandomTrigger(unit);
    }

}
