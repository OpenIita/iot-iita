package cc.iotkit.ruleengine.expression;


public class GtComparator extends BaseComparator {

    @Override
    public String getName() {
        return ">";
    }

    @Override
    public String getScript() {
        return "a>b";
    }

}
