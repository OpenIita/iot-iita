package cc.iotkit.data;

import cc.iotkit.model.Paging;
import cc.iotkit.model.rule.RuleInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IRuleInfoData extends IOwnedData<RuleInfo, String> {

    List<RuleInfo> findByUidAndType(String uid, String type);

    Paging<RuleInfo> findByUidAndType(String uid, String type, int page, int size);

    Paging<RuleInfo> findByType(String type, int page, int size);

}
