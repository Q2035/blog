package top.hellooooo.blog;

import org.junit.jupiter.api.Test;
import top.hellooooo.blog.pojo.Comment;
import top.hellooooo.blog.util.CommentConvertor;

import java.util.Arrays;

/**
 * @author Q
 * @date 7/9/2022 21:06
 */
public class CommentTest {

    @Test
    public void testConvertComments() {
        Comment c1 = new Comment();
        c1.setId(1L);
        c1.setParentId(null);

        Comment c2 = new Comment();
        c2.setId(2L);
        c2.setParentId(1L);

        Comment c3 = new Comment();
        c3.setId(3L);
        c3.setParentId(1L);

        Comment c4 = new Comment();
        c4.setId(4L);
        c4.setParentId(2L);

        Comment c6 = new Comment();
        c6.setId(6L);
        c6.setParentId(2L);

        Comment c7 = new Comment();
        c7.setId(7L);
        c7.setParentId(3L);

        Comment c8 = new Comment();
        c8.setId(8L);
        c8.setParentId(3L);

        CommentConvertor.convertStraightforwardList(Arrays.asList(
                c1, c2, c3, c4, c6, c7, c8
        ));
    }

}
