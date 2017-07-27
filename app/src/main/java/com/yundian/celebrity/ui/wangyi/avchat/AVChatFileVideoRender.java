package com.yundian.celebrity.ui.wangyi.avchat;


import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by liuqijun on 2/27/17.
 */
public class AVChatFileVideoRender  {

    private OutputStream outputStream;
    private int width;
    private int height;



    private boolean needCreateFile(int w, int h) {
        return outputStream == null || width != w || height != h;
    }

    private boolean createFile(int w, int h) {


        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        final String videoOutPath = Environment.getExternalStorageDirectory().getPath() + "/" ;
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(videoOutPath));
            outputStream.write(("YUV4MPEG2 C420 W" + w + " H" + h + " Ip F30:1 A1:1\n").getBytes());
            width = w;
            height = h;
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }


}
