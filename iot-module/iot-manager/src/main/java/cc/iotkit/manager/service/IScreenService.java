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

package cc.iotkit.manager.service;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.api.Paging;
import cc.iotkit.manager.dto.bo.screen.DebugChangeBo;
import cc.iotkit.manager.dto.bo.screen.PublishChangeBo;
import cc.iotkit.model.screen.Screen;
import cc.iotkit.model.screen.ScreenApi;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author：tfd
 * @Date：2023/6/25 15:14
 */
public interface IScreenService {
    Long uploadResourceFile(MultipartFile file, Long id);

    List<ScreenApi> findByScreenId(Long id);

    Screen getDefaultScreen();

    List<ScreenApi> syncResourceApis(Long id);

    void previewApis(List<ScreenApi> screenApis);

    void saveScreenApis(List<ScreenApi> screenApis);

    void debugModeChange(DebugChangeBo debugChange);

    void addBigScreen(Screen screen);

    void saveBigScreen(Screen screen);

    void publishStatusChange(PublishChangeBo data);

    void setDefaultScreen(Long id);

    void deleteScreen(Long id);

    Paging<Screen> getBigScreens(PageRequest<Screen> request);
}
