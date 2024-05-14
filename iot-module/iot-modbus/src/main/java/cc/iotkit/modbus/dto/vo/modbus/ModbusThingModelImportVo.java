package cc.iotkit.modbus.dto.vo.modbus;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;


@ApiModel(value = "ModbusThingModel.Property")
@Data
public class ModbusThingModelImportVo implements Serializable {
    private static final long serialVersionUID = -1L;

    @ExcelProperty(value = "名称")
    private String name;

    @ExcelProperty(value = "标识符")
    private String identifier;

    @ExcelProperty(value = "描述")
    private String description;

    @ExcelProperty(value = "寄存器类型")
    private String regType;
    //寄存器地址
    @ExcelProperty(value = "寄存器地址")
    private Integer regAddr;
    //寄存器数量
    @ExcelProperty(value = "寄存器数量")
    private Integer regNum;
    /**
     * 处理公式
     * [
     *   { value: 'AB',  },
     *   { value: 'BA',  },
     *   { value: 'AB CD',  },
     *   { value: 'CD AB',  },
     *   { value: 'DC BA',  },
     *   { value: 'BA DC',  },
     * ]
     */
    @ExcelProperty(value = "处理公式")
    private String processor;


    @ExcelProperty(value = "数据类型")
    private String dataType;

    @ExcelProperty(value = "读写模式")
    private String accessMode = "rw";

    @ExcelProperty(value = "单位")
    private String unit;
    //数据顺序
    @ExcelProperty(value = "数据顺序")
    private Integer sort;


}
