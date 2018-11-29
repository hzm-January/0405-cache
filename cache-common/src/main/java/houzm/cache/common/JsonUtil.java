package houzm.cache.common;

import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Package: houzm.cache.common
 * Author: houzm
 * Date: Created in 2018/10/20 13:53
 * Copyright: Copyright (c) 2018
 * Version: 0.0.1
 * Modified By:
 * Description： json util
 */
public class JsonUtil {
    private static Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    /**
     * @Fields MAPPER : jackson mapper
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        /**
         * 是否允许一个类型没有注解表明打算被序列化。默认true，抛出一个异常；否则序列化一个空对象，比如没有任何属性。 Note that
         * empty types that this feature has only effect on those "empty" beans
         * that do not have any recognized annotations (like
         * <code>@JsonSerialize</code>): ones that do have annotations do not
         * result in an exception being thrown.
         *
         * @since 1.4
         */
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false); // 允许有空对象要封装
        /**
         * 该特性决定了当遇到未知属性（没有映射到属性，没有任何setter或者任何可以处理它的handler），是否应该抛出一个
         * JsonMappingException异常。这个特性一般式所有其他处理方法对未知属性处理都无效后才被尝试，属性保留未处理状态。
         *
         * 默认情况下，该设置是被打开的。
         *
         * @since 1.2
         */
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false); // 遇到未知属性，不抛出异常
        /**
         * 该特性可以允许接受所有引号引起来的字符，使用‘反斜杠\’机制：如果不允许，只有JSON标准说明书中 列出来的字符可以被避开约束。
         * 由于JSON标准说明中要求为所有控制字符使用引号，这是一个非标准的特性，所以默认是关闭的。
         *
         * 注意：一般在设置ALLOW_SINGLE_QUOTES属性时，
         * 也设置了ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER属性，
         * 所以，有时候，你会看到不设置ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER为true，但是依然可以正常运行。
         *
         * @since 1.6
         */
        MAPPER.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);

        // MAPPER.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS,true);
        /**
         * jackson 实体转json属性为NULL或者为空不参加序列化 方法一：
         * 通过该方法对mapper对象进行设置，所有序列化的对象都将按改规则进行系列化 Include.Include.ALWAYS 默认
         * Include.NON_DEFAULT 属性为默认值不序列化 Include.NON_EMPTY 属性为 空（“”） 或者为 NULL
         * 都不序列化 Include.NON_NULL 属性为NULL 不序列化 方法二：
         *
         * @JsonInclude(Include.NON_NULL) 将该标记放在属性上，如果该属性为NULL则不参与序列化
         *                                如果放在类上边,那对这个类的全部属性起作用
         *                                Include.Include.ALWAYS 默认
         *                                Include.NON_DEFAULT 属性为默认值不序列化
         *                                Include.NON_EMPTY 属性为 空（“”） 或者为 NULL
         *                                都不序列化 Include.NON_NULL 属性为NULL 不序列化
         */
        // MAPPER.setSerializationInclusion(Include.NON_NULL);
    }

    /**
     * 将对象转换成json字符串。
     * <p>
     * Title: pojoToJson
     * </p>
     * <p>
     * Description:
     * </p>
     *
     * @param data
     * @return
     */
    public static String objectToJson(Object data) {
        try {
            String string = MAPPER.writeValueAsString(data);
            return string;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            logger.error("Parse object to json failed!", e);
        }
        return null;
    }

    /**
     * 将json结果集转化为对象
     *
     * @param jsonData
     *            json数据
     * @param beanType
     *            对象中的object类型
     * @return
     */
    public static <T> T jsonToPojo(String jsonData, Class<T> beanType) {
        try {
            T t = MAPPER.readValue(jsonData, beanType);
            return t;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Parse json to object failed!", e);
        }
        return null;
    }

    /**
     * 将json数据转换成pojo对象list
     * <p>
     * Title: jsonToList
     * </p>
     * <p>
     * Description:
     * </p>
     *
     * @param jsonData
     * @param beanType
     * @return
     */
    public static <T> List<T> jsonToList(String jsonData, Class<T> beanType) {
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(
                List.class, beanType);
        try {
            List<T> list = MAPPER.readValue(jsonData, javaType);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * objToJsonTree
     *
     * @param jsonstr
     * @return 转换结果jsonTree
     * @throws JsonProcessingException
     * @throws IOException
     */
    public static JsonNode jsonStrToJsonTree(String jsonstr)
            throws JsonProcessingException, IOException {
        return MAPPER.readTree(jsonstr);
    }
}
