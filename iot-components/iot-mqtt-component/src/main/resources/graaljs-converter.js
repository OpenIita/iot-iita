
var mid=1;

function getMid(){
	mid++;
	if(mid>10000){
		mid=1;
	}
	return mid+"";
}

this.decode = function (msg) {

	//对msg进行解析，并返回物模型数据
	console.log("msg", msg);
	var content= msg.getContent();
	console.log("content",content);
	var topic = content.topic;
	console.log("topic",topic);
	var payload = content.get("payload");
	console.log("payload",payload);
	var identifier = topic.substring(topic.lastIndexOf("/") + 1);

	//透传上报
	if(topic.endsWith("/event/rawReport")){
		var rst= component.transparentDecode(payload.params);
		if(!rst){
			return null;
		}
		rst.occured=new Date().getTime();
		rst.time=new Date().getTime();
		return rst;
	}

	if (topic.endsWith("/property/post")) {
		//属性上报
		return {
			"mid": msg.getMid(),
			"productKey": msg.getProductKey(),
			"deviceName": msg.getDeviceName(),
			"type":"property",
			"identifier": "report", //属性上报
			"occured": new Date().getTime(), //时间戳，设备上的事件或数据产生的本地时间
			"time": new Date().getTime(), //时间戳，消息上报时间
			data: payload,
		};
	} else if (topic.indexOf("/event/") > 0) {
		//事件上报
		return {
			mid: msg.getMid(),
			productKey: msg.getProductKey(),
			deviceName: msg.getDeviceName(),
			type:"event",
			identifier: identifier,
			occured: new Date().getTime(),
			time: new Date().getTime(),
			data: payload.params,
		};
	}else if(topic.endsWith("/service/property/set_reply")){
		//属性设置回复
		return {
			mid: msg.getMid(),
			productKey: msg.getProductKey(),
			deviceName: msg.getDeviceName(),
			type:"property",
			identifier: identifier,
			occured: new Date().getTime(),
			time: new Date().getTime(),
			code: payload.code
		};
	}else if(topic.endsWith("/config/set_reply")){
		//设备配置设置回复
		return {
			mid: msg.getMid(),
			productKey: msg.getProductKey(),
			deviceName: msg.getDeviceName(),
			type:"config",
			identifier: "set_reply",
			occured: new Date().getTime(),
			time: new Date().getTime(),
			code: payload.code
		};
	}else if(topic.endsWith("/config/get")){
		//设备配置获取
		return {
			mid: msg.getMid(),
			productKey: msg.getProductKey(),
			deviceName: msg.getDeviceName(),
			type:"config",
			identifier: "get",
			occured: new Date().getTime(),
			time: new Date().getTime(),
			data: {},
		};
	} else if (topic.endsWith("_reply")) {
		//服务回复
		return {
			mid: msg.getMid(),
			productKey: msg.getProductKey(),
			deviceName: msg.getDeviceName(),
			type:"service",
			identifier: identifier,
			occured: new Date().getTime(),
			time: new Date().getTime(),
			code: payload.code,
			data: payload.data,
		};
	}
	return null;
};

this.encode = function (service,device) {
	var deviceMid=getMid();
	var method="thing.service.";
	var topic="/sys/"+service.getProductKey()+"/"+service.getDeviceName()+"/c/service/";
	var params={};

	//透传下发
	if(device.isTransparent()){
		var rst=component.transparentEncode(service,device);
		topic="/sys/"+rst.productKey+"/"+rst.deviceName+"/c/service/rawSend";
		params.model=rst.content.model;
		params.deviceName=rst.content.deviceName;
		params.data=rst.content.data;

		return {
			productKey:rst.productKey,
			deviceName:rst.deviceName,
			mid:rst.mid,
			content:{
				topic:topic,
				payload:JSON.stringify({
					id:rst.mid,
					method:method+"rawSend",
					params:params
				})
			}
		}

	}

	var type=service.getType();
	var identifier=service.getIdentifier();

	if(type=="property"){
		method+="property."+identifier;
		topic+="property/"+identifier;
	}else if(type=="service"){
		method+=identifier;
		topic+=identifier;
	}else if(type=="config"){
		//设备配置下发
		method+=identifier;
		topic="/sys/"+service.getProductKey()+"/"+service.getDeviceName()+"/c/config/"+identifier;
	}else if(type="lifetime"){
		//子设备注销下发
		method+=identifier;
		topic="/sys/"+service.getProductKey()+"/"+service.getDeviceName()+"/c/deregister";
	}

	for(var p in service.getParams()){
		params[p]=service.getParams()[p];
	}

	return {
		productKey:service.getProductKey(),
		deviceName:service.getDeviceName(),
		mid:deviceMid,
		content:{
			topic:topic,
			payload:JSON.stringify({
				id:deviceMid,
				method:method,
				params:params
			})
		}
	}
};