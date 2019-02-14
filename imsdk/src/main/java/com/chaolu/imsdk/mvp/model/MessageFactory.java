package com.chaolu.imsdk.mvp.model;


import com.tencent.imsdk.TIMMessage;

/**
 * 消息工厂
 */
public class MessageFactory {

    private MessageFactory() {
    }


    /**
     * 消息工厂方法
     */
    public static Message getMessage(TIMMessage message) {
        switch (message.getElement(0).getType()) {
            case Text:
            case Face:
                return new TextMessage(message);
            case Image:
                return new ImageMessage(message);
            case Sound:
                return new VoiceMessage(message);
            case Video:
                //小视频
//                return new VideoMessage(message);
                return null;
            case GroupTips:
                return new GroupTipMessage(message);
            case File:
                //文件
//                return new FileMessage(message);
                return null;
            case Custom:
                return new CustomMessage(message);
            case UGC:
                //小视频
//                return new UGCMessage(message);
                return null;
            default:
                return null;
        }
    }


}
