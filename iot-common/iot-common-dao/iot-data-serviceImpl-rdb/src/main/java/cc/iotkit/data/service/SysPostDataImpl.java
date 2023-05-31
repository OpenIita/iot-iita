package cc.iotkit.data.service;

import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.data.dao.SysPostRepository;
import cc.iotkit.data.system.ISysPostData;
import cc.iotkit.data.util.PredicateBuilder;
import cc.iotkit.model.system.SysPost;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

import static cc.iotkit.data.model.QTbSysPost.tbSysPost;
import static cc.iotkit.data.model.QTbSysUser.tbSysUser;
import static cc.iotkit.data.model.QTbSysUserPost.tbSysUserPost;

/**
 * @Author：tfd
 * @Date：2023/5/30 18:20
 */
@Primary
@Service
@RequiredArgsConstructor
public class SysPostDataImpl implements ISysPostData {

    private SysPostRepository postRepository;


    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<SysPost> findAll() {
        return MapstructUtils.convert(postRepository.findAll(),SysPost.class);
    }

    @Override
    public List<Long> selectPostListByUserId(Long userId) {
        return jpaQueryFactory.select(Projections.bean(Long.class, tbSysPost.id))
                .from(tbSysPost)
                .leftJoin(tbSysUserPost).on(tbSysUserPost.postId.eq(tbSysPost.id))
                .leftJoin(tbSysUser).on(tbSysUser.id.eq(tbSysUserPost.userId))
                .where(PredicateBuilder.instance()
                        .and(tbSysUser.id.eq(userId))
                        .build()).fetch();
    }
}
