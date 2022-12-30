package jdbc.sql;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlHandler {
    private static String sql;
    private static Class<?> sqlType;

    private static final List<String> order=new ArrayList<>();

    private static final List<Object> sqlOrder=new ArrayList<>();

    public static String getSql(Method method,Object[] args){
        order.clear();
        sqlOrder.clear();
        Map<String,Object> map=new HashMap<>();

        Annotation[] annotations = method.getAnnotations();
        for (Annotation a: annotations) {
            if(a instanceof Insert) {
                sql = ((Insert) a).value();
                sqlType= Update.class;
            } else if (a instanceof Update) {
                sql = ((Update) a).value();
                sqlType= Update.class;
            } else if (a instanceof Select) {
                sql = ((Select) a).value();
                sqlType= Select.class;
            } else if (a instanceof Delete) {
                sql = ((Delete) a).value();
                sqlType= Update.class;
            }
        }

        Pattern pattern = Pattern.compile("#\\{([^}]+)}");
        Matcher matcher = pattern.matcher(sql);

        while (matcher.find()) {
            // 获取第一组匹配结果
            String s = matcher.group(1);
            order.add(s);
        }

        String para;

        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        int i=0;
        for (Annotation[] a: parameterAnnotations) {
            for (Annotation annotation : a) {
                if(annotation instanceof Para){
                    para=((Para) annotation).value();
                    String s = "#\\{" + para + "}";
                    sql = sql.replaceAll(s, "?");
                    map.put(para,args[i]);
                } else if (annotation instanceof ObjectPara) {
                    for (Object arg : args) {
                        Field[] fields = arg.getClass().getDeclaredFields();
                        for (Field field : fields) {
                            String name = field.getName();
                            String s = "#\\{" + name + "}";
                            sql = sql.replaceAll(s, "?");
                        }
                    }
                    map.put(((ObjectPara) annotation).value(),args[0]);
                    sqlOrder.add(args[0]);
                    return sql;
                }
            }
            i++;
        }


        for (String s : order) {
            sqlOrder.add(map.get(s));
        }

        return sql;
    }

    public static List<String> getOrder(){
        return order;
    }

    public static Class<?> getType() {
        return sqlType;
    }

    public static List<Object> getSqlOrder(){
        return sqlOrder;
    }
}
