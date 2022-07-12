var pidPkMap={
  "H5Z31yKBmy":"3ptfx2dRescPAwTn",
  "xOCy76jn6k":"jzC6eQGRse6hDZPB"
}

this.onReceive=function(method,path,header,params,body){
  var type=header["Content-Type"];
  if(type=="application/json"){
	var msg=JSON.parse(body.msg);
	var productId=msg.productId;
	var deviceName=msg.deviceName;
	var messageType=msg.messageType;
	var data=msg.data;
	var pk=pidPkMap[productId];
	if(!pk){
	  return {
		url:"",
		header:{
		  contentType:"application/json"
		},
		content:"error"
	  }
	}

	if(messageType=="lifeCycle"){
	  //登录、登出
	  var online=data.status=="online";
	  deviceBehaviour.deviceStateChange(pk,deviceName,online);
	}else if(messageType=="notify"){
	  //设备消息
	  //消息类型
	  var notifyType=msg.notifyType;
	  if(notifyType=="property"){
		//属性上报
		var propertyData={};
		for(var p in data.params){
		  propertyData[p]=data.params[p].value;
		}
		deviceBehaviour.reportMessage(JSON.stringify({
		  mid:data.id,
		  productKey:pk,
		  deviceName:deviceName,
		  type:"property",
		  identifier:"report",
		  data:propertyData
		}));
	  }else if(notifyType=="event"){
		//事件上报
		var identifier="";
		var paramData={};
		for(var p in data.params){
		  identifier=p;
		  paramData=data.params[p];
		}

		deviceBehaviour.reportMessage(JSON.stringify({
		  mid:data.id,
		  productKey:pk,
		  deviceName:deviceName,
		  type:"event",
		  identifier:identifier,
		  data:paramData.value
		}));
	  }
	}
  }
  return {
	url:"",
	header:{
	  contentType:"application/json"
	},
	content:JSON.stringify(params.msg)
  }
};