var mid=1;

function getMid(){
	mid++;
	if(mid>10000){
		mid=1;
	}
	return mid;
};
function getPkDn(deviceKey){
	var arr=deviceKey.split("_");
	return {
		pk:arr[1],
		dn:deviceKey
	};
}
function register(data){
	var device=getPkDn(data.data.deviceName)
	var subDevicesList=data.data.subDevices
	var subDevices=[]
	if(subDevicesList!=undefined&&subDevicesList.length>0){
		apiTool.log("device:"+subDevicesList);
		for (var i = 0; i < subDevicesList.length; i++) {
			var deviceKey=subDevicesList[i]
			var subDevice=getPkDn(deviceKey)
			subDevices.push({
				productKey:subDevice.pk,
				deviceName:subDevice.dn,
				model:''
			})
		}
	}

	var reply=
		{
			productKey:device.pk,
			deviceName:device.dn,
			mid:"0",
			content:{
				id:data.id,
				type:data.type,
				result:'success'
			}
		};
	var data={
		productKey:device.pk,
		deviceName:device.dn
	}
	if(subDevices.length>0){
		data['subDevices']=subDevices
	}
	apiTool.log("subDevices:"+JSON.stringify(data));
	return {
		type:"register",
		data:data,
		action:{
			type:"ack",
			content:JSON.stringify(reply)
		}
	};
}

function online(data){
	var device=getPkDn(data.data.deviceName)
	return {
		type:"state",
		data:{
			productKey:device.pk,
			deviceName:device.dn,
			state:data.type
		}
	};
}

function offline(data){
	var device=getPkDn(data.deviceKey)
	return {
		type:"state",
		data:{
			productKey:device.pk,
			deviceName:device.dn,
			state:data.type
		}
	};
}

//必须提供onReceive方法
this.onReceive=function(head,type,payload){
  apiTool.log("payload:"+payload);
	var data=JSON.parse(payload)
	if(data.type=="register"){
		apiTool.log("data:"+payload);
		return register(data)
	}else if(data.type=="online"){
		return online(data);
	}else if(data.type=="offline"){
		return offline(data);
	}
var device=getPkDn(data.device_name)
var reply=
		{
			productKey:device.pk,
			deviceName:device.dn,
			mid:"0",
			content:{
				id:data.id,
				type:data.type,
				result:'success'
			}
		};
	return {
	type:"report",
	data:{
	  productKey:device.pk,
	  deviceName:device.dn,
	  mid:getMid(),
	  content:{
		type:"report",
		params:data.data
	  }
	},
	action:{
			type:"ack",
			content:JSON.stringify(reply)
		}
  }
};

this.onRegistered=function (data,status) {
	apiTool.log("onRegistered调用");
}