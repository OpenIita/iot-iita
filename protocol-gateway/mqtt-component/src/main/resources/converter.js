new (function () {
    this.decode = function (msg) {
      //对msg进行解析，并返回物模型数据
      var mqttMsg = JSON.parse(msg.content);
      var topic = mqttMsg.topic;
      var payload = mqttMsg.payload;
  
      if (topic.endsWith("/property/post")) {
        //属性上报
        return {
          mid: msg.mid,
          productKey: msg.productKey, //可根据消息内容判断填写不同产品
          deviceName: msg.deviceName,
          type:"property",
          identifier: "report", //属性上报
          occur: new Date().getTime(), //时间戳，设备上的事件或数据产生的本地时间
          time: new Date().getTime(), //时间戳，消息上报时间
          data: payload.params,
        };
      } else if (topic.indexOf("/event/") > 0) {
        var identifier = topic.substring(topic.lastIndexOf("/") + 1);
        //事件上报
        return {
          mid: msg.mid,
          productKey: msg.productKey,
          deviceName: msg.deviceName,
          type:"event",
          identifier: identifier,
          occur: new Date().getTime(),
          time: new Date().getTime(),
          data: payload.params,
        };
      } else if (topic.endsWith("_reply")) {
        var identifier = topic.substring(topic.lastIndexOf("/") + 1);
        //服务回复
        return {
          mid: msg.mid,
          productKey: msg.productKey,
          deviceName: msg.deviceName,
          type:"service",
          identifier: identifier.replace("_reply", "Reply"),
          occur: new Date().getTime(),
          time: new Date().getTime(),
          code: payload.code,
          data: payload.data,
        };
      }
      return null;
    };
  })()
  