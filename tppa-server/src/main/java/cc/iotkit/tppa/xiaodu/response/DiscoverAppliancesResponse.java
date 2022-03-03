package cc.iotkit.tppa.xiaodu.response;

import cc.iotkit.tppa.xiaodu.request.Header;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
public class DiscoverAppliancesResponse {

    private Header header;

    private Payload payload;

    @Data
    public static class Payload {
        private List<DiscoveredAppliance> discoveredAppliances;
        /**
         * discoveredGroups 对象的数组，该对象包含可发现分组，与用户设备帐户相关联的。
         * 如果没有与用户帐户关联的分组，此属性应包含一个空数组。
         * 如果发生错误，该属性可以为空数组[]。阵列中允许的最大项目数量为10。
         */
        private List<DiscoveredGroup> discoveredGroups;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DiscoveredAppliance {
        private List<String> actions;
        /**
         * discoveredAppliance.applianceTypes	支持的设备、场景类型
         */
        private String applianceTypes;
        /**
         * 设备标识符。标识符在用户拥有的所有设备上必须是唯一的。此外，标识符需要在同一设备的多个发现请求之间保持一致。
         */
        private String applianceId;
        /**
         * 设备相关的描述，描述内容提需要提及设备厂商，使用场景及连接方式
         */
        private String friendlyDescription;
        /**
         * 用户用来识别设备的名称
         */
        private String friendlyName;
        /**
         * 设备当前是否能够到达。true表示设备当前可以到达，false表示当前设备不能到达。
         */
        private boolean isReachable;
        /**
         * 设备厂商的名字。
         */
        private String manufacturerName;
        /**
         * 设备型号名称，是字符串类型，长度不能超过128个字符。
         */
        private String modelName;
        /**
         * 供应商提供的设备版本。是字符串类型，长度不能超过128个字符。
         */
        private String version;
        /**
         * 设备的属性信息。当设备没有属性信息时，协议中不需要传入该字段。每个设备允许同步的最大的属性数量是10。详细信息请参考设备属性及设备属性上报。
         */
        private List<Attribute> attributes;
        /**
         * 提供给设备云使用，存放设备或场景相关的附加信息，是键值对。DuerOS不解析或使用这些数据
         */
        private List additionalApplianceDetails=new ArrayList();

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Attribute {
        /**
         * 属性名称，支持数字、字母和下划线，长度不能超过128个字符。
         */
        private String name;
        /**
         * 属性值，支持多种json类型。
         */
        private String value;
        /**
         * 属性值的单位名称，支持数字、字母和下划线，长度不能超过128个字符。
         */
        private String scale;
        /**
         * 属性值取样的时间戳，单位是秒。
         */
        private Long timestampOfSample;
        /**
         * 属性值取样的时间误差，单位是ms。如果设备使用的是轮询时间间隔的取样方式，那么uncertaintyInMilliseconds就等于时间间隔。
         * 如温度传感器每1秒取样1次，那么uncertaintyInMilliseconds的值就是1000。
         */
        private Long uncertaintyInMilliseconds;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiscoveredGroup{
        /**
         * 用户用来识别分组的名称
         */
        private String groupName;
        /**
         * 分组所包含设备ID的数组，要求设备ID必须是已经发现的设备中的ID，否则会同步失败
         */
        private List<String> applianceIds;
        /**
         * 分组备注信息
         */
        private String groupNotes;
        /**
         * 提供给技能使用的分组相关的附加信息的键值对
         */
        private List additionalGroupDetails;
    }

}
