package com.lhcygan.chatbibackend.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JsonUtils {

    public static String addQuotesToKeys(String jsonString) {
        jsonString = jsonString.replace("\\n", "").replace("\\\"", "\"").
                replace("option =", "").replace(";", "");
        JSONObject jsonObject = JSON.parseObject(jsonString);
        String result = JSON.toJSONString(jsonObject, SerializerFeature.QuoteFieldNames);
        return result;
    }

    public static void main(String[] args) {
        String jsonStr = " {    title: {        text: '网站用户增长情况'    },    tooltip: {},    xAxis: {        data: ['10', '15', '20']    },    yAxis: {},    series: [{        name: '用户数量',        type: 'line',        data: [50, 60, 70]    }]}";
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        String result = JSON.toJSONString(jsonObject, SerializerFeature.QuoteFieldNames);
        System.out.println(result);
    }
}
