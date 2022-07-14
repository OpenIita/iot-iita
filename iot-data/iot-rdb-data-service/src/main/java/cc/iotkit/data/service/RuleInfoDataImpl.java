package cc.iotkit.data.service;

import cc.iotkit.data.IRuleInfoData;
import cc.iotkit.model.Paging;
import cc.iotkit.model.rule.RuleInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RuleInfoDataImpl implements IRuleInfoData {
    @Override
    public List<RuleInfo> findByUidAndType(String uid, String type) {
        return null;
    }

    @Override
    public Paging<RuleInfo> findByUidAndType(String uid, String type, int page, int size) {
        return null;
    }

    @Override
    public Paging<RuleInfo> findByType(String type, int page, int size) {
        return null;
    }

    @Override
    public List<RuleInfo> findByUid(String uid) {
        return null;
    }

    @Override
    public Paging<RuleInfo> findByUid(String uid, int page, int size) {
        return null;
    }

    @Override
    public long countByUid(String uid) {
        return 0;
    }

    @Override
    public RuleInfo findById(String s) {
        return null;
    }

    @Override
    public RuleInfo save(RuleInfo data) {
        return null;
    }

    @Override
    public RuleInfo add(RuleInfo data) {
        return null;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public List<RuleInfo> findAll() {
        return null;
    }

    @Override
    public Paging<RuleInfo> findAll(int page, int size) {
        return null;
    }
}
