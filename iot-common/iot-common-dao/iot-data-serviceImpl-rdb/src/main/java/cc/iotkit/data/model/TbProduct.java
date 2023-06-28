/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.data.model;

import cc.iotkit.model.product.Product;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@ApiModel(value = "产品")
@Table(name = "product")
@AutoMapper(target = Product.class)
public class TbProduct {

    @Id
    @GeneratedValue(generator = "SnowflakeIdGenerator")
    @GenericGenerator(name = "SnowflakeIdGenerator", strategy = "cc.iotkit.data.config.id.SnowflakeIdGenerator")
    @ApiModelProperty(value = "产品id")
    private Long id;

    @ApiModelProperty(value = "产品key")
    private String productKey;

    @ApiModelProperty(value = "产品密钥")
    private String productSecret;

    @ApiModelProperty(value = "产品名称")
    private String name;

    @ApiModelProperty(value = "品类")
    private String category;

    @ApiModelProperty(value = "节点类型")
    private Integer nodeType;

    /**
     * 所属平台用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private String uid;

    @ApiModelProperty(value = "图片")
    private String img;

    /**
     * 是否透传,true/false
     */
    @ApiModelProperty(value = "是否透传,true/false")
    private String transparent;

    @ApiModelProperty(value = "创建时间")
    private Long createAt;

}