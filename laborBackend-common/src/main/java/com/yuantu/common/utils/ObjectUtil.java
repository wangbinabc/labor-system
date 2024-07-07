package com.yuantu.common.utils;

import com.yuantu.common.exception.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author syw
 * @date 2022/11/4 16:08
 */
public class ObjectUtil {

    /**
     * 判断对象中部分属性值是否不为空
     *
     * @param object            对象
     * @param excludeFieldNames 选择忽略校验的属性名称List集合
     * @return boolean
     */
    public static boolean checkObjFieldsIsNotNull(Object object, List<String> excludeFieldNames) {
        if (null == object) {
            return false;
        }

        try {
            for (Field f : object.getClass().getDeclaredFields()) {
                ReflectionUtils.makeAccessible(f);
                if (!excludeFieldNames.contains(f.getName()) && f.get(object) != null && StringUtils.isNotBlank(f.get(object).toString())) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean areAllFieldsNull(Object obj) {
        try {
            Class<?> clazz = obj.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                ReflectionUtils.makeAccessible(field);
                if (field.get(obj) != null) {
                    return false; // 如果存在非空属性，返回 false
                }
            }
            return true; // 所有属性都为 null，返回 true
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new ServiceException("对象属性检测失败");
    }

}
