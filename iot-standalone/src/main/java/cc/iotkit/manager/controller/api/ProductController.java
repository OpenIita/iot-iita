/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.manager.controller.api;

import cc.iotkit.dao.ProductRepository;
import cc.iotkit.dao.ThingModelRepository;
import cc.iotkit.model.product.Product;
import cc.iotkit.model.product.ThingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController("api-product")
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ThingModelRepository thingModelRepository;

    @GetMapping("/list")
    public List<Product> list() {
        List<Product> list = new ArrayList<>();
        productRepository.findAll().forEach(list::add);
        return list;
    }

    @GetMapping("/{pk}")
    public Product detail(@PathVariable("pk") String pk) {
        return productRepository.findById(pk).orElseThrow(() -> new RuntimeException("product not found."));
    }

    @GetMapping("/{pk}/thingModel")
    public ThingModel thingModel(@PathVariable("pk") String pk) {
        return thingModelRepository.findById(pk).orElseThrow(() -> new RuntimeException("thing model not found."));
    }

}
