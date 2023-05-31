package cc.iotkit.data.service;

import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.common.utils.StringUtils;
import cc.iotkit.data.dao.SysPostRepository;
import cc.iotkit.data.model.TbSysPost;
import cc.iotkit.data.system.ISysPostData;
import cc.iotkit.data.util.PredicateBuilder;
import cc.iotkit.model.system.SysPost;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

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

    @Override
    public List<SysPost> selectPostList(SysPost post) {
        List<TbSysPost> ret=jpaQueryFactory.selectFrom(tbSysPost).where(PredicateBuilder.instance()
                .and(StringUtils.isNotBlank(post.getPostCode()),()->tbSysPost.postCode.like(post.getPostCode()))
                .and(StringUtils.isNotBlank(post.getPostName()),()->tbSysPost.postName.like(post.getPostName()))
                .and(StringUtils.isNotBlank(post.getStatus()),()->tbSysPost.status.eq(post.getStatus())).build())
                .orderBy(tbSysPost.postSort.asc()).fetch();
        return MapstructUtils.convert(ret,SysPost.class);
    }

    @Override
    public boolean checkPostNameUnique(SysPost post) {
        final TbSysPost ret = jpaQueryFactory.selectFrom(tbSysPost)
                .where(PredicateBuilder.instance()
                        .and(tbSysPost.postName.eq(post.getPostName()))
                        .and(Objects.nonNull(post.getId()), () -> tbSysPost.id.eq(post.getId()))
                        .build()).fetchOne();
        return Objects.isNull(ret);
    }

    @Override
    public boolean checkPostCodeUnique(SysPost post) {
        final TbSysPost ret = jpaQueryFactory.selectFrom(tbSysPost)
                .where(PredicateBuilder.instance()
                        .and(tbSysPost.postCode.eq(post.getPostCode()))
                        .and(Objects.nonNull(post.getId()), () -> tbSysPost.id.eq(post.getId()))
                        .build()).fetchOne();
        return Objects.isNull(ret);
    }

    @Override
    public SysPost findById(Long aLong) {
        return MapstructUtils.convert(postRepository.findById(aLong),SysPost.class);
    }

    @Override
    public SysPost save(SysPost data) {
        postRepository.save(data.to(TbSysPost.class));
        return data;
    }

    @Override
    public void deleteByIds(Collection<Long> longs) {
        postRepository.deleteAllByIdInBatch(longs);
    }
}