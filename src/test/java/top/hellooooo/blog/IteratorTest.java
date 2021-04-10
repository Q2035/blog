package top.hellooooo.blog;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IteratorTest {
    @Test
    public void test01(){
        List<Integer> list = new ArrayList<>();
        list.add(12);
        list.add(24);
        list.add(54);
        list.add(543);
        list.add(12);
        Iterator<Integer> iterator = list.iterator();
        Integer t;
        while (iterator.hasNext()){
            t =iterator.next();
            if (t ==12){
                iterator.remove();
            }
        }
        System.out.println(list);
    }

    @Test
    public void testLetter(){
        String path = ".png";
        StringBuffer stringBuffer = new StringBuffer();
        int length = path.length();
        int dotInd = path.lastIndexOf(".");
        // 如果有点，则从点开始往前找字母/数字，如果没有就从尾部开始找
        if (dotInd == -1) {
            System.out.println(path);
            return;
        }
        char[] chars = path.toCharArray();
        boolean firstIn = true;
        while (dotInd >= 0) {
            // 放行"."
            if (firstIn) {
                firstIn = false;
                dotInd--;
                continue;
            }
            char aChar = chars[dotInd];
            // 为字母或数字
            if (Character.isLetter(aChar)
                    || Character.isDigit(aChar)) {

            } else {
                break;
            }
            dotInd--;
        }
        if (dotInd >= 0) {
            System.out.println(path.substring(dotInd + 1));
        } else {
            System.out.println(path);
        }
    }

    @Test
    public void testStringTrim(){
        List<String> list = Arrays.asList("https://www.hellooooo.top/image/1.png", "https://www.hellooooo.top/image/2.png", "https://www.hellooooo.top/image/3.png");
        list.forEach(url -> {
            System.out.println(url.trim());
        });
    }


    @Test
    public void testRegex(){
        // 按指定模式在字符串查找
        String line = "ads![image-20210305105526395](https://cdn.hellooooo.top/image/blog/2021/03/mysql_optimize/image-20210305105526395.png)123fafa\n" +
                "ad23s![imagsafe-20210305105526395](https://cdn.hellooooo.top/image/blog/2021/03/mysql_optimize/image-20210305105526395.png)2sf23 sa";
        // String pattern = ".*!\\[.*]\\(";
        String pattern = "!\\[.*]\\([a-zA-z]+://[^\\s]*\\)";
        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);
        // 现在创建 matcher 对象
        Matcher m = r.matcher(line);
        int matcher_start = 0;
        while (m.find(matcher_start)) {
            System.out.println(m.group(0));
            matcher_start = m.end();
        }
    }
}
