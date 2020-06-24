package com.test.blog.service.impl;

import com.test.blog.mapper.CommentMapper;
import com.test.blog.pojo.Comment;
import com.test.blog.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CommentServiceImpl implements CommentService {

    private Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public List<Comment> selectCommentsByParentId(Long pid) {
        return commentMapper.selectCommentsByParentId(pid);
    }

    @Override
    public List<Comment> findByBlogIdAndParentCommentNull(Long blogId) {
        return commentMapper.findByBlogIdAndParentCommentNull(blogId);
    }

    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        List<Comment> comments = commentMapper.findByBlogIdAndParentCommentNull(blogId);

        return eachComment(comments);
    }

    @Override
    public void saveComment(Comment comment) {
        Long parentCommentId = comment.getParentComment().getId();
        if (parentCommentId !=-1){
            comment.setParentComment(commentMapper.selectCommentById(parentCommentId));
        }else{
            comment.setParentComment(null);
        }
        comment.setCreateTime(new Date());
        logger.info(comment.toString() +"is inserted");
        commentMapper.saveComment(comment);
    }

    @Override
    public Comment selectCommentById(Long id) {
        return commentMapper.selectCommentById(id);
    }

    @Override
    public void deleteCommentsByBlogId(Long id) {
        commentMapper.deleteCommentsByBlogId(id);
    }


    /**
     * 循环每个顶级的评论节点
     * @param comments
     * @return
     */
    private List<Comment> eachComment(List<Comment> comments) {
        List<Comment> commentsView = new ArrayList<>();
        for (Comment comment : comments) {
            Comment c = new Comment();
            BeanUtils.copyProperties(comment,c);
            commentsView.add(c);
        }
        //合并评论的各层子代到第一级子代集合中
        combineChildren(commentsView);
        return commentsView;
    }

    /**
     *
     * @param comments root根节点，blog不为空的对象集合
     * @return
     */
    private void combineChildren(List<Comment> comments) {
        if (comments==null){
            logger.warn("ERROR: comments is null");
            return;
        }
        comments.sort((l,r)->{
            if (l.getId() > r.getId()){
                return 1;
            }
            return -1;
        });
//        得到所有评论信息，comments均为根结点
        if (comments ==null || comments.isEmpty()){
            return;
        }
        List<Comment> comments1 = commentMapper.listCommentByBlogId(comments.get(0).getBlog().getId());
        try {
            comments1.sort((l,r)-> {
                    if (l.getId() > r.getId()) {
                        return 1;
                    }
                    return -1;
                });
        }catch (IndexOutOfBoundsException e){
            logger.warn("this blog doesn't has comments");
            e.printStackTrace();
        }
        ConcurrentHashMap<Long, ArrayList<Comment>> map = new ConcurrentHashMap<>();
//        需要确保ID按照顺序排序。。还不如直接i++
        int tempI =0;
        for (Comment comment : comments) {
            if (comment.getParentId() ==-1){
                map.put(comment.getId(),new ArrayList<>());
            }
        }
        for (Comment comment : comments1) {
            if (comment.getParentId() ==-1){
                continue;
            }
            Comment temp =comment;
//            map遍历时插入数据报错，Con
            if (map.get(temp.getParentId())==null){
                map.forEach((k,y)->{
                    if (y !=null) {
                        for (int i=0;i<y.size();i++) {
                            if (y.get(i).getId() == temp.getParentId()) {
                                y.add(temp);
                                break;
                            }
                        }
                    }
                });
            }else{
                map.get(comment.getParentId()).add(comment);
            }
        }
        map.forEach((k,v)->{
            for (int i=0;i<comments.size();i++){
                if (comments.get(i).getId() == k){
                    comments.get(i).setReplyComments(v);
                }
            }
        });
    }

    //存放迭代找出的所有子代的集合
    private List<Comment> tempReplys = new ArrayList<>();
    /**
     * 递归迭代，剥洋葱
     * @param comment 被迭代的对象
     * @return
     */
    private void recursively(Comment comment) {
        tempReplys.add(comment);//顶节点添加到临时存放集合
        if (comment.getReplyComments().size()>0) {
            List<Comment> replys = comment.getReplyComments();
            for (Comment reply : replys) {
                tempReplys.add(reply);
                if (reply.getReplyComments().size()>0) {
                    recursively(reply);
                }
            }
        }
    }
}
