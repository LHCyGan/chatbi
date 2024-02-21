package com.lhcygan.chatbibackend.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JsonUtils {

    public static String addQuotesToKeys(String jsonString) {
        jsonString = jsonString.replace("\\n", "").replace("\\\"", "\"").
                replace("option =", "").replace(";", "").trim();
        if (!jsonString.startsWith("{"))
            jsonString = "{" + jsonString;
        if (!jsonString.endsWith("}"))
            jsonString = jsonString + "}";
        JSONObject jsonObject = JSON.parseObject(jsonString);
        String result = JSON.toJSONString(jsonObject, SerializerFeature.QuoteFieldNames);
        return result;
    }

    public static void main(String[] args) {
//        String jsonStr = '{"errorCode":"0","message":"success","data":"【【【【【\n{\n\"title\": {\n\"text\": \"网站用户增长情况\"\n},\n\"tooltip\": {},\n\"xAxis\": {\n\"type\": \"category\",\n\"data\": [\"10\", \"15\", \"20\"]\n},\n\"yAxis\": {\n\"type\": \"value\"\n},\n\"series\": [{\n\"data\": [50, 60, 70],\n\"type\": \"line\"\n}]}\n】】】】】\n\n明确的数据分析结论：从折线图中可以看出，网站用户在日期10、15、20的价格分别为50、60、70，呈现出稳定的增长趋势。"}";
//                System.out.println(JsonUtils.addQuotesToKeys(jsonStr));
    }
}
