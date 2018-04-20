package Grib2dat.test;

/**
 * Created by Administrator on 2018/4/17.
 * http://www.runoob.com/java/java-regular-expressions.html
 */
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class RegexMatches正则匹配
{
    public static void main( String args[] ){
        String content = "I am noob " +
                "from runoob.com.";
        String pattern1 = ".*runoob.*";
        boolean isMatch = Pattern.matches(pattern1, content);
        System.out.println("字符串中是否包含了 'runoob' 子字符串? " + isMatch);

        // 按指定模式在字符串查找
        String line = "This order was333 placed for QT3000! OK?";
        String pattern = "(\\D*)(\\d+)(.*)(was)";
        // 创建 Pattern 对象
        Pattern r = Pattern.compile(pattern);
        // 现在创建 matcher 对象
        Matcher m = r.matcher(line);
        if (m.find( )) {
            System.out.println("Found value: " + m.group(0) );
            System.out.println("Found value: " + m.group(1) );
            System.out.println("Found value: " + m.group(2) );
            System.out.println("Found value: " + m.group(3) );
        } else {
            System.out.println("NO MATCH");
        }
    }
}