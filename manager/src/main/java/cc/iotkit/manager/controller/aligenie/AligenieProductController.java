package cc.iotkit.manager.controller.aligenie;

import cc.iotkit.dao.AligenieProductDao;
import cc.iotkit.dao.AligenieProductRepository;
import cc.iotkit.dao.ProductCache;
import cc.iotkit.manager.controller.DbBaseController;
import cc.iotkit.manager.model.aligenie.AligenieProductVo;
import cc.iotkit.manager.service.DataOwnerService;
import cc.iotkit.manager.utils.AuthUtil;
import cc.iotkit.model.product.Product;
import cc.iotkit.model.aligenie.AligenieProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/aligenie/product")
public class AligenieProductController extends DbBaseController<AligenieProductRepository, AligenieProduct> {

    private final ProductCache productCache;
    private final AligenieProductDao aligenieProductDao;
    private final DataOwnerService dataOwnerService;


    @Autowired
    public AligenieProductController(AligenieProductRepository aligenieProductRepository,
                                     ProductCache productCache,
                                     AligenieProductDao aligenieProductDao,
                                     DataOwnerService dataOwnerService) {
        super(aligenieProductRepository);
        this.productCache = productCache;
        this.aligenieProductDao = aligenieProductDao;
        this.dataOwnerService = dataOwnerService;
    }

    @GetMapping("/products")
    public List<AligenieProductVo> products() {
        List<AligenieProductVo> productVos = new ArrayList<>();
        List<AligenieProduct> aligenieProducts = repository
                .findAll(Example
                        .of(AligenieProduct.builder()
                                .uid(AuthUtil.getUserId())
                                .build()));

        for (AligenieProduct ap : aligenieProducts) {
            Product product = productCache.findById(ap.getProductKey());
            productVos.add(new AligenieProductVo(ap, product.getName()));
        }
        return productVos;
    }

    @PostMapping("/save")
    public void save(AligenieProduct product) {
        if (product.getCreateAt() == null) {
            product.setCreateAt(System.currentTimeMillis());
        }

        dataOwnerService.checkOwnerSave(repository,product);
        aligenieProductDao.save(product.getProductId(), product);
    }
}
