package com.cloudTop.starshare.widget.audiorecorder;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaFormat;
import android.util.Log;

import com.liulishuo.engzo.lingorecorder.processor.AudioProcessor;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import static com.kiwi.tracker.common.Config.TAG;

/**
 * Created by wcw on 3/30/17.
 */

public class AndroidAACProcessor implements AudioProcessor {

    private String filePath;
    private MediaCodec codec;

    private FileOutputStream fos;
    private ByteBuffer[] encodeInputBuffers;
    private ByteBuffer[] encodeOutputBuffers;
    private MediaCodec.BufferInfo encodeBufferInfo;
    private BufferedOutputStream bos;

    public AndroidAACProcessor(String filePath) {
        this.filePath = filePath;
    }


    @Override
    public void start() {
//        try {
//            fos = new FileOutputStream(filePath);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        try {
            fos = new FileOutputStream(filePath);
            bos = new BufferedOutputStream(fos, 200 * 1024);
            //File file = new File(srcPath);
            //fileTotalSize=file.length();
        } catch (IOException e) {
            e.printStackTrace();
        }


//        MediaFormat format = new MediaFormat();
//        String mime = "audio/flac";
//        format.setString(MediaFormat.KEY_MIME, mime);
//        format.setInteger(MediaFormat.KEY_SAMPLE_RATE, 16000);
//        format.setInteger(MediaFormat.KEY_CHANNEL_COUNT, 1);
//
//        String componentName = getEncoderNamesForType(mime);
//        try {
//            codec = MediaCodec.createByCodecName(componentName);
//            codec.configure(
//                    format,
//                    null /* surface */,
//                    null /* crypto */,
//                    MediaCodec.CONFIGURE_FLAG_ENCODE);
//            codec.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        try {
            MediaFormat encodeFormat = MediaFormat.createAudioFormat(MediaFormat.MIMETYPE_AUDIO_AAC, 16000, 1);//参数对应-> mime type、采样率、声道数
            encodeFormat.setInteger(MediaFormat.KEY_BIT_RATE, 128 * 100);//比特率
            encodeFormat.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC);
            encodeFormat.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, 16*1024);
            codec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_AUDIO_AAC);
            codec.configure(encodeFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (codec == null) {
            Log.e(TAG, "create mediaEncode failed");
            return;
        }
        codec.start();

    }
//    public void flow(byte[] bytes, int size){
//        Log.d(TAG, "encode: " + presentationTimeUs);
//        if (!mIsOpened) {
//            return false;
//        }
//
//        try {
//            ByteBuffer[] inputBuffers = mMediaCodec.getInputBuffers();
//            int inputBufferIndex = mMediaCodec.dequeueInputBuffer(1000);
//            if (inputBufferIndex >= 0) {
//                ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
//                inputBuffer.clear();
//                inputBuffer.put(input);
//                mMediaCodec.queueInputBuffer(inputBufferIndex, 0, input.length, presentationTimeUs, 0);
//            }
//        } catch (Throwable t) {
//            t.printStackTrace();
//            return false;
//        }
//        Log.d(TAG, "encode -");
//        return true;
//    }


//
    @Override
    public void flow(byte[] bytes, int size) {
//        try {
//            ByteBuffer[] inputBuffers = codec.getInputBuffers();
//            ByteBuffer[] outputBuffers = codec.getOutputBuffers();
//            int inputBufferIndex = codec.dequeueInputBuffer(-1);
//            if (inputBufferIndex >= 0) {
//                ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
//                inputBuffer.clear();
//                inputBuffer.put(bytes);
//                codec.queueInputBuffer(inputBufferIndex, 0, size, 0, 0);
//            }
//
//            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
//            int outputBufferIndex = codec.dequeueOutputBuffer(bufferInfo, 0);
//
//            while (outputBufferIndex >= 0) {
//                ByteBuffer outputBuffer = outputBuffers[outputBufferIndex];
//                byte[] outData = new byte[bufferInfo.size];
//                outputBuffer.get(outData);
//                fos.write(outData, 0, outData.length);
//                LOG.d("FlacEncoder " + outData.length + " bytes written");
//
//                codec.releaseOutputBuffer(outputBufferIndex, false);
//                outputBufferIndex = codec.dequeueOutputBuffer(bufferInfo, 0);
//
//            }
//        } catch (Throwable t) {
//            t.printStackTrace();
//        }


        int inputIndex;
        ByteBuffer inputBuffer;
        int outputIndex;
        ByteBuffer outputBuffer;
        byte[] chunkAudio;
        int outBitSize;
        int outPacketSize;

        encodeInputBuffers = codec.getInputBuffers();
        encodeOutputBuffers = codec.getOutputBuffers();
        encodeBufferInfo = new MediaCodec.BufferInfo();

//        showLog("doEncode");
//        for (int i = 0; i < encodeInputBuffers.length - 1; i++) {
//            //获取解码器所在线程输出的数据 代码后边会贴上
//            if (bytes == null) {
//                break;
//            }
//            inputIndex = codec.dequeueInputBuffer(1000);//同解码器
//            inputBuffer = encodeInputBuffers[inputIndex];//同解码器
//            inputBuffer.clear();//同解码器
//            inputBuffer.limit(size);
//            inputBuffer.put(bytes);//PCM数据填充给inputBuffer
//            codec.queueInputBuffer(inputIndex, 0, size, System.nanoTime(), 0);//通知编码器 编码
//        }

        int inputBufferIndex = codec.dequeueInputBuffer(-1);
        if (inputBufferIndex >= 0) {
            inputBuffer = encodeInputBuffers[inputBufferIndex];
            inputBuffer.clear();
            inputBuffer.put(bytes);
            codec.queueInputBuffer(inputBufferIndex, 0, size, 0, 0);
        }

        outputIndex = codec.dequeueOutputBuffer(encodeBufferInfo, 10000);//同解码器
        while (outputIndex >= 0) {//同解码器
            outBitSize = encodeBufferInfo.size;
            outPacketSize = outBitSize + 7;//7为ADTS头部的大小
            outputBuffer = encodeOutputBuffers[outputIndex];//拿到输出Buffer
            outputBuffer.position(encodeBufferInfo.offset);
            outputBuffer.limit(encodeBufferInfo.offset + outBitSize);
            chunkAudio = new byte[outPacketSize];
            addADTStoPacket(chunkAudio, outPacketSize);//添加ADTS 代码后面会贴上
            outputBuffer.get(chunkAudio, 7, outBitSize);//将编码得到的AAC数据 取出到byte[]中 偏移量offset=7 你懂得
            outputBuffer.position(encodeBufferInfo.offset);
//                showLog("outPacketSize:" + outPacketSize + " encodeOutBufferRemain:" + outputBuffer.remaining());
            try {
                bos.write(chunkAudio, 0, chunkAudio.length);//BufferOutputStream 将文件保存到内存卡中 *.aac
            } catch (IOException e) {
                e.printStackTrace();
            }
            codec.releaseOutputBuffer(outputIndex, false);
            outputIndex = codec.dequeueOutputBuffer(encodeBufferInfo, 10000);

        }
    }

    /**
     * 添加ADTS头
     *
     * @param packet
     * @param packetLen
     */
    private void addADTStoPacket(byte[] packet, int packetLen) {
        int profile = 2; // AAC LC
        int freqIdx = 8; // 44.1KHz
        int chanCfg = 1; // CPE


// fill in ADTS data
        packet[0] = (byte) 0xFF;
        packet[1] = (byte) 0xF9;
        packet[2] = (byte) (((profile - 1) << 6) + (freqIdx << 2) + (chanCfg >> 2));
        packet[3] = (byte) (((chanCfg & 3) << 6) + (packetLen >> 11));
        packet[4] = (byte) ((packetLen & 0x7FF) >> 3);
        packet[5] = (byte) (((packetLen & 7) << 5) + 0x1F);
        packet[6] = (byte) 0xFC;
    }

    @Override
    public boolean needExit() {
        return false;
    }

    @Override
    public void end() {
        try {
            if (bos != null) {
                bos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    bos=null;
                }
            }
        }

        try {
            if (fos != null) {
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            fos=null;
        }

        if (codec != null) {
            codec.stop();
            codec.release();
            codec=null;
        }
//
//        try {
//            if (codec != null) {
//                codec.stop();
//                codec.releaseWithError();
//                codec = null;
//            }
//
//            if (fos != null) {
//                fos.flush();
//                fos.close();
//                fos = null;
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
    }

    public String getFilePath() {
        return filePath;
    }

    @Override
    public void release() {
        end();
    }

    private String getEncoderNamesForType(String mime) {
        int n = MediaCodecList.getCodecCount();
        String name = null;
        for (int i = 0; i < n; ++i) {
            MediaCodecInfo info = MediaCodecList.getCodecInfoAt(i);
            if (!info.isEncoder()) {
                continue;
            }
            if (!info.getName().startsWith("OMX.")) {
                // Unfortunately for legacy reasons, "AACEncoder", a
                // non OMX component had to be in this list for the video
                // editor code to work... but it cannot actually be instantiated
                // using MediaCodec.
                continue;
            }
            String[] supportedTypes = info.getSupportedTypes();
            for (int j = 0; j < supportedTypes.length; ++j) {
                if (supportedTypes[j].equalsIgnoreCase(mime)) {
                    name = info.getName();
                    return name;
                }
            }
        }
        return name;
    }
}
