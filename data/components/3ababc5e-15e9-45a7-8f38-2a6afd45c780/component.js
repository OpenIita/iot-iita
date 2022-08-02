//api配置
apiTool.config("127.0.0.1",8085,3000);

this.onReceive=function(method,path,header,params,body){
  //method：post、get、delete...
  //path：请求路径
  //header：http请求头数据,结构：{xx:xx,yy:yy}
  //params：请求参数，结构：{xx:[...],yy:[...]}
  //body：请求体，当提交的数据为json格式时使用，结构：{xx:xx,yy:yy}
  apiTool.log("onReceive body:"+body);
  var duHeader=body.header;
  var namespace=duHeader.namespace;
  var requestName=duHeader.name;
  var messageId=duHeader.messageId;
  var duPayload=body.payload;
  var token=duPayload.accessToken;
  var openUid=duPayload.openUid;
  
  //设备发现
  if(namespace=="DuerOS.ConnectedHome.Discovery" && requestName=="DiscoverAppliancesRequest"){
	var deviceIds=[];
	var discoveredDevices=[];
	var content={
	  header:{
		namespace:"DuerOS.ConnectedHome.Discovery",
		name:"DiscoverAppliancesResponse",
		messageId:messageId,
		payloadVersion:1
	  },
	  payload:{
		discoveredAppliances:discoveredDevices,
		discoveredGroups:[{
		  groupName:"客厅",
		  applianceIds:deviceIds,
		  groupNotes:"客厅分组控制",
		  additionalGroupDetails:{}
		}]
	  }
	};
    var rst=apiTool.getSpaceDevices(token);
	apiTool.log(JSON.stringify(rst));
	if(rst && rst.status==200 && rst.data){
	  var devices=rst.data;
	  for(var i in devices){
		var device=devices[i];
		var did=device.deviceId;
		var pk=device.productKey;
		var dn=device.deviceName;
		
		//更新设备openUid
		rst=apiTool.setOpenUid(token,did,"dueros",openUid);
		if(!rst || rst.status!=200){
		  continue;
		}
		
		//插座
		if(pk=="cGCrkK7Ex4FESAwe"){
		  var powerstate=device.property.powerstate;
		  discoveredDevices.push({
			actions:["turnOn","turnOff"],
			applianceTypes:["SOCKET"],
			additionalApplianceDetails:{},
			applianceId:device.deviceId,
			friendlyDescription:"智能插座",
			friendlyName:device.name,
			isReachable:device.online,
			manufacturerName:"海曼",
			modelName:"S1",
			version:"v1.0",
			attributes:[
			  {
				name:"客厅的插座",
				scale:"",
				timestampOfSample:0,
				uncertaintyInMilliseconds:10
			  },
			  {
				name:"connectivity",
				value:"REACHABLE",
				scale:"",
				timestampOfSample:0,
				uncertaintyInMilliseconds:10
			  },
			  {
				name:"turnOnState",
				value:powerstate==1?"ON":"OFF",
				scale:"",
				timestampOfSample:0,
				uncertaintyInMilliseconds:10,
				legalValue:"(ON, OFF)"
			  }
			]
		  });
		}else if(pk=="Rf4QSjbm65X45753"){
		  //开关
		  var powerstate=device.property.powerstate;
		  discoveredDevices.push({
			actions:["turnOn","turnOff"],
			applianceTypes:["SWITCH"],
			additionalApplianceDetails:{},
			applianceId:device.deviceId,
			friendlyDescription:"智能开关",
			friendlyName:device.name,
			isReachable:device.online,
			manufacturerName:"海曼",
			modelName:"S1",
			version:"v1.0",
			attributes:[
			  {
				name:"客厅的开关",
				scale:"",
				timestampOfSample:0,
				uncertaintyInMilliseconds:10
			  },
			  {
				name:"connectivity",
				value:"REACHABLE",
				scale:"",
				timestampOfSample:0,
				uncertaintyInMilliseconds:10
			  },
			  {
				name:"turnOnState",
				value:powerstate==1?"ON":"OFF",
				scale:"",
				timestampOfSample:0,
				uncertaintyInMilliseconds:10,
				legalValue:"(ON, OFF)"
			  }
			]
		  });
		  
		}
	  }
	}
	
	return {
	  url:"",//不指定直接作为响应返回
	  header:{
		contentType:"application/json"
	  },
	  content:JSON.stringify(content)
	}
  }else if(namespace=="DuerOS.ConnectedHome.Control"){
	//设备控制
  	var appliance=duPayload.appliance;
	var deviceId=appliance.applianceId;
	var confirmName="UnsupportedOperationError";
	var rst={status:500};
	
	//开关
	if(requestName=="TurnOnRequest"){
		//开
		confirmName="TurnOnConfirmation";
		rst=apiTool.setProperties(token,deviceId,{powerstate:1});
	}else if(requestName=="TurnOffRequest"){
		//关
	  	confirmName="TurnOffConfirmation";
		rst=apiTool.setProperties(token,deviceId,{powerstate:0});
	}
	
	if(rst.status!=200){
	  confirmName="UnsupportedOperationError";
	  apiTool.log("device control failed:"+JSON.stringify(rst));
	}
	
	var content={
	  header: {
		namespace: "DuerOS.ConnectedHome.Control",
		name: confirmName,
		messageId: messageId,
		payloadVersion: "1"
	  },
	  payload: {
		"attributes": []
	  }
	};
	
	return {
	  url:"",
	  header:{
		contentType:"application/json"
	  },
	  content:JSON.stringify(content)
	}
  }else if(namespace=="DuerOS.ConnectedHome.Query"){
	//属性查询
	if(requestName=="ReportStateRequest"){
	  var appliance=duPayload.appliance;
	  var deviceId=appliance.applianceId;
	  var property=appliance.attributeName;
	  var propertyVal="";
	  var success=false;
	  if(property=="turnOnState"){
		//开关状态查询
		var rst= apiTool.getSpaceDeviceDetail(token,deviceId);
		if(rst && rst.status==200 && rst.data.property){
		  propertyVal=rst.data.property.powerstate==1?"ON":"OFF";
		  success=true;
		}
	  }
	  var content=success?{
		"header": {
		  "namespace": "DuerOS.ConnectedHome.Query",
		  "name": "ReportStateResponse",
		  "messageId": new Date().getTime()+"",
		  "payloadVersion": "1"
		},
		"payload": {
		  "attributes": [
			{
			  "name": property,
			  "value": propertyVal,
			  "scale": "",
			  "timestampOfSample": new Date().getTime()/1000,
			  "uncertaintyInMilliseconds": 0
			}
		  ]
		}
	  }:{};
	  
	  return {
		url:"",
		header:{
		  contentType:"application/json"
		},
		content:JSON.stringify(content)
	  }
	}
  }
  return {
	  url:"",//不指定直接作为响应返回
	  header:{
		contentType:"application/json"
	  },
	  content:""
	}
}