package cc.iotkit.manager.controller.aligenie;

import cc.iotkit.dao.AligenieDeviceRepository;
import cc.iotkit.manager.controller.DbBaseController;
import cc.iotkit.model.aligenie.AligenieDevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/aligenieDevice")
public class AligenieDeviceController extends DbBaseController<AligenieDeviceRepository, AligenieDevice> {

    @Autowired
    public AligenieDeviceController(AligenieDeviceRepository aligenieDeviceRepository) {
        super(aligenieDeviceRepository);
    }

}
