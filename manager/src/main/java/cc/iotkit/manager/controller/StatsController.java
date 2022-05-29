package cc.iotkit.manager.controller;

import cc.iotkit.dao.*;
import cc.iotkit.manager.model.stats.MainStats;
import cc.iotkit.utils.AuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/stats")
public class StatsController {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private DeviceReportRepository deviceReportRepository;
    @Autowired
    private DeviceReportDao deviceReportDao;
    @Autowired
    private DeviceCache deviceCache;

    @GetMapping("/main")
    public MainStats getMainStats() {
        MainStats mainStats = new MainStats();
        String uid = AuthUtil.getUserId();

        long now = System.currentTimeMillis();
        if (AuthUtil.isAdmin()) {
            mainStats.setCategoryTotal(categoryRepository.count());
            mainStats.setProductTotal(productRepository.count());
            mainStats.setDeviceTotal(deviceRepository.count());
            mainStats.setReportTotal(deviceReportRepository.count());
            //上报数据统计
            mainStats.setReportDataStats(deviceReportDao.getDeviceMessageStats(now - 48 * 3600 * 1000, now));
            //产品数量统计
            mainStats.setDeviceStatsOfCategory(deviceCache.getDeviceStatsByCategory(""));
        } else {
            mainStats.setCategoryTotal(categoryRepository.count());
            mainStats.setProductTotal(productRepository.countByUid(uid));
            mainStats.setDeviceTotal(deviceRepository.countByUid(uid));
            mainStats.setReportTotal(deviceReportRepository.countByUid(uid));
            //上报数据统计
            mainStats.setReportDataStats(deviceReportDao.getDeviceMessageStatsWithUid(uid, now - 48 * 3600 * 1000, now));
            //产品数量统计
            mainStats.setDeviceStatsOfCategory(deviceCache.getDeviceStatsByCategory(uid));
        }

        return mainStats;
    }

}
