package com.iot.agent.dto;

import lombok.Data;
import java.io.Serializable;

/**
 * @author SongOf
 * @ClassName AlertDto
 * @Description
 * @Date 2021/10/22 0:16
 * @Version 1.0
 */
@Data
public class AlertDto implements Serializable {
    private String topic;
    private Long cameraId;
    private String url;
}
