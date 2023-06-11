package cc.iotkit.baetyl.constant;

/***
 *  接口文档地址-https://baetyl.io/docs/cn/latest/_static/api.html
 */
public class BaetylConstant {


    public class Url {

        /**
         * 节点管理
         */
        public class NodeManagement {
            /**
             * 查询节点关联的应用 GET
             */
            public static final String GetNodeAppsByName = "/v1/nodes/{name}/apps";
            /**
             * 修改 core 配置（core 自升级） PUT
             */
            public static final String UpdateCoreConfigByName = "/v1/nodes/{name}/core/configs";
            /**
             * 创建节点 POST
             */
            public static final String CreatNode = "/v1/nodes";
            /**
             * 删除节点 DELETE
             */
            public static final String DeleteNodeByName = "/v1/nodes/{name}";
            /**
             * 批量查询节点 PUT
             */
            public static final String GetNodesBatch = "/v1/nodes?batch";
            /**
             * 更新节点属性 PUT
             */
            public static final String UpdateNodeProperties = "/v1/nodes/{name}/properties";
            /**
             * 查询节点 GET
             */
            public static final String GetNodeByName = "/v1/nodes/{name}";
            /**
             * 修改节点 PUT
             */
            public static final String UpdateNode = "/v1/nodes/{name}";
            /**
             * 查询节点状态信息 GET
             */
            public static final String GetNodeStats = "/v1/nodes/{name}/stats";
            /**
             * 罗列当前节点 core 版本号 GET
             */
            public static final String GetNodeCoreVersion = "/v1/nodes/{name}/core/versions";
            /**
             * 罗列节点 GET
             */
            public static final String GetNodes = "/v1/nodes";
            /**
             * 获取 core 配置 GET
             */
            public static final String GetCoreConfig = "/v1/nodes/{name}/core/configs";
            /**
             * 获取安装命令 GET
             */
            public static final String GetInstallCommand = "/v1/nodes/:name/init";
            /**
             * 获取节点属性 GET
             */
            public static final String GetNodeProperties = "/v1/nodes/{name}/properties";

        }





    }



}
