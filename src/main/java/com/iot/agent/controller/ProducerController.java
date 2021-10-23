package com.iot.agent.controller;

import com.iot.agent.dto.AlertDto;
import com.iot.agent.model.KafkaMessage;
import com.iot.agent.transfer.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author toutou 需要用定时任务管理器优化 也需要服务管理(心跳包活)
 * @date by 2019/08
 */
@RestController
public class ProducerController {

    @Autowired
    private KafkaTemplate<String, KafkaMessage> kafkaTemplate;

    @Autowired
    private TransferService transferService;

    @RequestMapping("message/send")
    public String startTransferRtspStream(AlertDto alertDto){
        KafkaMessage kafkaMessage = new KafkaMessage();
        kafkaMessage.setPix(new byte[1024*1024*5]);
        kafkaMessage.setCols(300);
        kafkaMessage.setRows(400);
        kafkaMessage.setChannels(2);
        kafkaTemplate.send("demo", kafkaMessage); //使用kafka模板发送信息
        return "success";
    }
}
