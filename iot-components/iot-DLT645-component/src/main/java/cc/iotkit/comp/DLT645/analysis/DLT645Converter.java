package cc.iotkit.comp.DLT645.analysis;

import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.thing.ThingService;
import cc.iotkit.common.utils.JsonUtils;
import cc.iotkit.common.utils.UniqueIdUtil;
import cc.iotkit.comp.DLT645.utils.ByteUtils;
import cc.iotkit.converter.Device;
import cc.iotkit.converter.DeviceMessage;
import cc.iotkit.converter.IConverter;
import cc.iotkit.model.device.message.ThingModelMessage;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@Data
public class DLT645Converter implements IConverter {
    @Override
    public void setScript(String script) {

    }

    /**
     * 编码
     * @param msg
     * @return
     */
    @Override
    public ThingModelMessage decode(DeviceMessage msg) {
        ThingModelMessage tmm = null;
        ReportData rd=JsonUtils.parseObject(JsonUtils.toJsonString(msg.getContent()),ReportData.class);
        if(ThingModelMessage.TYPE_PROPERTY.equals(rd.type)&&"report".equals(rd.getIdentifier())){
            tmm=ThingModelMessage.builder()
                    .mid(msg.getMid())
                    .productKey(msg.getProductKey())
                    .deviceName(msg.getDeviceName())
                    .identifier(rd.getIdentifier())
                    .occurred(rd.getOccur())
                    .time(rd.getTime())
                    .type(rd.getType())
                    .data(rd.getData())
                    .build();
        }
        return tmm;
    }
    /**
     * 解码
     * @param service，device
     * @return
     */
    @Override
    public DeviceMessage encode(ThingService<?> service, Device device) {
        DeviceMessage deviceMsg=new DeviceMessage();
        deviceMsg.setProductKey(service.getProductKey());
        deviceMsg.setDeviceName(service.getDeviceName());
        deviceMsg.setMid(UniqueIdUtil.newRequestId());
        Map<String,String> sd = (Map<String, String>) service.getParams();
        String funCode="";
        if(ThingService.TYPE_SERVICE.equals(service.getType())){//服务相关
            if("readData".equals(service.getIdentifier())){//读数据
                funCode=DLT645FunCode.func_v97_00001;
            }else if("writeData".equals(service.getIdentifier())){//写数据
                funCode=DLT645FunCode.func_v97_00100;
            }
            //...其他功能码
        }
        deviceMsg.setContent(packData(sd.get("deviceAddr"),funCode,sd.get("dataIdentifier")));
        return deviceMsg;
    }

    @Override
    public void putScriptEnv(String key, Object value) {

    }

    private String packData(String deviceAddress,String funCode,String dataIdentifier){
        // 对设备地址进行编码
        byte[] tmp = ByteUtils.hexStringToByteArray(deviceAddress);
        byte[] adrr = new byte[6];
        ByteUtils.byteInvertedOrder(tmp,adrr);

        // 根据对象名获取对象格式信息，这个格式信息，记录在CSV文件中
        DLT645Data dataEntity = DLT645Analysis.inst().getTemplateByDIn().get(dataIdentifier);
        if (dataEntity == null) {
            throw new BizException("CSV模板文件中未定义对象:" + dataIdentifier + " ，你需要在模板中添加该对象信息");
        }
        byte byFun = Byte.decode(String.valueOf(DLT645FunCode.getCodev1997(funCode)));

        // 使用DLT645协议框架编码
        byte[] pack = DLT645Analysis.packCmd(adrr,byFun,dataEntity.getDIn());

        // 将报文按要求的16进制格式的String对象返回
        return ByteUtils.byteArrayToHexString(pack,false);
    }
    @Data
    public static class ReportData{
        private String type;
        private String identifier;
        private Long occur;
        private Long time;
        private Object data;
    }
}
