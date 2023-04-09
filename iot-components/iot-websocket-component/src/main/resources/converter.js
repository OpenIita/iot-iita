
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
	var entityId=service.deviceName;
	var deviceMid=getMid();
	var params={};
	var target={};
	if("property"==type&&"set"==identifier){
		var domain=entityId.split(".")[0];
		var powerstate=service.params.powerstate==1?"turn_on":"turn_off";
		params.type="call_service";
		params.domain=domain;
		params.service=powerstate;
		target.entity_id=entityId;
		params.target=target;
	}
	return {
		productKey:service.productKey,
		deviceName:service.deviceName,
		mid:deviceMid,
		content:params
	}
};