package cc.iotkit.manager.controller;

import cc.iotkit.common.enums.ErrCode;
import cc.iotkit.common.exception.BizException;
import cc.iotkit.common.utils.ReflectUtil;
import cc.iotkit.data.IBigScreenApiData;
import cc.iotkit.data.IBigScreenData;
import cc.iotkit.manager.service.DataOwnerService;
import cc.iotkit.model.Paging;
import cc.iotkit.model.screen.BigScreen;
import cc.iotkit.model.screen.BigScreenApi;
import cc.iotkit.screen.BigScreenManager;
import cc.iotkit.screen.config.BigScreenConfig;
import cc.iotkit.utils.AuthUtil;
import cn.hutool.core.util.ZipUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @Author：tfd
 * @Date：2023/5/6 15:23
 */
@Api(tags = {"大屏"})
@Slf4j
@RestController
@RequestMapping("/bigScreen")
public class BigScreenController {

    @Autowired
    private BigScreenConfig bigScreenConfig;

    @Autowired
    private IBigScreenData bigScreenData;

    @Autowired
    private IBigScreenApiData bigScreenApiData;

    @Autowired
    private BigScreenManager screenManager;

    @Autowired
    private DataOwnerService dataOwnerService;


    @ApiOperation(value = "上传大屏资源包", httpMethod = "POST")
    @PostMapping("/uploadResourceFile")
    public String uploadResourceFile(@RequestParam("file") MultipartFile file,
                            @RequestParam("id") String id){
        if (file == null) {
            throw new BizException(ErrCode.PARAMS_EXCEPTION);
        }
        log.info("saving upload resource file:{}", file.getName());
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            if (StringUtils.hasLength(id)) {
                getAndCheckBigScreen(id);
            } else {
                id = UUID.randomUUID().toString();
            }
            Path filePath = bigScreenConfig.getBigScreenFilePath(id);
            Files.createDirectories(filePath);
            Path targetLocation = filePath.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            ZipUtil.unzip(bigScreenConfig.getBigScreenFilePath(id).toString()+"/"+fileName);
            return id;
        } catch (IOException ex) {
            throw new BizException(ErrCode.UPLOAD_FILE_ERROR, ex);
        }
    }

    @ApiOperation(value = "获取大屏接口", httpMethod = "POST")
    @PostMapping("/getScreenApis/{id}")
    public List<BigScreenApi> getScreenApis(@PathVariable("id") String id) {
        if (!StringUtils.hasLength(id)) {
            throw new BizException(ErrCode.ID_BLANK);
        }
        BigScreen screen = getAndCheckBigScreen(id);
        return bigScreenApiData.findByScreenId(screen.getId());
    }

    @ApiOperation(value = "获取默认大屏", httpMethod = "POST")
    @PostMapping("/getDefaultScreen")
    public BigScreen getDefaultScreen() {
        return bigScreenData.findByUidAndIsDefault(AuthUtil.getUserId(), true);
    }

    @ApiOperation(value = "同步资源包接口", httpMethod = "POST")
    @PostMapping("/syncResourceApis/{id}")
    public List<BigScreenApi> syncResourceApis(@PathVariable("id") String id) {
        if (!StringUtils.hasLength(id)) {
            throw new BizException(ErrCode.ID_BLANK);
        }
        BigScreen screen = getAndCheckBigScreen(id);
        return screenManager.getScreenApis(screen);
    }

    @ApiOperation(value = "预览接口", httpMethod = "POST")
    @PostMapping("/previewApis/{id}")
    public void previewApis(@PathVariable("id") String id,@RequestBody List<BigScreenApi> screenApis) {
        if (!StringUtils.hasLength(id)) {
            throw new BizException(ErrCode.ID_BLANK);
        }
        if (screenApis.size()==0) {
            throw new BizException(ErrCode.API_LIST_BLANK);
        }
        BigScreen screen = getAndCheckBigScreen(id);
        screenManager.previewApis(screen,screenApis);
    }

    @ApiOperation(value = "保存大屏接口", httpMethod = "POST")
    @PostMapping("/saveScreenApis/{id}")
    public void publishApis(@PathVariable("id") String id,@RequestBody List<BigScreenApi> screenApis) {
        if (!StringUtils.hasLength(id)) {
            throw new BizException(ErrCode.ID_BLANK);
        }
        if (screenApis.size()==0) {
            throw new BizException(ErrCode.API_LIST_BLANK);
        }
        BigScreen screen = getAndCheckBigScreen(id);
        bigScreenApiData.deleteByScreenId(screen.getId());
        for (BigScreenApi screenApi:screenApis){
            screenApi.setUid(AuthUtil.getUserId());
            screenApi.setScreenId(screen.getId());
            bigScreenApiData.save(screenApi);
        }
    }

    @ApiOperation(value = "调试模式转换", httpMethod = "POST")
    @PostMapping("/debugModeChange/{id}/debugMode/{debugMode}")
    public void debugMode(@PathVariable("id") String id,@PathVariable("debugMode") Boolean debugMode) {
        if (!StringUtils.hasLength(id)) {
            throw new BizException(ErrCode.ID_BLANK);
        }
        BigScreen screen = getAndCheckBigScreen(id);
        screenManager.debugMode(screen,debugMode);
    }

    @ApiOperation(value = "添加大屏", httpMethod = "POST")
    @PostMapping("/addBigScreen")
    public void addBigScreen(BigScreen screen) {
        String id = screen.getId();
        if (!StringUtils.hasLength(id)) {
            throw new BizException(ErrCode.ID_BLANK);
        }
        Path resPath = bigScreenConfig.getBigScreenFilePath(id);
        if (!resPath.resolve(screen.getResourceFile()).toFile().exists()) {
            throw new BizException(ErrCode.RESOURCE_FILE_NOT_FOUND);
        }
        BigScreen bigScreen = bigScreenData.findById(id);
        if (bigScreen != null) {
            throw new BizException(ErrCode.BIG_SCREEN_ALREADY);
        }
        try {
            screen.setCreateAt(System.currentTimeMillis());
            screen.setUid(AuthUtil.getUserId());
            if(bigScreenData.countByUid(AuthUtil.getUserId())==0l){
                screen.setIsDefault(true);
            }
            bigScreenData.save(screen);
        } catch (Throwable e) {
            throw new BizException(ErrCode.ADD_BIG_SCREEN_ERROR, e);
        }
    }

    @ApiOperation(value = "保存大屏", httpMethod = "POST")
    @PostMapping("/saveBigScreen")
    public void saveBigScreen(BigScreen screen) {
        String id = screen.getId();
        if (!StringUtils.hasLength(id)) {
            throw new BizException(ErrCode.ID_BLANK);
        }
        Path jarPath = bigScreenConfig.getBigScreenFilePath(id);
        if (!jarPath.resolve(screen.getResourceFile()).toFile().exists()) {
            throw new BizException(ErrCode.RESOURCE_FILE_NOT_FOUND);
        }

        BigScreen oldBigScreen = getAndCheckBigScreen(id);
        screen = ReflectUtil.copyNoNulls(screen, oldBigScreen);

        try {
            bigScreenData.save(screen);
        } catch (Throwable e) {
            throw new BizException(ErrCode.ADD_BIG_SCREEN_ERROR, e);
        }
    }

    @ApiOperation(value = "发布状态改变", httpMethod = "POST")
    @PostMapping("/publishStatusChange/{id}/state/{state}")
    public void publishStatusChange(@PathVariable("id") String id,
                              @PathVariable("state") String state) {
        BigScreen screen = getAndCheckBigScreen(id);

        if (screen.STATE_RUNNING.equals(state)) {//发布状态
            List<BigScreenApi> screenApis=bigScreenApiData.findByScreenId(screen.getId());
            if(screenApis==null||screenApis.size()==0){
                throw new BizException(ErrCode.API_LIST_BLANK);
            }
            screen.setState(screen.STATE_RUNNING);
            screenManager.register(screen);
            screenManager.publish(screen);
        } else {//取消发布
            screen.setState(screen.STATE_STOPPED);
            screenManager.unpublish(screen);
        }
        bigScreenData.save(screen);
    }

    @ApiOperation(value = "设置默认大屏", httpMethod = "POST")
    @PostMapping("/setDefaultScreen/{id}")
    public void setDefaultScreen(@PathVariable("id") String id) {
        BigScreen screen = getAndCheckBigScreen(id);
        BigScreen oldBigScreen=bigScreenData.findByUidAndIsDefault(AuthUtil.getUserId(), true);
        oldBigScreen.setIsDefault(false);
        bigScreenData.save(oldBigScreen);
        screen.setIsDefault(true);
        bigScreenData.save(screen);
    }

    @ApiOperation(value = "删除大屏", httpMethod = "POST")
    @PostMapping("/deleteBigScreen/{id}")
    public void deleteBigScreen(@PathVariable("id") String id) {
        BigScreen bigScreen = getAndCheckBigScreen(id);
        try {
            Path path = Paths.get(String.format("%s/%s", bigScreenConfig.getBigScreenDir(), id))
                    .toAbsolutePath().normalize();
            File file = path.toFile();
            try {
                if (file.isDirectory()) {
                    FileUtils.deleteDirectory(file);
                } else {
                    FileUtils.delete(file);
                }
            } catch (NoSuchFileException e) {
                log.warn("delete big screen resource error", e);
            }
            bigScreenData.deleteById(bigScreen.getId());
        } catch (Throwable e) {
            throw new BizException(ErrCode.DELETE_BIG_SCREEN_ERROR, e);
        }
    }

    @ApiOperation(value = "获取大屏列表", httpMethod = "POST")
    @PostMapping("/getBigScreens/{size}/{page}")
    public Paging<BigScreen> getBigScreens(
            @PathVariable("size") int size,
            @PathVariable("page") int page) {
        Paging<BigScreen> bigScreens = bigScreenData.findAll(page, size);
        return bigScreens;
    }

    private BigScreen getAndCheckBigScreen(@PathVariable("id") String id) {
        BigScreen oldBigScreen = bigScreenData.findById(id);
        if (oldBigScreen == null) {
            throw new BizException(ErrCode.BIG_SCREEN_NOT_FOUND);
        }
        dataOwnerService.checkOwner(oldBigScreen);
        return oldBigScreen;
    }
}
