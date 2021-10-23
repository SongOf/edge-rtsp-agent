package com.iot.agent.model;

import lombok.Data;
import java.io.Serializable;

/**
 * @author SongOf
 * @ClassName KafkaMessage
 * @Description
 * @Date 2021/10/21 20:17
 * @Version 1.0
 */
@Data
public class KafkaMessage implements Serializable {
    private static final long serialVersionUID = 37497756327443964L;

    private byte[] pix;
    private Integer channels;
    private Integer rows;
    private Integer cols;
}
