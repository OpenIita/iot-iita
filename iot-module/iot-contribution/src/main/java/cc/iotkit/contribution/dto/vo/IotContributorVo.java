package cc.iotkit.contribution.dto.vo;

import cc.iotkit.common.excel.annotation.ExcelDictFormat;
import cc.iotkit.common.excel.convert.ExcelDictConvert;
import cc.iotkit.contribution.model.IotContributor;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;

import java.io.Serializable;



/**
 * 贡献者视图对象 iot_contributor
 *
 * @author Lion Li
 * @date 2023-07-04
 */
@Data
@ExcelIgnoreUnannotated
@AutoMapper(target = IotContributor.class)
public class IotContributorVo implements Serializable {


    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 贡献者名称
     */
    @ExcelProperty(value = "贡献者名称")
    private String contributor;

    /**
     * 头像
     */
    @ExcelProperty(value = "头像")
    private String avatar;

    /**
     * 岗位(1前端开发,2后端开发,3全栈开发,4测试,5ui设计师,6产品经理,7架构师)
     */
    @ExcelProperty(value = "岗位(1前端开发,2后端开发,3全栈开发,4测试,5ui设计师,6产品经理,7架构师)")
    private Integer post;

    /**
     * 简介
     */
    @ExcelProperty(value = "简介")
    private String intro;

    /**
     * tag列表(为了简单,逗号隔开)
     */
    @ExcelProperty(value = "tag列表(为了简单,逗号隔开)")
    private String tags;

    /**
     * 详情标题
     */
    @ExcelProperty(value = "详情标题")
    private String title;

    /**
     * 详情
     */
    @ExcelProperty(value = "详情")
    private String context;

    /**
     * 帐号状态（0正常 1停用）
     */
    @ExcelProperty(value = "帐号状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=正常,1=停用")
    private String status;


}
