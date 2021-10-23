package com.iot.agent.transfer;

import com.iot.agent.dto.AlertDto;
import com.iot.agent.model.KafkaMessage;
import com.iot.agent.utils.JacksonUtil;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import static org.opencv.imgcodecs.Imgcodecs.imencode;
import static org.opencv.videoio.Videoio.CAP_PROP_FRAME_WIDTH;

/**
 * @author SongOf
 * @ClassName Transfer
 * @Description
 * @Date 2021/10/22 16:25
 * @Version 1.0
 */
public class TransferService {
    public static void main(String[] args) {
        // Set properties used to configure the producer
        Properties properties = new Properties();
        // Set the brokers (bootstrap servers)
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");
        properties.setProperty("max.request.size", String.valueOf(1024*1024*10));
        // Set how to serialize key/value pairs
        properties.setProperty("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        properties.setProperty("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
        // specify the protocol for Domain Joined clusters
        //properties.setProperty(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");

        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.load("E:\\Opencv\\opencv\\build\\x64\\vc15\\bin\\opencv_world401.dll");
        System.load("E:\\Opencv\\opencv\\build\\bin\\opencv_ffmpeg401_64.dll");
        VideoCapture vc = new VideoCapture();
        boolean isOpen = vc.open("rtsp://admin:YHDYPD@192.168.1.105:554/h264/ch1/main/av_stream");

        System.out.println("isOpen="+isOpen);

        Mat mat = new Mat();
        String winName = "showFrame";
        int height = 600,width = 800;
        HighGui.namedWindow(winName);
        HighGui.resizeWindow(winName, width, height);
        while(vc.read(mat)){
            // 重置大小
            Mat dst = new Mat();
            Imgproc.resize(mat, dst, new Size(width,height));
            // 显示
            HighGui.imshow(winName, dst);
            // waitkey 必须要，否则无法显示
            int key = HighGui.waitKey(1);
            System.out.println("key="+key);
            //esc键退出
            if(key == 27){
                break;
            }
            //拿到了每帧之后要干嘛就是后面逻辑的事情了
            MatOfByte mob = new MatOfByte();
            imencode(".jpg", dst, mob);
            KafkaMessage kafkaMessage = new KafkaMessage();
            kafkaMessage.setPix(mob.toArray());
            kafkaMessage.setChannels(dst.channels());
            kafkaMessage.setCols(dst.cols());
            kafkaMessage.setRows(dst.rows());
            try {
                producer.send(new ProducerRecord<String, String>("edge_camera_185339746", JacksonUtil.toJson(kafkaMessage)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        HighGui.destroyAllWindows();
        vc.release();
    }
}
