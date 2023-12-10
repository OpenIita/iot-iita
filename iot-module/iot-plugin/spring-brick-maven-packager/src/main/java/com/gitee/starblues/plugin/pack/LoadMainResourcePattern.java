/**
 * Copyright [2019-Present] [starBlues]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gitee.starblues.plugin.pack;

import lombok.Data;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * 从主程序加载资源配置
 *
 * @author starBlues
 * @since 3.0.0
 * @version 3.0.0
 */
@Data
public class LoadMainResourcePattern {

    @Parameter(name = "includes")
    private String[] includes;

    @Parameter(name = "excludes")
    private String[] excludes;

}
