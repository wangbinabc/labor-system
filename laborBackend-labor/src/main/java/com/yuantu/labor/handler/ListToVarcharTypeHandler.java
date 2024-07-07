package com.yuantu.labor.handler;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(List.class)
public class ListToVarcharTypeHandler implements TypeHandler<List<Long>> {
    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, List<Long> longs, JdbcType jdbcType) throws SQLException {
        // 遍历List类型的入参，拼装为String类型，使用Statement对象插入数据库
        StringBuffer sb = new StringBuffer();
        for (int j = 0; j < longs.size(); j++) {
            if (j == longs.size() - 1) {
                sb.append(longs.get(j));
            } else {
                sb.append(longs.get(j)).append(",");
            }
        }
        preparedStatement.setString(i, sb.toString());
    }

    @Override
    public List<Long> getResult(ResultSet resultSet, String s) throws SQLException {
        // 获取String类型的结果，使用","分割为List后返回
        String resultString = resultSet.getString(s);

        if (StringUtils.isNotEmpty(resultString)) {
            List<String> strings = Arrays.asList(resultString.split(","));
            List<Long> longs = new ArrayList<>();
            for (String ss : strings) {
                longs.add(Long.valueOf(ss));
            }
            return longs;
        }
        return null;
    }

    @Override
    public List<Long> getResult(ResultSet resultSet, int i) throws SQLException {
        // 获取String类型的结果，使用","分割为List后返回
        String resultString = resultSet.getString(i);
        if (StringUtils.isNotEmpty(resultString)) {
            List<String> strings = Arrays.asList(resultString.split(","));
            List<Long> longs = new ArrayList<>();
            for (String ss : strings) {
                longs.add(Long.valueOf(ss));
            }
            return longs;
        }
        return null;
    }


    @Override
    public List<Long> getResult(CallableStatement callableStatement, int i) throws SQLException {
        // 获取String类型的结果，使用","分割为List后返回
        String resultString = callableStatement.getString(i);
        if (StringUtils.isNotEmpty(resultString)) {
            List<String> strings = Arrays.asList(resultString.split(","));
            List<Long> longs = new ArrayList<>();
            for (String ss : strings) {
                longs.add(Long.valueOf(ss));
            }
            return longs;
        }
        return null;
    }
}

