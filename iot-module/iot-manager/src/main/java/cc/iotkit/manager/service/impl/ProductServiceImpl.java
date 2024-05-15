/*
 *
 *  * | Licensed 未经许可不能去掉「OPENIITA」相关版权
 *  * +----------------------------------------------------------------------
 *  * | Author: xw2sy@163.com
 *  * +----------------------------------------------------------------------
 *
 *  Copyright [2024] [OPENIITA]
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * /
 */

package cc.iotkit.manager.service.impl;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.common.enums.ErrCode;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.oss.entity.UploadResult;
import cc.iotkit.common.oss.factory.OssFactory;
import cc.iotkit.common.utils.JsonUtils;
import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.data.manager.*;
import cc.iotkit.manager.config.AliyunConfig;
import cc.iotkit.manager.dto.bo.category.CategoryBo;
import cc.iotkit.manager.dto.bo.product.IconBo;
import cc.iotkit.manager.dto.bo.product.IconTypeBo;
import cc.iotkit.manager.dto.bo.product.ProductBo;
import cc.iotkit.manager.dto.bo.productmodel.ProductModelBo;
import cc.iotkit.manager.dto.bo.thingmodel.ThingModelBo;
import cc.iotkit.manager.dto.vo.category.CategoryVo;
import cc.iotkit.manager.dto.vo.product.IconTypeVo;
import cc.iotkit.manager.dto.vo.product.IconVo;
import cc.iotkit.manager.dto.vo.product.ProductVo;
import cc.iotkit.manager.dto.vo.productmodel.ProductModelVo;
import cc.iotkit.manager.dto.vo.thingmodel.ThingModelVo;
import cc.iotkit.manager.service.DataOwnerService;
import cc.iotkit.manager.service.IProductService;
import cc.iotkit.model.product.*;
import cc.iotkit.temporal.IDbStructureData;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ObjectUtil;
import com.github.yitter.idgen.YitIdHelper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: jay
 * @Date: 2023/5/30 17:00
 * @Version: V1.0
 * @Description: 产品服务实现类
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    @Qualifier("productDataCache")
    private IProductData productData;
    @Autowired
    @Qualifier("thingModelDataCache")
    private IThingModelData thingModelData;
    @Autowired
    @Qualifier("categoryDataCache")
    private ICategoryData categoryData;

    @Autowired
    private DataOwnerService dataOwnerService;
    @Autowired
    private AliyunConfig aliyunConfig;
    @Autowired
    @Qualifier("productModelDataCache")
    private IProductModelData productModelData;
    @Autowired
    private IDbStructureData dbStructureData;

    @Autowired
    private IDeviceInfoData deviceInfoData;

    @Autowired
    private IIconTypeData iconTypeData;

    @Autowired
    private IIconData iconData;

    @Override
    public ProductVo addEntity(ProductBo data) {
        Product product = data.to(Product.class);
        if (product.getCreateAt() == null) {
            product.setCreateAt(System.currentTimeMillis());
        }

        String secret = UUID.randomUUID().toString(true);
        product.setProductSecret(secret);
        String productKey = data.getProductKey();
        Product oldProduct = productData.findByProductKey(productKey);
        if (oldProduct != null) {
            throw new BizException(ErrCode.PRODUCT_KEY_EXIST);
        }

        productData.save(product);
        return MapstructUtils.convert(product, ProductVo.class);
    }

    @Override
    public boolean updateEntity(ProductBo productBo) {
        Product product = productBo.to(Product.class);

        if (product.getCreateAt() == null) {
            product.setCreateAt(System.currentTimeMillis());
        }
        productData.save(product);
        return true;
    }

    @Override
    public ProductVo getDetail(String productKey) {
        return MapstructUtils.convert(productData.findByProductKey(productKey), ProductVo.class);
    }

    @Override
    public boolean deleteProduct(String productKey) {
        Product product = productData.findByProductKey(productKey);
        if (Objects.isNull(product)) {
            throw new BizException(ErrCode.PRODUCT_NOT_FOUND);
        }
        boolean exist = deviceInfoData.existByProductKey(productKey);
        if (exist) {
            throw new BizException(ErrCode.DEVICE_HAS_ASSOCIATED);
        }
        productData.deleteById(product.getId());
        return true;
    }

    @Override
    public ThingModelVo getThingModelByProductKey(String productKey) {
        ThingModel thingModel = thingModelData.findByProductKey(productKey);
        if(ObjectUtil.isNotNull(thingModel)&&ObjectUtil.isNotNull(thingModel.getModel())&&
                thingModel.getModel().getProperties().size()>0){
            for (ThingModel.Property pro:thingModel.getModel().getProperties()){
                if(ObjectUtil.isNotNull(pro.getIconId())){
                    pro.setIcon(iconData.findById(pro.getIconId()));
                }
            }
        }
        return MapstructUtils.convert(thingModel, ThingModelVo.class);
    }

    @Override
    public boolean saveThingModel(ThingModelBo data) {
        String productKey = data.getProductKey();
        String model = data.getModel();
        ThingModel oldData = thingModelData.findByProductKey(productKey);
        ThingModel thingModel = new ThingModel(YitIdHelper.nextId(), productKey, JsonUtils.parseObject(model, ThingModel.Model.class));

        //验证物模型合法性
        List<ThingModel.Property> properties = thingModel.getModel().getProperties();
        for (ThingModel.Property property : properties) {
            //属性标识符合法性校验
            String identifier = property.getIdentifier();
            if (StringUtils.isBlank(identifier)) {
                throw new BizException("属性标识符不能为空");
            }
            if (!identifier.matches("^[a-zA-Z].*")) {
                throw new BizException("属性标识符【" + identifier + "】不合法");
            }
        }

        if (oldData == null) {
            //定义时序数据库物模型数据结构
            dbStructureData.defineThingModel(thingModel);
        } else {
            thingModel.setId(oldData.getId());
            //更新时序数据库物模型数据结构
            dbStructureData.updateThingModel(thingModel);
        }
        thingModelData.save(thingModel);
        return true;
    }

    @Override
    public boolean deleteThingModel(Long id) {
        ThingModel thingModel = thingModelData.findById(id);
        //删除时序数据库物模型数据结构
        dbStructureData.defineThingModel(thingModel);
        thingModelData.deleteById(id);
        return true;
    }


    @Override
    public boolean deleteCategory(String id) {
        categoryData.deleteById(id);
        return true;
    }

    @Override
    public boolean editCategory(CategoryBo req) {
        Category cate = req.to(Category.class);
        cate.setCreateAt(System.currentTimeMillis());
        categoryData.save(cate);
        return true;
    }

    @Override
    @SneakyThrows
    public String uploadImg(String productKey, MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new BizException(ErrCode.PARAMS_EXCEPTION);
        }
        String contentType = file.getContentType();
        if (contentType == null || !contentType.contains("image/")) {
            throw new BizException(ErrCode.PARAMS_EXCEPTION, "上传的文件不是图片");
        }

        UploadResult upload = OssFactory.instance().upload(file.getInputStream(),
                String.format("/product/%s%s", productKey, originalFilename.substring(originalFilename.lastIndexOf("."))),
                contentType);
        return upload.getUrl();
    }

    @Override
    public Paging<ProductVo> selectPageList(PageRequest<ProductBo> request) {
        Paging<ProductVo> result =productData.findAll(request.to(Product.class)).to(ProductVo.class);
        for (ProductVo row : result.getRows()) {
            if(ObjectUtil.isNotNull(row.getIconId())){
                row.setIcon(iconData.findById(row.getIconId()));
            }
        }
        return result;
    }

    @Override
    public Paging<CategoryVo> selectCategoryPageList(PageRequest<CategoryBo> request) {
        return MapstructUtils.convert(categoryData.findAll(request.to(Category.class)), CategoryVo.class);

    }

    @Override
    public List<CategoryVo> selectCategoryList() {
        return MapstructUtils.convert(categoryData.findAll(), CategoryVo.class);

    }

    @Override
    public List<ProductModelVo> getModels(String productKey) {
//        dataOwnerService.checkOwner(productData, productKey);
        return MapstructUtils.convert(productModelData.findByProductKey(productKey), ProductModelVo.class);

    }

    @Override
    public boolean deleteProductModel(String id) {
        productModelData.deleteById(id);
        return true;
    }

    @Override
    public ProductVo findByProductKey(String productKey) {
        return productData.findByProductKey(productKey).to(ProductVo.class);
    }

    @Override
    public boolean saveIconType(IconTypeBo data) {
        IconType iconType = data.to(IconType.class);
        iconTypeData.save(iconType);
        return true;
    }

    @Override
    public boolean deleteIconType(Long data) {
        iconTypeData.deleteById(data);
        return true;
    }

    @Override
    public boolean saveIcon(IconBo data) {
        Icon icon = data.to(Icon.class);
        iconData.save(icon);
        return true;
    }

    @Override
    public boolean deleteIcon(Long data) {
        iconData.deleteById(data);
        return true;
    }

    @Override
    public List<IconTypeVo> selectIconTypeList() {
        return MapstructUtils.convert(iconTypeData.findAll(), IconTypeVo.class);
    }

    @Override
    public Paging<IconVo> selectIconPageList(PageRequest<IconBo> request) {
        Map<String,String> sortMap = new HashMap<>();
        sortMap.put("updateTime","desc");
        request.setSortMap(sortMap);
        return iconData.findAll(request.to(Icon.class)).to(IconVo.class);
    }

    @Override
    public boolean editProductModel(ProductModelBo productModelBo) {
        ProductModel productModel = productModelBo.to(ProductModel.class);
        String model = productModel.getModel();
        String productKey = productModel.getProductKey();
        Product product = productData.findByProductKey(productKey);
        if (product == null) {
            throw new BizException(ErrCode.PRODUCT_NOT_FOUND);
        }

        ProductModel oldScript = productModelData.findByModel(model);
        if (oldScript != null && !oldScript.getProductKey().equals(productKey)) {
            throw new BizException(ErrCode.MODEL_ALREADY);
        }

        productModel.setModifyAt(System.currentTimeMillis());
        productModelData.save(productModel);
        return true;
    }

    private Product getProduct(String productKey) {
        return productData.findByProductKey(productKey);
    }


    /***********/
    private void checkProductOwner(String productKey) {
//        dataOwnerService.checkOwner(productData.findByProductKey(productKey));
    }

}
