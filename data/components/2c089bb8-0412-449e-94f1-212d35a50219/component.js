var mid=1;

var access_token="";

function getMid(){
	mid++;
	if(mid>10000){
		mid=1;
	}
	return mid;
};
function getPingData(data){
	var ping={
		productKey:"",
		deviceName:"",
		content:{
			id:getMid(),
			type:data
		}
	};
	return {
		type:"action",
		data:{
			productKey:"",
			deviceName:"",
			state:""
		},
		action:{
			type:"ack",
			content:JSON.stringify(ping)
		}
	}
};
//必须提供onReceive方法
this.onReceive=function(head,type,payload){
	var data=JSON.parse(payload)
	if(data.type=="auth_required"){
		var auth={
			productKey:"",
			deviceName:"",
			content:{
				type:"auth",
				access_token:access_token
			}
		};
		return {
			type:"action",
			data:{
				productKey:"",
				deviceName:"",
				state:""
			},
			action:{
				type:"ack",
				content:JSON.stringify(auth)
			}
		}
	}else if(data.type=="auth_ok"){
		return getPingData(data.heartBeatData);
	}else if(data.type=="pong"){
		apiTool.log("receive pong!");
	}else if("ping"==type){
		return getPingData(data.heartBeatData);
	}
	return {
		productKey:"",
		deviceName:"",
		mid:0,
		content:{
		}
	}
};