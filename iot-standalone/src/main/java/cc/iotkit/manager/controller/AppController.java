/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.manager.controller;

import cc.iotkit.dao.AppDesignRepository;
import cc.iotkit.dao.CategoryRepository;
import cc.iotkit.dao.ProductRepository;
import cc.iotkit.manager.model.vo.AppDesignVo;
import cc.iotkit.manager.service.DataOwnerService;
import cc.iotkit.model.Paging;
import cc.iotkit.model.product.AppDesign;
import cc.iotkit.model.product.Category;
import cc.iotkit.model.product.Product;
import cc.iotkit.utils.AuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/app")
public class AppController {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private AppDesignRepository appDesignRepository;
    @Autowired
    private DataOwnerService dataOwnerService;

    @PostMapping("/designs")
    public Paging<AppDesignVo> getDesigns() {

        List<AppDesignVo> appDesignVos = new ArrayList<>();
        Iterable<Product> products;
        Iterable<AppDesign> appDesigns;
        if (AuthUtil.isAdmin()) {
            products = productRepository.findAll();
            appDesigns = appDesignRepository.findAll();
        } else {
            products = productRepository.findByUid(AuthUtil.getUserId());
            appDesigns = appDesignRepository.findByUid(AuthUtil.getUserId());
        }

        products.forEach(product -> {
            Category category = categoryRepository.findById(product.getCategory()).orElse(new Category());
            AppDesignVo dhVo = AppDesignVo.builder()
                    .productName(product.getName())
                    .cateName(category.getName())
                    .productKey(product.getId())
                    .build();
            appDesignVos.add(dhVo);
            for (AppDesign dh : appDesigns) {
                if (product.getId().equals(dh.getProductKey())) {
                    dhVo.setId(dh.getId());
                    dhVo.setModifyAt(dh.getModifyAt());
                    dhVo.setState(dh.getState());
                    break;
                }
            }
        });

        return new Paging<>(appDesignRepository.count(),
                appDesignVos);
    }

    @GetMapping("/designDetail")
    public AppDesign designDetail(String id) {
        if (StringUtils.isBlank(id)) {
            return new AppDesign();
        }
        return dataOwnerService.checkOwner(appDesignRepository.findById(id)
                .orElse(new AppDesign()));
    }

    @PostMapping("/saveDesign")
    public void saveDesign(AppDesign design) throws IOException {
        design.setState(false);
        design.setModifyAt(System.currentTimeMillis());
        dataOwnerService.checkOwnerSave(appDesignRepository, design);
        appDesignRepository.save(design);
    }

}
