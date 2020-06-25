package com.zlm.hello.spring.cloud.alibaba.nacos.provider4.utils;

import org.apache.commons.lang3.StringUtils;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;


public class NumberParser {
    public static List<Long> toLongs(String nums) {
        if (StringUtils.isEmpty(nums)) {
            return Collections.emptyList();
        }
        String[] numTokens = nums.split(",");
        List<Long> numbers = new ArrayList<Long>(numTokens.length);
        for (String token : numTokens) {
            numbers.add(Long.valueOf(token));
        }
        return numbers;
    }

    public static Long parseLong(Object obj) {
        if (obj == null) {
            return null;
        } else if (obj instanceof Number) {
            return ((Number) obj).longValue();
        } else {
            Long val = null;
            try {
                val = Long.parseLong(obj.toString());
            } catch (Exception e) {

            }
            return val;
        }
    }

    public static Long parseLong(Object obj, Long defaultValue) {
        if (obj == null) {
            return defaultValue;
        } else if (obj instanceof Number) {
            return ((Number) obj).longValue();
        } else {
            Long val = null;
            try {
                val = Long.parseLong(obj.toString());
            } catch (Exception e) {
                val = defaultValue;
            }
            return val;
        }

    }

    public static Integer parseInteger(Object obj) {
        if (obj == null) {
            return null;
        } else if (obj instanceof Number) {
            return ((Number) obj).intValue();
        } else {
            Integer val = null;
            try {
                val = Integer.parseInt(obj.toString());
            } catch (Exception e) {

            }
            return val;
        }
    }

    public static List<Integer> parseIntegerList(String str) {
        return parseIntegerList(str, ",");
    }

    public static List<Integer> parseIntegerList(String str, String regex) {
        List<Integer> list = new ArrayList<Integer>();
        if (StringUtils.isNotEmpty(str)) {
            String[] tokens = str.split(regex);
            for (String token : tokens) {
                Integer val = parseInteger(token);
                if (val != null) {
                    list.add(val);
                }
            }
        }
        return list;
    }

    public static String format(Object number, String pattern) {
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        return decimalFormat.format(number);
    }

    public static void main(String[] args) {
        System.out.println(format(0.1, "#.#####"));
        System.out.println(format(1, "#.#####"));
        System.out.println(format(1.1, "#.#####"));
        System.out.println(format(1.000005, "#.#####"));
    }

    private static Pattern PATTERN_FLOAT = Pattern.compile("([+-]?\\d[\\d,]*\\.\\d+)");
}

