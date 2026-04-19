package com.restaurant.service.impl;

import com.restaurant.service.AiService;
import com.restaurant.service.SystemService;
import com.restaurant.entity.SystemConfig;
import com.restaurant.entity.Orders;
import com.restaurant.entity.OrderDetail;
import com.restaurant.mapper.OrdersMapper;
import com.restaurant.mapper.OrderDetailMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

@Service
public class AiServiceImpl implements AiService {

    @Autowired
    private SystemService systemService;

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Override
    public String analyzeOrders() {
        SystemConfig config = systemService.getConfig();
        String apiKey = config.getAiApiKey();
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new RuntimeException("请先在前端配置 DeepSeek R1 API Key");
        }

        List<Orders> ordersList = ordersMapper.selectList(new QueryWrapper<Orders>().orderByDesc("create_time").last("LIMIT 10"));
        if (ordersList.isEmpty()) {
            return "当前没有任何订单，无需智能分析。";
        }

        StringBuilder dataBuilder = new StringBuilder();
        dataBuilder.append("最新订单数据如下：\n");
        for (Orders order : ordersList) {
            dataBuilder.append("订单号: ").append(order.getOrderNo())
                .append(", 总金额: ").append(order.getTotalAmount()).append("元\n");
            
            List<OrderDetail> details = orderDetailMapper.selectList(
                new QueryWrapper<OrderDetail>().eq("order_no", order.getOrderNo())
            );
            if (!details.isEmpty()) {
                dataBuilder.append("  |- 包含菜品: ");
                for (OrderDetail d : details) {
                    dataBuilder.append(d.getDishName()).append("x").append(d.getQuantity()).append(" ");
                }
                dataBuilder.append("\n");
            }
        }

        String prompt = "你是一位专业的餐饮经营管理专家。请根据以下最近10个订单的数据，帮我分析热销菜品、营业状况，并给出2到3条经营优化建议。\n\n" + dataBuilder;

        // 创建RestTemplate并设置超时时间
        RestTemplate restTemplate = new RestTemplate();
        // 设置超时时间
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(120000); // 连接超时120秒
        factory.setReadTimeout(120000);    // 读取超时120秒
        restTemplate.setRequestFactory(factory);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "deepseek-reasoner");
        
        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", "你是一位专业的餐饮专家，回答需使用Markdown格式。");
        messages.add(systemMessage);

        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", prompt);
        messages.add(userMessage);

        requestBody.put("messages", messages);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://api.deepseek.com/chat/completions",
                entity,
                Map.class
            );
            
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map<String, Object> choice = choices.get(0);
                    Map<String, Object> messageContent = (Map<String, Object>) choice.get("message");
                    return (String) messageContent.get("content");
                }
            }
            return "解析模型返回值失败";
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("调用 DeepSeek API 失败。请检查API Key是否正确：" + e.getMessage());
        }
    }
}
