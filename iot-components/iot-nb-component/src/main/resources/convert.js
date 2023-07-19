var mid = 1;
var COMMAD_UNKOWN = 0xff;    //未知的命令
function getMid() {
    mid++;
    if (mid > 10000) {
        mid = 1;
    }
    return mid + "";
}
//上行数据
this.decode = function (msg) {
    var content = msg.content;
    var topic = content.topic;
    var bytes = arrayGroup(content.payload.params,2);
    var byteData=content.payload.params;
    if (topic.endsWith("/thing/model/up_raw")) {
        var data = arrayGroup(byteData, 2);
        var params = {};
        var uint8Array = new Uint8Array(bytes.length);
        for (var i = 0; i < bytes.length; i++) {
            uint8Array[i] = bytes[i] & 0xff;
        }
        if (data[1] == '03') {
            if (byteData.length == 22 ) {
                params['Switch'] = Number(parseInt(data[3] + data[4], 16))
                params['HandMode'] = Number(parseInt(data[5] + data[6], 16))+1
                params['TempGear'] = Number(data[7].slice(1,2))+1
                params['RatioGear'] = Number(data[7].slice(0,1))+1
                params['PressureGear'] = data[8].slice(0,1) != 'f' ?  Number(data[8].slice(0,1))+1 : 0
                params['SpeedGear'] = data[8].slice(1,2) != 'f' ?  Number(data[8].slice(1,2))+1 : 0

            } else if (byteData.length == 126) {
                params['Pressure'] = Number((parseInt(data[3] + data[4], 16) / 100).toFixed(1))
                params['EmtyRunPressure'] = Number((parseInt(data[5] + data[6], 16) / 100).toFixed(1))
                params['StartPressure'] = Number((parseInt(data[7] + data[8], 16) / 100).toFixed(1))
                params['WorkMode'] = Number((parseInt(data[9] + data[10], 16)).toFixed(0))
                params['ClearIceEn'] = Number((parseInt(data[11] + data[12], 16)).toFixed(0))
                params['WaterT'] = Number((parseInt(data[13] + data[14], 16)).toFixed(0))
                params['WaterTReset'] = Number((parseInt(data[15] + data[16], 16)).toFixed(0))
                params['SensorMode'] = Number((parseInt(data[17] + data[18], 16)).toFixed(0))
                params['SensorGroup'] = Number((parseInt(data[19] + data[20], 16)).toFixed(0))
                params['ElectronicTMax'] = Number(parseInt(data[59], 16))
                params['ElectronicTMaxReset'] = Number(parseInt(data[60], 16))
            }  else if (byteData.length == 26) {
                params['ActiveTime'] =  String(parseInt(data[3], 16))+'-'+String(parseInt(data[4], 16))+'-'+String(parseInt(data[5], 16))
            }	else if (byteData.length == 190) {
                params['Pressure'] = Number((parseInt(data[3] + data[4], 16) / 100).toFixed(1))
                params['EmtyRunPressure'] = Number((parseInt(data[5] + data[6], 16) / 100).toFixed(1))
                params['StartPressure'] = Number((parseInt(data[7] + data[8], 16) / 100).toFixed(1))
                params['WorkMode'] = Number((parseInt(data[9] + data[10], 16)).toFixed(0))
                params['ClearIceEn'] = Number((parseInt(data[11] + data[12], 16)).toFixed(0))
                params['WaterT'] = Number((parseInt(data[13] + data[14], 16)).toFixed(0))
                params['WaterTReset'] = Number((parseInt(data[15] + data[16], 16)).toFixed(0))
                params['SensorMode'] = Number((parseInt(data[17] + data[18], 16)).toFixed(0))
                params['SensorGroup'] = Number((parseInt(data[19] + data[20], 16)).toFixed(0))
                params['ElectronicTMax'] = Number(parseInt(data[59], 16))
                params['ElectronicTMaxReset'] = Number(parseInt(data[60], 16))
                params['SceneMode'] = Number((parseInt(data[63] + data[64], 16)).toFixed(0))
                params['EnergyModeTime'] = Number((parseInt(data[65] + data[66], 16)).toFixed(0))
                params['WaterTime'] = String(Number((parseInt(data[68].substring(0, 1), 16)).toFixed(0))) + ',' + String(Number((parseInt(data[68].substring(1), 16)).toFixed(0)))
                params['HotWaterTime'] = Number((parseInt(data[69] + data[70], 16)).toFixed(0))
                params['TempSet'] = String(Number((parseInt(data[71], 16)).toFixed(0)))+','+ String(Number((parseInt(data[72], 16)).toFixed(0)))
                params['TimeModeSet'] = parseInt(data[73] + data[74], 16)+'-'+parseInt(data[75] + data[76], 16)+','+ parseInt(data[77] + data[78], 16)+'-'+parseInt(data[79] + data[80], 16)+','+ parseInt(data[81] + data[82], 16)+'-'+parseInt(data[83] + data[84], 16)+','+ parseInt(data[85] + data[86], 16)+'-'+parseInt(data[87] + data[88], 16)+','+ parseInt(data[89] + data[90], 16)+'-'+parseInt(data[91] + data[92], 16)

            } else if (byteData.length == 94) {
                let timeStamp = parseInt(String(data[41])+String(data[42])+String(data[43])+String(data[44]),16)
                let time = timestampToTime(timeStamp)
                params['ActiveTime'] =  String(parseInt(data[3], 16))+'-'+String(parseInt(data[4], 16))+'-'+String(parseInt(data[5], 16))
                params['Time'] =  time + '-'+ timeStamp

            }
        } else if (data[1] == '04') {
            if (byteData.length == 54) {
                params['ErrorMsg'] = Number(parseInt(data[3]+data[4], 16))
                params['Voltage'] = Number(parseInt(data[5]+data[6], 16))
                params['Electric'] = Number(parseInt(data[7]+data[8], 16))/10
                params['Power'] = Number(parseInt(data[9]+data[10], 16))
                params['Speed'] = Number(parseInt(data[11]+data[12], 16))
                params['CurrentPressure1'] = data[13] != 'ff' ? Number((parseInt(data[13]+data[14], 16)/100).toFixed(1)) : 0
                params['CurrentPressure2'] = data[15] != 'ff' ? Number((parseInt(data[15]+data[16], 16)/100).toFixed(1)) : 0
                params['IpmTemperature'] = Number(parseInt(data[17]+data[18], 16))-55
                params['MotorTemperature'] = Number(parseInt(data[19]+data[20], 16))-55
                params['WaterTemperature'] = Number(parseInt(data[21]+data[22], 16))-55
                let warnArray = reverseStr(hex2bin(data[23] + data[24]))
                let warnInfo = ""
                for (let i = 0; i < warnArray.length; i++) {
                    if (warnArray[i] === "1") {
                        if (warnInfo === "") {
                            warnInfo = DEVICE_ERROR[i]
                        } else {
                            warnInfo += `、${DEVICE_ERROR[i]}`
                        }
                    }
                }

                params['WarnInfo'] = warnInfo

            } else if (byteData.length == 18) {
                if (String(byteData) == '130404000800004847') {
                    params['query'] = String(byteData)
                } else if (String(byteData).slice(9,10) == 3) {
                    params['query'] = String(byteData)
                } else if (String(byteData).slice(9,10) == 4) {
                    params['query'] = String(byteData)
                }
            } else if (byteData.length == 30) {
                params['McuVersion'] = String(Number(parseInt(data[3].slice(1,2), 16)))+'.'+String(Number(parseInt(data[4].slice(0,1), 16)))+'.'+String(Number(parseInt(data[4].slice(1,2), 16)))
                params['McuStatus'] = Number(parseInt(data[3].slice(0,1), 16))
                params['Scene'] = Number(parseInt(data[9]+data[10], 16))
                params['Model'] = String(Number(parseInt(data[11]+data[12], 16)))+'W'

            } else if (byteData.length == 66) {
                params['ErrorMsg'] = Number(parseInt(data[3]+data[4], 16))
                params['Voltage'] = Number(parseInt(data[5]+data[6], 16))
                params['Electric'] = Number(parseInt(data[7]+data[8], 16))/10
                params['Power'] = Number(parseInt(data[9]+data[10], 16))
                params['Speed'] = Number(parseInt(data[11]+data[12], 16))
                params['CurrentPressure1'] = data[13] != 'ff' ? Number((parseInt(data[13]+data[14], 16)/100).toFixed(1)) : 0
                params['CurrentPressure2'] = data[15] != 'ff' ? Number((parseInt(data[15]+data[16], 16)/100).toFixed(1)) : 0
                params['IpmTemperature'] = Number(parseInt(data[17]+data[18], 16))-55
                params['MotorTemperature'] = Number(parseInt(data[19]+data[20], 16))-55
                params['WaterTemperature'] = Number(parseInt(data[21]+data[22], 16))-55
                let warnArray = reverseStr(hex2bin(data[23] + data[24]))
                let warnInfo = ""
                for (let i = 0; i < warnArray.length; i++) {
                    if (warnArray[i] === "1") {
                        if (warnInfo === "") {
                            warnInfo = DEVICE_ERROR[i]
                        } else {
                            warnInfo += `、${DEVICE_ERROR[i]}`
                        }
                    }
                }
                params['CountDown'] = Number(parseInt(data[25]+data[26], 16))
                params['WarnInfo'] = warnInfo
                params['Enabled'] = Number(parseInt(data[29]+data[30], 16))

            }
        }
        params['History'] = byteData
        //属性上报
        return {
            mid: msg.mid,
            productKey: msg.productKey,
            deviceName: msg.deviceName,
            type: "property",
            identifier: "report", //属性上报
            occur: new Date().getTime(), //时间戳，设备上的事件或数据产生的本地时间
            time: new Date().getTime(), //时间戳，消息上报时间
            data: params,
        };
    }  else if (topic.indexOf("/event/") > 0) {
        //事件上报
    } else if (topic.endsWith("/service/property/set_reply")) {
        //属性设置回复
    } else if (topic.endsWith("/config/set_reply")) {
        //设备配置设置回复
    } else if (topic.endsWith("/config/get")) {
        //设备配置获取
    } else if (topic.endsWith("_reply")) {
        //服务回复
    }
    return null;
};

//下行数据
this.encode = function (service, device) {
    var deviceMid = getMid();
    var method = "thing.service.";
    var topic = "/sys/" + service.productKey + "/" + service.deviceName + "/thing/model/down_raw";
    var params = {};
    var payloadArray = [];
    var deviceArray = [];;
    var totalArray =[];
    var totalItemArray = [];
    var itemArray = [];
    var outFFIndex = ''
    var type = service.type;
    var identifier = service.identifier;
    if (type == "property" && identifier == "get") {
        var listParams = []
        for (var p in service.params) {
            listParams.push(service.params[p]);
        }
        return {
            productKey: service.productKey,
            deviceName: service.deviceName,
            mid: deviceMid,
            content: {
                topic: topic,
                payload: JSON.stringify({
                    id: deviceMid,
                    method: method += "property." + identifier,
                    params: listParams
                })
            }
        }
    } else if (type == "property" && identifier == "set") {
        for (var p in service.params) {
            params[p] = service.params[p];
        }
        var paramsArr = Object.keys(params)
        //站地址
        if (paramsArr.includes('query')) {
            let queryHexData = arrayGroup(params['query'],2)
            let queryData = []
            queryHexData.map(function(value) {
                queryData.push(parseInt(value,16))
            })
            payloadArray = queryData
        } else {
            const stationAddress = "49";
            totalArray.push(stationAddress)
            //功能码
            var functionCode = "";
            // 寄存器数量
            var register = ''
            var baseVal = ''
            //起始地址
            var startAddressCode = '';
            if (paramsArr.length == 1) {
                functionCode = '06'
                totalArray.push(functionCode)
            } else {
                functionCode = '10'
                paramsArr = ['Pressure', 'EmtyRunPressure', 'StartPressure', 'WorkMode', 'ClearIceEn', 'WaterT', 'WaterTReset']
                totalArray.push(parseInt(Number(functionCode),16))
            }
            if (paramsArr.includes('Switch')) {
                startAddressCode = 300
                totalArray.push('01','44')
            } else if (paramsArr.includes('Pressure')) {
                startAddressCode = 400
                totalArray.push('01','144')
                register = '00' + add0(String(paramsArr.length),2) + add0(String((paramsArr.length*2).toString(16)),2)
                totalArray.push('00', add0(paramsArr.length,2), add0(String((paramsArr.length)*2),2))
            }
            if (functionCode == 6) {
                paramsArr.forEach(function(value){
                    totalArray.push('00',params[value])
                    itemArray = pad(params[value].toString(16),4)
                    deviceArray.push(itemArray[0],itemArray[1])
                })
            } else if (functionCode == 10) {
                paramsArr.forEach(function(value, index){
                    if (index < 3) {
                        //   if (params[value]*100 < 256) {
                        //       totalItemArray = ['00', String((params[value]*100).toFixed(0))]
                        //   itemArray = pad(String((params[value]*100).toString(16)),4)
                        //   totalItemArray = pad(String((params[value]*100).toFixed(0)),4)
                        //   } else {
                        //       outFFIndex = ((Number(params[value]*100))/256).toFixed(0)
                        //       totalItemArray = [outFFIndex, params[value]*100-outFFIndex*256]
                        //   }

                        itemArray = pad(Number((params[value]*100).toFixed(0)).toString(16),4)
                        totalItemArray = pad(String((params[value]*100).toFixed(0)),4)
                    } else {
                        itemArray = pad(String(params[value].toString(16)),4)
                        totalItemArray = pad(String(params[value]),4)
                    }
                    // totalArray.push(params[value])
                    //   totalArray.push(totalItemArray[0],totalItemArray[1])
                    totalArray.push(parseInt(itemArray[0],16),parseInt(itemArray[1],16))
                    //   deviceArray.push(parseInt(itemArray[0],16),parseInt(itemArray[1],16))
                    deviceArray.push(itemArray[0],itemArray[1])

                })
            }

            var startAddressHex = pad(startAddressCode.toString(16), 4);
            baseVal = String(stationAddress) + String(functionCode) + String(startAddressHex[0]) + String(startAddressHex[1])
            if (functionCode != 6) {
                baseVal += register
            }
            deviceArray.forEach(function(item) {
                baseVal += item
            })
            // baseVal += '00070e0064000a00640001000100370037'
            var Crc = CRC.ToModbusCRC16(baseVal)
            var crcArray = arrayGroup(Crc,2)
            totalArray.push(parseInt(crcArray[0],16),parseInt(crcArray[1],16))
            payloadArray = totalArray;
        }
        // var params = json['params'];
        // var prop_float = params['prop_float'];
        // var prop_int16 = params['prop_int16'];
        // var prop_bool = params['prop_bool'];
        // //按照自定义协议格式拼接 rawData。
        // payloadArray = payloadArray.concat(buffer_uint8(COMMAND_SET)); //command字段。
        // payloadArray = payloadArray.concat(buffer_int32(parseInt(id))); //ALink JSON格式 'id'。
        // payloadArray = payloadArray.concat(buffer_int16(prop_int16)); //属性'prop_int16'的值。
        // payloadArray = payloadArray.concat(buffer_uint8(prop_bool)); //属性'prop_bool'的值。
        // payloadArray = payloadArray.concat(buffer_float32(prop_float)); //属性'prop_float'的值。

    }else if (method ==  'thing.event.property.post') { //设备上报数据返回结果,如果不需要回复,可以去除该内容
        // var code = json['code'];
        // payloadArray = payloadArray.concat(buffer_uint8(COMMAND_REPORT_REPLY)); //command字段
        // payloadArray = payloadArray.concat(buffer_int32(parseInt(id))); // ALink JSON格式 'id'
        // payloadArray = payloadArray.concat(buffer_uint8(code));
    } else { //未知命令，对于有些命令不做处理
        var code = "FF";
        payloadArray = payloadArray.concat(buffer_uint8(COMMAD_UNKOWN)); //command字段
        payloadArray = payloadArray.concat(buffer_int32(parseInt(id))); // ALink JSON格式 'id'
        payloadArray = payloadArray.concat(buffer_uint8(code));
    }
    return {
        productKey: service.productKey,
        deviceName: service.deviceName,
        mid: deviceMid,
        content: {
            topic: topic,
            payload: JSON.stringify({
                id: deviceMid,
                method: method += "property." + identifier,
                params: ab2hex(payloadArray).toUpperCase()
            })
        }
    }
};


const DEVICE_ERROR = [
    "压力传感器故障", //0
    "高温限功率", //1
    "渗漏", // 2
    "电机温度传感器故障", //3
    "水温传感器故障", //4
    "保留", //5
    "保留", //6
    "保留", //7
    "保留", //8
    "保留", //9
    "保留", //10
    "保留", //11
    "保留", //12
    "保留", //13
    "保留", //14
    "保留", // 15
    "保留", //16
]
function buffer_uint8(value) {
    var uint8Array = new Uint8Array(1);
    var dv = new DataView(uint8Array.buffer, 0);
    dv.setUint8(0, value);
    return [].slice.call(uint8Array);
}
function buffer_int16(value) {
    var uint8Array = new Uint8Array(2);
    var dv = new DataView(uint8Array.buffer, 0);
    dv.setInt16(0, value);
    return [].slice.call(uint8Array);
}
function buffer_int32(value) {
    var uint8Array = new Uint8Array(4);
    var dv = new DataView(uint8Array.buffer, 0);
    dv.setInt32(0, value);
    return [].slice.call(uint8Array);
}
function buffer_float32(value) {
    var uint8Array = new Uint8Array(4);
    var dv = new DataView(uint8Array.buffer, 0);
    dv.setFloat32(0, value);
    return [].slice.call(uint8Array);
}

function ab2hex(buffer) {
    var hexArr = Array.prototype.map.call(
        new Uint8Array(buffer),
        function (bit) {
            return ('00' + bit.toString(16)).slice(-2)
        }
    )
    return hexArr.join('');
}

function arrayGroup(ss, step) {
    var r = [];

    function doGroup(s) {
        if (!s) return;
        r.push(s.substr(0, step));
        s = s.substr(step);
        doGroup(s);
    }
    doGroup(ss);
    return r;
}

var CRC = {};

CRC._auchCRCHi = [
    0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0,
    0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
    0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0,
    0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40,
    0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1,
    0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41,
    0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1,
    0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
    0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0,
    0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40,
    0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1,
    0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40,
    0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0,
    0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40,
    0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0,
    0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40,
    0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0,
    0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
    0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0,
    0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
    0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0,
    0x80, 0x41, 0x00, 0xC1, 0x81, 0x40, 0x00, 0xC1, 0x81, 0x40,
    0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0, 0x80, 0x41, 0x00, 0xC1,
    0x81, 0x40, 0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41,
    0x00, 0xC1, 0x81, 0x40, 0x01, 0xC0, 0x80, 0x41, 0x01, 0xC0,
    0x80, 0x41, 0x00, 0xC1, 0x81, 0x40
];
CRC._auchCRCLo = [
    0x00, 0xC0, 0xC1, 0x01, 0xC3, 0x03, 0x02, 0xC2, 0xC6, 0x06,
    0x07, 0xC7, 0x05, 0xC5, 0xC4, 0x04, 0xCC, 0x0C, 0x0D, 0xCD,
    0x0F, 0xCF, 0xCE, 0x0E, 0x0A, 0xCA, 0xCB, 0x0B, 0xC9, 0x09,
    0x08, 0xC8, 0xD8, 0x18, 0x19, 0xD9, 0x1B, 0xDB, 0xDA, 0x1A,
    0x1E, 0xDE, 0xDF, 0x1F, 0xDD, 0x1D, 0x1C, 0xDC, 0x14, 0xD4,
    0xD5, 0x15, 0xD7, 0x17, 0x16, 0xD6, 0xD2, 0x12, 0x13, 0xD3,
    0x11, 0xD1, 0xD0, 0x10, 0xF0, 0x30, 0x31, 0xF1, 0x33, 0xF3,
    0xF2, 0x32, 0x36, 0xF6, 0xF7, 0x37, 0xF5, 0x35, 0x34, 0xF4,
    0x3C, 0xFC, 0xFD, 0x3D, 0xFF, 0x3F, 0x3E, 0xFE, 0xFA, 0x3A,
    0x3B, 0xFB, 0x39, 0xF9, 0xF8, 0x38, 0x28, 0xE8, 0xE9, 0x29,
    0xEB, 0x2B, 0x2A, 0xEA, 0xEE, 0x2E, 0x2F, 0xEF, 0x2D, 0xED,
    0xEC, 0x2C, 0xE4, 0x24, 0x25, 0xE5, 0x27, 0xE7, 0xE6, 0x26,
    0x22, 0xE2, 0xE3, 0x23, 0xE1, 0x21, 0x20, 0xE0, 0xA0, 0x60,
    0x61, 0xA1, 0x63, 0xA3, 0xA2, 0x62, 0x66, 0xA6, 0xA7, 0x67,
    0xA5, 0x65, 0x64, 0xA4, 0x6C, 0xAC, 0xAD, 0x6D, 0xAF, 0x6F,
    0x6E, 0xAE, 0xAA, 0x6A, 0x6B, 0xAB, 0x69, 0xA9, 0xA8, 0x68,
    0x78, 0xB8, 0xB9, 0x79, 0xBB, 0x7B, 0x7A, 0xBA, 0xBE, 0x7E,
    0x7F, 0xBF, 0x7D, 0xBD, 0xBC, 0x7C, 0xB4, 0x74, 0x75, 0xB5,
    0x77, 0xB7, 0xB6, 0x76, 0x72, 0xB2, 0xB3, 0x73, 0xB1, 0x71,
    0x70, 0xB0, 0x50, 0x90, 0x91, 0x51, 0x93, 0x53, 0x52, 0x92,
    0x96, 0x56, 0x57, 0x97, 0x55, 0x95, 0x94, 0x54, 0x9C, 0x5C,
    0x5D, 0x9D, 0x5F, 0x9F, 0x9E, 0x5E, 0x5A, 0x9A, 0x9B, 0x5B,
    0x99, 0x59, 0x58, 0x98, 0x88, 0x48, 0x49, 0x89, 0x4B, 0x8B,
    0x8A, 0x4A, 0x4E, 0x8E, 0x8F, 0x4F, 0x8D, 0x4D, 0x4C, 0x8C,
    0x44, 0x84, 0x85, 0x45, 0x87, 0x47, 0x46, 0x86, 0x82, 0x42,
    0x43, 0x83, 0x41, 0x81, 0x80, 0x40
];

CRC.CRC16 = function (buffer) {
    var hi = 0xff;
    var lo = 0xff;
    for (var i = 0; i < buffer.length; i++) {
        var idx = hi ^ buffer[i];
        hi = (lo ^ CRC._auchCRCHi[idx]);
        lo = CRC._auchCRCLo[idx];
    }
    return CRC.padLeft((hi << 8 | lo).toString(16).toUpperCase(), 4, '0');
};

CRC.isArray = function (arr) {
    return Object.prototype.toString.call(arr) === '[object Array]';
};

CRC.ToCRC16 = function (str) {
    return CRC.CRC16(CRC.isArray(str) ? str : CRC.strToByte(str));
};

CRC.ToModbusCRC16 = function (str) {
    return CRC.CRC16(CRC.isArray(str) ? str : CRC.strToHex(str));
};

CRC.strToByte = function (str) {
    var tmp = str.split(""),
        arr = [];
    for (var i = 0, c = tmp.length; i < c; i++) {
        var j = encodeURI(tmp[i]);
        if (j.length == 1) {
            arr.push(j.charCodeAt());
        } else {
            var b = j.split("%");
            for (var m = 1; m < b.length; m++) {
                arr.push(parseInt('0x' + b[m]));
            }
        }
    }
    return arr;
};

CRC.convertChinese = function (str) {
    var tmp = str.split(""),
        arr = [];
    for (var i = 0, c = tmp.length; i < c; i++) {
        var s = tmp[i].charCodeAt();
        if (s <= 0 || s >= 127) {
            arr.push(s.toString(16));
        } else {
            arr.push(tmp[i]);
        }
    }
    return arr;
};

CRC.filterChinese = function (str) {
    var tmp = str.split(""),
        arr = [];
    for (var i = 0, c = tmp.length; i < c; i++) {
        var s = tmp[i].charCodeAt();
        if (s > 0 && s < 127) {
            arr.push(tmp[i]);
        }
    }
    return arr;
};

CRC.strToHex = function (hex, isFilterChinese) {
    hex = isFilterChinese ? CRC.filterChinese(hex).join('') : CRC.convertChinese(hex).join('');

    //清除所有空格
    hex = hex.replace(/\s/g, "");
    //若字符个数为奇数，补一个空格
    hex += hex.length % 2 != 0 ? " " : "";

    var c = hex.length / 2,
        arr = [];
    for (var i = 0; i < c; i++) {
        arr.push(parseInt(hex.substr(i * 2, 2), 16));
    }
    return arr;
};

CRC.padLeft = function (s, w, pc) {
    if (pc == undefined) {
        pc = '0';
    }
    for (var i = 0, c = w - s.length; i < c; i++) {
        s = pc + s;
    }
    return s;
};

function str2ab(str) {
    var buffer = new ArrayBuffer(str.length / 2); // 2 bytes for each char
    var dataView = new DataView(buffer);
    var str = str.split("")
    var n = 0;
    for (var i = 0; i < str.length; i = i + 2) {
        dataView.setUint8(n, `0x${str[i]}${str[i+1]}`)
        n++;
    }
    return buffer;
}

function pad(num, n, flag = false) {
    var len = num.toString().length;
    while (len < n) {
        num = "0" + num;
        len++;
    }

    const arr = arrayGroup(num, 2)
    //高低位互换
    if (arr[1] == '00') {
        flag = true
    }
    return flag ? [arr[1], arr[0]] : [arr[0], arr[1]];
}

function add0(num,n) {
    let len = num.length
    while(len < n){
        num = '0'+num
        len++
    }
    if (len > n) {
        num = String(num).slice(1)
    }
    //console.log(num)
    return num
}

function hex2bin(hex, len) {
    var bin = parseInt(hex, 16).toString(2);
    var l = bin.length;
    for (var i = 0; i < len - l; i++) {
        bin = "0" + bin;
    }
    return bin;
}

//翻转字符串并切割成数组
function reverseStr(str) {
    var arr = str.split("");
    arr.reverse();
    return arr;
}

function timestampToTime(timestamp) {
    var date = new Date(timestamp * 1000);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
    var Y = date.getFullYear() + '-';
    var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
    var D = date.getDate() + ' ';
    var h = date.getHours() + ':';
    var m = date.getMinutes() + ':';
    var s = date.getSeconds();
    return Y+M+D+h+m+s;
}