package cc.iotkit.manager.dto.vo.ota;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: 石恒
 * @Date: 2023/6/27 22:35
 * @Description:
 */
@Data
@Builder
@ExcelIgnoreUnannotated
@ApiModel(value = "OtaPackageUploadVo")
@AutoMapper(target = OtaPackageUploadVo.class)
public class OtaPackageUploadVo implements Serializable {
    private String url;
    private Long size;
    private String md5;
}
