package cc.iotkit.data.service;

import cc.iotkit.common.utils.ReflectUtil;
import cc.iotkit.data.IDeviceInfoData;
import cc.iotkit.data.dao.*;
import cc.iotkit.data.model.*;
import cc.iotkit.model.Paging;
import cc.iotkit.model.device.DeviceInfo;
import cc.iotkit.model.stats.DataItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Primary
@Service
public class DeviceInfoDataImpl implements IDeviceInfoData {

    @Autowired
    private DeviceInfoRepository deviceInfoRepository;
    @Autowired
    private DeviceSubUserRepository deviceSubUserRepository;
    @Autowired
    private DeviceGroupMappingRepository deviceGroupMappingRepository;
    @Autowired
    private DeviceGroupRepository deviceGroupRepository;
    @Autowired
    private DeviceTagRepository deviceTagRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void saveProperties(String deviceId, Map<String, Object> properties) {
    }

    @Override
    public Map<String, Object> getProperties(String deviceId) {
        return new HashMap<>();
    }

    @Override
    public DeviceInfo findByDeviceId(String deviceId) {
        TbDeviceInfo tbDeviceInfo = deviceInfoRepository.findByDeviceId(deviceId);
        DeviceInfo dto = DeviceInfoMapper.M.toDto(tbDeviceInfo);

        fillDeviceInfo(deviceId, tbDeviceInfo, dto);
        return dto;
    }

    /**
     * 填充设备其它信息
     */
    private void fillDeviceInfo(String deviceId, TbDeviceInfo vo, DeviceInfo dto) {
        //取子关联用户
        dto.setSubUid(deviceSubUserRepository.findByDeviceId(deviceId).stream()
                .map(TbDeviceSubUser::getUid).collect(Collectors.toList()));

        //取设备所属分组
        List<TbDeviceGroupMapping> groupMappings = deviceGroupMappingRepository.findByDeviceId(deviceId);
        Map<String, DeviceInfo.Group> groups = new HashMap<>();
        for (TbDeviceGroupMapping mapping : groupMappings) {
            TbDeviceGroup deviceGroup = deviceGroupRepository.findById(mapping.getGroupId()).orElse(null);
            if (deviceGroup == null) {
                continue;
            }
            groups.put(deviceGroup.getId(), new DeviceInfo.Group(deviceGroup.getId(), deviceGroup.getName()));
        }
        dto.setGroup(groups);

        //取设备标签
        List<TbDeviceTag> deviceTags = deviceTagRepository.findByDeviceId(deviceId);
        Map<String, DeviceInfo.Tag> tagMap = new HashMap<>();
        for (TbDeviceTag tag : deviceTags) {
            tagMap.put(tag.getCode(), new DeviceInfo.Tag(tag.getCode(), tag.getName(), tag.getValue()));
        }
        dto.setTag(tagMap);

        //将设备状态从vo转为dto的
        parseStateToDto(vo, dto);
    }

    /**
     * 将设备状态从vo转为dto的
     */
    private void parseStateToDto(TbDeviceInfo vo, DeviceInfo dto) {
        dto.setState(new DeviceInfo.State("online".equals(vo.getState()),
                vo.getOnlineTime(), vo.getOfflineTime()));
    }

    /**
     * 将设备状态从dto转vo
     */
    private void parseStateToVo(DeviceInfo dto, TbDeviceInfo vo) {
        DeviceInfo.State state = dto.getState();
        vo.setState(state.isOnline() ? "online" : "offline");
        vo.setOfflineTime(state.getOfflineTime());
        vo.setOnlineTime(state.getOnlineTime());
    }

    /**
     * 将数据库中查出来的vo转为dto
     */
    private DeviceInfo parseVoToDto(TbDeviceInfo vo) {
        if (vo == null) {
            return null;
        }
        DeviceInfo dto = DeviceInfoMapper.M.toDto(vo);
        fillDeviceInfo(vo.getDeviceId(), vo, dto);
        return dto;
    }

    /**
     * 将数据库中查出来的vo列表转为dto列表
     */
    private List<DeviceInfo> parseVoToDto(List<TbDeviceInfo> vos) {
        return vos.stream().map(d -> {
            DeviceInfo dto = DeviceInfoMapper.M.toDto(d);
            fillDeviceInfo(d.getDeviceId(), d, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public DeviceInfo findByProductKeyAndDeviceName(String productKey, String deviceName) {
        return parseVoToDto(deviceInfoRepository.findByProductKeyAndDeviceName(productKey, deviceName));
    }

    @Override
    public List<DeviceInfo> findByParentId(String parentId) {
        return parseVoToDto(deviceInfoRepository.findByParentId(parentId));
    }

    @Override
    public List<String> findSubDeviceIds(String parentId) {
        return jdbcTemplate.queryForList(
                "select device_id from device_info " +
                        "where parent_id=?", String.class, parentId);
    }

    @Override
    public List<DeviceInfo> findByDeviceName(String deviceName) {
        return parseVoToDto(deviceInfoRepository.findByDeviceName(deviceName));
    }

    @Override
    public Paging<DeviceInfo> findByConditions(String uid, String subUid,
                                               String productKey, String groupId,
                                               String state, String keyword,
                                               int page, int size) {
        String sql = "SELECT\n" +
                "a.id,\n" +
                "a.device_id,\n" +
                "a.product_key,\n" +
                "a.device_name,\n" +
                "a.model,\n" +
                "a.secret,\n" +
                "a.parent_id,\n" +
                "a.uid,\n" +
                "a.state,\n" +
                "a.online_time,\n" +
                "a.offline_time,\n" +
                "a.create_at\n" +
                "FROM device_info a ";

        if (StringUtils.isNotBlank(groupId)) {
            sql += " JOIN device_group_mapping b  on a.device_id=b.device_id\n" +
                    " JOIN device_group c on b.group_id=c.id ";
        }
        if (StringUtils.isNotBlank(subUid)) {
            sql += " JOIN device_sub_user d on d.device_id=a.device_id ";
        }

        List<Object> args = new ArrayList<>();
        sql += " where 1=1 ";
        if (StringUtils.isNotBlank(groupId)) {
            sql += "and c.id=? ";
            args.add(groupId);
        }

        if (StringUtils.isNotBlank(subUid)) {
            sql += "and d.uid=? ";
            args.add(subUid);
        } else if (StringUtils.isNotBlank(uid)) {
            sql += "and a.uid=? ";
            args.add(uid);
        }

        if (StringUtils.isNotBlank(productKey)) {
            sql += "and a.product_key=? ";
            args.add(productKey);
        }

        if (StringUtils.isNotBlank(state)) {
            sql += "and a.state=? ";
            args.add(state);
        }

        if (StringUtils.isNotBlank(keyword)) {
            keyword = "%" + keyword.trim() + "%";
            sql += "and (a.device_id like ? or a.device_name like ?) ";
            args.add(keyword);
            args.add(keyword);//两个参数
        }

        sql += String.format("order by create_at desc limit %d,%d", (page - 1) * size, size);

        List<DeviceInfo> list = jdbcTemplate.query(sql, (rs, rowNum) -> DeviceInfo.builder()
                .id(rs.getString("id"))
                .deviceId(rs.getString("device_id"))
                .deviceName(rs.getString("device_name"))
                .productKey(rs.getString("product_key"))
                .model(rs.getString("model"))
                .secret(rs.getString("secret"))
                .parentId(rs.getString("parent_id"))
                .uid(rs.getString("uid"))
                .state(new DeviceInfo.State(
                        "online".equals(rs.getString("state")),
                        rs.getLong("online_time"),
                        rs.getLong("offline_time")
                ))
                .createAt(rs.getLong("create_at"))
                .build(), args.toArray());

        sql = sql.replaceAll("SELECT[\\s\\S]+FROM", "SELECT count(*) FROM ");
        sql = sql.replaceAll("order by create_at desc limit.*", "");
        Long total = jdbcTemplate.queryForObject(sql, Long.class, args.toArray());

        //把当前页的deviceId串连起来作为in的参数
        String deviceIds = list.stream().map(d -> "'" + d.getDeviceId() + "'").collect(Collectors.joining(","));

        //取设备所属分组
        List<DeviceIdGroup> groups = list.size() == 0 ? new ArrayList<>() :
                jdbcTemplate.query("SELECT \n" +
                        "a.id,\n" +
                        "a.`name`, \n" +
                        "b.device_id as deviceId \n" +
                        "FROM\n" +
                        "device_group a \n" +
                        "JOIN device_group_mapping b on a.id=b.group_id\n" +
                        String.format("WHERE b.device_id in(%s)", deviceIds), new BeanPropertyRowMapper<>(DeviceIdGroup.class));

        //取设备标签
        List<TbDeviceTag> tags = list.size() == 0 ? new ArrayList<>() :
                jdbcTemplate.query("\n" +
                        "SELECT\n" +
                        "a.id,\n" +
                        "a.`code`,\n" +
                        "a.`name`,\n" +
                        "a.`value`\n" +
                        "FROM device_tag a " +
                        String.format("WHERE a.device_id IN(%s)", deviceIds), new BeanPropertyRowMapper<>(TbDeviceTag.class));

        for (DeviceInfo device : list) {
            //设置设备分组
            Map<String, DeviceInfo.Group> groupMap = new HashMap<>();
            groups.stream().filter(g -> device.getDeviceId().equals(g.getDeviceId()))
                    .forEach(g -> groupMap.put(g.getId(),
                            new DeviceInfo.Group(g.getId(), g.getName())));
            device.setGroup(groupMap);

            //设置设备标签
            Map<String, DeviceInfo.Tag> tagMap = new HashMap<>();
            tags.stream().filter(t -> device.getDeviceId().equals(t.getDeviceId()))
                    .forEach(t -> tagMap.put(t.getCode(),
                            new DeviceInfo.Tag(t.getCode(), t.getName(), t.getValue())));
            device.setTag(tagMap);
        }

        return new Paging<>(total, list);
    }

    @Override
    public void updateTag(String deviceId, DeviceInfo.Tag tag) {
        TbDeviceTag deviceTag = deviceTagRepository.findByDeviceIdAndCode(deviceId, tag.getId());
        if (deviceTag != null) {
            deviceTag.setName(tag.getName());
            deviceTag.setValue(tag.getValue());
            deviceTagRepository.save(deviceTag);
        } else {
            deviceTagRepository.save(
                    TbDeviceTag.builder()
                            .id(UUID.randomUUID().toString())
                            .code(tag.getId())
                            .deviceId(deviceId)
                            .name(tag.getName())
                            .value(tag.getValue())
                            .build()
            );
        }
    }

    @Override
    public List<DataItem> getDeviceStatsByCategory(String uid) {
        return null;
    }

    @Override
    public long countByGroupId(String groupId) {
        return deviceGroupMappingRepository.countByGroupId(groupId);
    }

    @Override
    @Transactional
    public void addToGroup(String deviceId, DeviceInfo.Group group) {
        String groupId = UUID.randomUUID().toString();
        deviceGroupMappingRepository.save(new TbDeviceGroupMapping(groupId, deviceId, group.getId()));

        //更新设备数量
        updateGroupDeviceCount(groupId);
    }

    private void updateGroupDeviceCount(String groupId) {
        //更新设备数量
        TbDeviceGroup deviceGroup = deviceGroupRepository.findById(groupId).orElse(null);
        if (deviceGroup != null) {
            deviceGroup.setDeviceQty((int) countByGroupId(groupId));
            deviceGroupRepository.save(deviceGroup);
        }
    }

    @Override
    public void updateGroup(String groupId, DeviceInfo.Group group) {
        //更新设备信息中的分组信息，关系数据库中不需要实现
    }

    @Override
    @Transactional
    public void removeGroup(String deviceId, String groupId) {
        jdbcTemplate.update("delete from device_group_mapping " +
                "where device_id=? and group_id=?", deviceId, groupId);
        //更新设备数量
        updateGroupDeviceCount(groupId);
    }

    @Override
    @Transactional
    public void removeGroup(String groupId) {
        jdbcTemplate.update("delete from device_group_mapping " +
                "where group_id=?", groupId);
        //更新设备数量
        updateGroupDeviceCount(groupId);
    }

    @Override
    public List<DeviceInfo> findByUid(String uid) {
        return new ArrayList<>();
    }

    @Override
    public Paging<DeviceInfo> findByUid(String uid, int page, int size) {
        return new Paging<>();
    }

    @Override
    public long countByUid(String uid) {
        return 0;
    }

    @Override
    public DeviceInfo findById(String s) {
        return DeviceInfoMapper.M.toDto(
                deviceInfoRepository.findById(s).orElse(null));
    }

    @Override
    @Transactional
    public DeviceInfo save(DeviceInfo data) {
        TbDeviceInfo vo = deviceInfoRepository.findByDeviceId(data.getDeviceId());
        if (StringUtils.isBlank(data.getId())) {
            data.setId(UUID.randomUUID().toString());
        }
        if (vo == null) {
            vo = new TbDeviceInfo();
        }

        ReflectUtil.copyNoNulls(data, vo);
        //状态转换
        parseStateToVo(data, vo);
        //保存设备信息
        deviceInfoRepository.save(vo);

        //设备分组转换
        Map<String, DeviceInfo.Group> groupMap = data.getGroup();
        groupMap.forEach((id, group) -> {
            TbDeviceGroupMapping mapping = deviceGroupMappingRepository.findByDeviceIdAndGroupId(data.getDeviceId(), id);
            if (mapping == null) {
                //保存设备分组与设备对应关系
                deviceGroupMappingRepository.save(new TbDeviceGroupMapping(
                        UUID.randomUUID().toString(),
                        data.getDeviceId(),
                        id
                ));
            }
        });

        return data;
    }

    @Override
    public DeviceInfo add(DeviceInfo data) {
        data.setCreateAt(System.currentTimeMillis());
        return save(data);
    }

    @Override
    public void deleteById(String s) {
        deviceInfoRepository.deleteById(s);
    }

    @Override
    public long count() {
        return deviceInfoRepository.count();
    }

    @Override
    public List<DeviceInfo> findAll() {
        return new ArrayList<>();
    }

    @Override
    public Paging<DeviceInfo> findAll(int page, int size) {
        Page<TbDeviceInfo> paged = deviceInfoRepository.findAll(Pageable.ofSize(size).withPage(page - 1));
        return new Paging<>(paged.getTotalElements(), parseVoToDto(paged.getContent()));
    }

}
