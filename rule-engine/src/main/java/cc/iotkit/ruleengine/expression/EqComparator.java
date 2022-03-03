package cc.iotkit.ruleengine.expression;


public class EqComparator extends BaseComparator {

    @Override
    public String getName() {
        return "==";
    }

    @Override
    public String getScript() {
        return "a==b";
    }

}
