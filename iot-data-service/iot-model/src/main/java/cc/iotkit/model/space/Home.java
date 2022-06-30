/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.model.space;

import cc.iotkit.model.Owned;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "home")
public class Home implements Owned {

    @Id
    private String id;

    /**
     * 家庭名称
     */
    private String name;

    /**
     * 家庭地址
     */
    private String address;

    /**
     * 关联用户id
     */
    private String uid;

    /**
     * 空间数量
     */
    private Integer spaceNum;

    /**
     * 设备数量
     */
    private Integer deviceNum;

    /**
     * 是否为用户当前使用的家庭
     */
    private Boolean current;
}
