/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.manager.controller.aligenie;

import cc.iotkit.dao.AligenieProductRepository;
import cc.iotkit.manager.service.DataOwnerService;
import cc.iotkit.utils.AuthUtil;
import cc.iotkit.model.aligenie.AligenieProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/aligenie/product")
public class AligenieProductController {

    @Autowired
    private DataOwnerService dataOwnerService;

    @Autowired
    private AligenieProductRepository aligenieProductRepository;


    @GetMapping("/products")
    public List<AligenieProduct> products() {
        return aligenieProductRepository.findByUid(AuthUtil.getUserId());
    }

    @PostMapping("/save")
    public void save(AligenieProduct product) {
        if (product.getCreateAt() == null) {
            product.setCreateAt(System.currentTimeMillis());
        }

        dataOwnerService.checkOwnerSave(aligenieProductRepository, product);
        aligenieProductRepository.save(product);
    }
}