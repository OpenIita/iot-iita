
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
	var content=msg.content;
	var type = content.type;

	if (type=="report") {
		//属性上报
		return {
			mid: msg.mid,
			productKey: msg.productKey,
			deviceName: msg.deviceName,
			type:"property",
			identifier: "report", //属性上报
			occur: new Date().getTime(), //时间戳，设备上的事件或数据产生的本地时间
			time: new Date().getTime(), //时间戳，消息上报时间
			data: content.params,
		};
	}
	return null;
};

this.encode = function (service,device) {
	var type=service.type;
	var identifier=service.identifier;
	var deviceMid=getMid();
	var params={};
	if("property"==type&&"set"==identifier){
	   params.id=deviceMid;
		params.type="control";
	   params.device_name=service.deviceName;
		params.data=service.params
	}
	return {
		productKey:service.productKey,
		deviceName:service.deviceName,
		mid:deviceMid,
		content:params
	}
};