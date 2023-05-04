var mid=1;

var gatewayPk="BRD3x4fkKxkaxXFt"
var smartMeterPk="PwMfpXmp4ZWkGahn"

function getMid(){
	mid++;
	if(mid>10000){
		mid=1;
	}
	return mid;
};
function register(head){
	var mac= head.mac;
	return {
		type:"register",
		data:{
			productKey:gatewayPk,
			deviceName:mac,
			model:""
		}
	};
}

function deviceStateChange(head,type){
	var mac=head.mac;
	return {
		type:"state",
		data:{
			productKey:gatewayPk,
			deviceName:mac,
			state:type
		}
	}
}

function dltHandle(payload){
	var dltData= JSON.parse(payload);
	var identify= dltData.identify;
	var content={};
	content[identify]=dltData.data;
	return {
		type:"report",
		data:{
			productKey:smartMeterPk,
			deviceName:dltData.deviceAddress,
			mid:getMid(),
			content:{
				type:"property",
				identifier: "report", //属性上报
				occur: new Date().getTime(), //时间戳，设备上的事件或数据产生的本地时间
				time: new Date().getTime(), //时间戳，消息上报时间
				data: content
			}
		},
	}
}

//必须提供onReceive方法
this.onReceive=function(head,type,payload){
	if("register"==type){
		return register(head);
	}else if("online"==type){
		return deviceStateChange(head,type);
	}else if("offline"==type){
		return deviceStateChange(head,type);
	}else if("dlt"==type){
		return dltHandle(payload);
	}
};

this.onRegistered=function (data,status) {
	apiTool.log("onRegistered调用");
}