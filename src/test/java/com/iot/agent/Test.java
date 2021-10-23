package com.iot.agent;

import org.opencv.core.Core;
import org.opencv.videoio.VideoCapture;

/**
 * @author SongOf
 * @ClassName Test
 * @Description
 * @Date 2021/10/22 16:22
 * @Version 1.0
 */
public class Test {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        VideoCapture vc = new VideoCapture(0);
    }
}
