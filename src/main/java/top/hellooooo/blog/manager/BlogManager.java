package top.hellooooo.blog.manager;

import top.hellooooo.blog.pojo.Blog;

import java.util.List;

/**
 * @author Q
 * @date 6/27/2022 3:27 PM
 */
public interface BlogManager {

    /**
     * 列举无需正文的博客信息
     * @param start
     * @param end
     * @return
     */
    List<Blog> listBlogWithoutContent(int start, int end);
}
