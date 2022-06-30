//引用api工具类
var apiTool = Java.type("cc.iotkit.comp.biz.ApiTool");
//api配置
apiTool.config("http://localhost",8086,3000);

this.onReceive=function(method,path,header,params,body){
  //method：post、get、delete...
  //path：请求路径
  //header：http请求头数据,结构：{xx:xx,yy:yy}
  //params：请求参数，结构：{xx:[...],yy:[...]}
  //body：请求体，当提交的数据为json格式时使用，结构：{xx:xx,yy:yy}
  apiTool.log("onReceive method:"+method);
  apiTool.log("onReceive path:"+path);
  apiTool.log("onReceive header:"+header);
  apiTool.log("onReceive params:"+params);
  apiTool.log("onReceive body:"+body);
  var duHeader=body.header;
  var namespace=duHeader.namespace;
  var requestName=duHeader.name;
  var messageId=duHeader.messageId;
  var duPayload=duHeader.payload;
  var token=duHeader.accessToken;

  //设备发现
  if(namespace=="DuerOS.ConnectedHome.Discovery" && requestName=="DiscoverAppliancesRequest"){

  }

  return {
    url:"xx",//不指定直接作为响应返回
    header:{
      contentType:"xx"
    },
    content:"xx"
  }
}