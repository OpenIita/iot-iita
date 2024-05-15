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

package cc.iotkit.data.manager;

import cc.iotkit.common.api.Paging;
import cc.iotkit.data.IOwnedData;
import cc.iotkit.model.rule.RuleInfo;

import java.util.List;

public interface IRuleInfoData extends IOwnedData<RuleInfo, String> {

    List<RuleInfo> findByUidAndType(String uid, String type);

    Paging<RuleInfo> findByUidAndType(String uid, String type, int page, int size);

    Paging<RuleInfo> findByType(String type, int page, int size);

}
