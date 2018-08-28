package com.baosight.message.admin.controller;

import com.baosight.message.admin.persist.service.MessageBizTypeService;
import com.baosight.message.admin.persist.service.MessageInfoService;
import com.baosight.message.admin.persist.service.MessageSendService;
import com.baosight.message.admin.persist.service.MessageSourceService;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.influxdb.InfluxDBTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@Slf4j
public class IndexController {

    @Autowired
    MessageSourceService messageSourceService;

    @Autowired
    MessageBizTypeService messageBizTypeService;

    @Autowired
    MessageInfoService messageInfoService;

    @Autowired
    MessageSendService messageSendService;

    @Autowired
    InfluxDBTemplate<Point> influxDBTemplate;

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/")
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView();
        //查询
        int sourceAmount = messageSourceService.getAmount();
        int bizTypeAmount = messageBizTypeService.getAmount();
        int todayMessageAmount = messageInfoService.todayAmount();
        int todaySendAmount = messageSendService.todayAmount();
        mv.addObject("sourceAmount", sourceAmount);
        mv.addObject("bizTypeAmount", bizTypeAmount);
        mv.addObject("todayMessageAmount", todayMessageAmount);
        mv.addObject("todaySendAmount", todaySendAmount);
        mv.setViewName("index");
        return mv;
    }

    @RequestMapping("/stats/messageSending")
    @ResponseBody
    public Map<String, List<String>> messageSending() {
        String command = "SELECT count(\"receiver\") AS \"count\" FROM \"message_sending\" WHERE time > now() - 15m GROUP BY time(15s) FILL(0)";
        Query query = new Query(command, influxDBTemplate.getDatabase());
        QueryResult queryResult = influxDBTemplate.query(query);
        return convertQueryResult(queryResult);
    }

    @RequestMapping("/stats/messageSent")
    @ResponseBody
    public Map<String, List<String>> messageSent() {
        String command = "SELECT count(\"receiver\") AS \"count\" FROM \"message_sent\" WHERE time > now() - 15m GROUP BY time(15s) FILL(0)";
        Query query = new Query(command, influxDBTemplate.getDatabase());
        QueryResult queryResult = influxDBTemplate.query(query);
        return convertQueryResult(queryResult);
    }

    @RequestMapping("/testSend")
    @ResponseBody
    public String insertSend(){
        Point point = Point.measurement("message_sending")
                .tag("messageId", UUID.randomUUID().toString())
                .tag("messageBizType", "testBiz")
                .tag("messageSendType", "email")
                .tag("messageSource", "test")
                .addField("receiver", "cy")
                .addField("receiverAddress", "178333@baosight.com")
                .build();

        influxDBTemplate.write(point);
        return "xxx";
    }

    @RequestMapping("/testSent")
    @ResponseBody
    public String insertSent(){
        Point point = Point.measurement("message_sent")
                .tag("messageId", UUID.randomUUID().toString())
                .tag("messageBizType", "testBiz")
                .tag("messageSource", "test")
                .tag("sendStatus", "success")
                .addField("receiver", "cy")
                .build();
        influxDBTemplate.write(point);
        return "yyy";
    }

    private String formatUTC(String time, String utcPattern, String curPattern) throws ParseException {
        SimpleDateFormat utcDateFormat = new SimpleDateFormat(utcPattern);
        utcDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date utcDate = utcDateFormat.parse(time);
        SimpleDateFormat currentDateFormat = new SimpleDateFormat(curPattern);
        currentDateFormat.setTimeZone(TimeZone.getDefault());
        return currentDateFormat.format(utcDate);
    }

    private Map<String, List<String>> convertQueryResult(QueryResult queryResult){
        List<String> time = new ArrayList<>();
        List<String> value = new ArrayList<>();
        if(!CollectionUtils.isEmpty(queryResult.getResults().get(0).getSeries())){
            QueryResult.Series series = queryResult.getResults().get(0).getSeries().get(0);
            List<List<Object>> list = series.getValues();
            list.stream().forEach(s ->{
                String originTime = ObjectUtils.getDisplayString(s.get(0));
                String val = ObjectUtils.getDisplayString(s.get(1));
                try {
                    String formatTime = formatUTC(originTime, "yyyy-MM-dd'T'HH:mm:ss'Z'", "HH:mm:ss");
                    time.add(formatTime);
                    value.add(val);
                } catch (ParseException e) {
                    log.error(e.getMessage());
                }
            });
        }
        Map<String ,List<String>> map = new HashMap<>();
        map.put("time", time);
        map.put("count", value);
        return map;
    }
}
