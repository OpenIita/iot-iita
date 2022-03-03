package cc.iotkit.manager.controller.api;

import cc.iotkit.dao.ProductRepository;
import cc.iotkit.dao.ThingModelRepository;
import cc.iotkit.model.product.Product;
import cc.iotkit.model.product.ThingModel;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("api-product")
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ThingModelRepository thingModelRepository;

    @ApiOperation("产品列表")
    @GetMapping("/list")
    public List<Product> list() {
        return productRepository.findAll();
    }

    @ApiOperation("产品详情")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "产品pk", name = "pk", required = true, dataType = "String"),
    })
    @GetMapping("/{pk}")
    public Product detail(@PathVariable("pk") String pk) {
        return productRepository.findById(pk).orElseThrow(() -> new RuntimeException("product not found."));
    }

    @ApiOperation("产品物模型")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "产品pk", name = "pk", required = true, dataType = "String"),
    })
    @GetMapping("/{pk}/thingModel")
    public ThingModel thingModel(@PathVariable("pk") String pk) {
        return thingModelRepository.findById(pk).orElseThrow(() -> new RuntimeException("thing model not found."));
    }

}
