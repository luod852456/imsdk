package com.chaolu.imsdk.manager;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.chaolu.imsdk.ImSdkLibrary;
import com.chaolu.imsdk.R;
import com.chaolu.imsdk.event.FriendshipEvent;
import com.chaolu.imsdk.event.GroupEvent;
import com.chaolu.imsdk.mvp.presenter.MessageEvent;
import com.chaolu.imsdk.mvp.presenter.RefreshEvent;
import com.chaolu.imsdk.ui.NotifyDialog;
import com.chaolu.imsdk.util.LogUtil;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConnListener;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMElemType;
import com.tencent.imsdk.TIMFaceElem;
import com.tencent.imsdk.TIMFileElem;
import com.tencent.imsdk.TIMFriendshipSettings;
import com.tencent.imsdk.TIMGroupEventListener;
import com.tencent.imsdk.TIMGroupMemberInfo;
import com.tencent.imsdk.TIMGroupSettings;
import com.tencent.imsdk.TIMGroupTipsElem;
import com.tencent.imsdk.TIMImageElem;
import com.tencent.imsdk.TIMLocationElem;
import com.tencent.imsdk.TIMLogLevel;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageStatus;
import com.tencent.imsdk.TIMRefreshListener;
import com.tencent.imsdk.TIMSNSChangeInfo;
import com.tencent.imsdk.TIMSdkConfig;
import com.tencent.imsdk.TIMSoundElem;
import com.tencent.imsdk.TIMTextElem;
import com.tencent.imsdk.TIMUserConfig;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMUserStatusListener;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupAssistantListener;
import com.tencent.imsdk.ext.group.TIMGroupCacheInfo;
import com.tencent.imsdk.ext.group.TIMUserConfigGroupExt;
import com.tencent.imsdk.ext.message.TIMConversationExt;
import com.tencent.imsdk.ext.message.TIMManagerExt;
import com.tencent.imsdk.ext.message.TIMMessageDraft;
import com.tencent.imsdk.ext.message.TIMMessageExt;
import com.tencent.imsdk.ext.message.TIMMessageLocator;
import com.tencent.imsdk.ext.message.TIMMessageRevokedListener;
import com.tencent.imsdk.ext.message.TIMUserConfigMsgExt;
import com.tencent.imsdk.ext.sns.TIMFriendshipProxyListener;
import com.tencent.imsdk.ext.sns.TIMUserConfigSnsExt;

import java.util.List;

public class ImSdkManager {

    private static ImSdkManager instance;

    public static ImSdkManager getInstance(){
        if (instance == null) {
            synchronized (ImSdkManager.class) {
                if (instance == null) {
                    instance = new ImSdkManager();
                }
            }
        }
        return instance;
    }

    /* 初始化（start） */

    /**
     * 初始化
     *
     * @param applicationContext
     */
    public void init(Context applicationContext, String appId) {
        //初始化 SDK
        boolean init = TIMManager.getInstance().init(applicationContext, initTIMSdkConfig(appId));

        //将用户配置与通讯管理器进行绑定
        TIMManager.getInstance().setUserConfig(initUserConfig());
    }

    /**
     * 初始化 SDK 基本配置
     *
     * @return
     * @param appId
     */
    private TIMSdkConfig initTIMSdkConfig(String appId) {
        int i = Integer.parseInt(appId);
        TIMSdkConfig config = new TIMSdkConfig(i)
//                .setAccoutType("36862")
                .enableCrashReport(false)//是否开启sdk集成的bugly
                .enableLogPrint(true)//是否把日志输出到控制台
                .setLogLevel(TIMLogLevel.DEBUG)
//                .setLogPath(Environment.getExternalStorageDirectory().getPath() + "/" + ImSdkLibrary.getInstance().getContext().getPackageName() + "/imsdklogs/");
                .setLogPath(Environment.getExternalStorageDirectory().getPath() + "/imsdklogs/");
        return config;
    }

    /**
     * 基本用户配置
     *
     * @return
     */
    private static TIMUserConfig initUserConfig() {
        TIMUserConfig userConfig = new TIMUserConfig()
                //设置群组资料拉取字段
                .setGroupSettings(new TIMGroupSettings())
                //设置资料关系链拉取字段
                .setFriendshipSettings(new TIMFriendshipSettings())
                //设置用户状态变更事件监听器
                .setUserStatusListener(new TIMUserStatusListener() {
                    @Override
                    public void onForceOffline() {
                        //被其他终端踢下线
                        LogUtil.e("onForceOffline");

//                Intent intent = new Intent(SplashActivity.this, DialogActivity.class);
//                startActivity(intent);
                    }

                    @Override
                    public void onUserSigExpired() {
                        //用户签名过期了，需要刷新 userSig 重新登录 SDK
                        LogUtil.e("onUserSigExpired");

                        if(ActivityManager.getInstance().getLastActivity() instanceof AppCompatActivity) {
                            AppCompatActivity activity = (AppCompatActivity)ActivityManager.getInstance().getLastActivity();
                            //票据过期，需要重新登录
                            new NotifyDialog().show(ImSdkLibrary.getInstance().getContext().getString(R.string.tls_expire), activity.getSupportFragmentManager(), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //                            logout();
                                }
                            });
                        }
                    }
                })
                //设置连接状态事件监听器
                .setConnectionListener(new TIMConnListener() {
                    @Override
                    public void onConnected() {
                        LogUtil.e("onConnected");
                    }

                    @Override
                    public void onDisconnected(int code, String desc) {
                        LogUtil.e("onDisconnected");
                    }

                    @Override
                    public void onWifiNeedAuth(String name) {
                        LogUtil.e("onWifiNeedAuth");
                    }
                })
                //设置群组事件监听器
                .setGroupEventListener(new TIMGroupEventListener() {
                    @Override
                    public void onGroupTipsEvent(TIMGroupTipsElem elem) {
                        LogUtil.e("onGroupTipsEvent, type: " + elem.getTipsType());
                    }
                })
                //设置会话刷新监听器
                .setRefreshListener(new TIMRefreshListener() {
                    @Override
                    public void onRefresh() {
                        LogUtil.e("onRefresh");
                    }

                    @Override
                    public void onRefreshConversation(List<TIMConversation> conversations) {
                        LogUtil.e("onRefreshConversation, conversation size: " + conversations.size());
                    }
                });

        //消息扩展用户配置
        userConfig = new TIMUserConfigMsgExt(userConfig)
                //禁用消息存储
                .enableStorage(false)
                //开启消息已读回执
                .enableReadReceipt(true)
                .setMessageRevokedListener(new TIMMessageRevokedListener() {
                    @Override
                    public void onMessageRevoked(TIMMessageLocator timMessageLocator) {
                        //对方消息撤回
                        //通过checkEquals(@NonNull TIMMessageLocator locator)判断是否 一致
                    }
                });

        //资料关系链扩展用户配置
        userConfig = new TIMUserConfigSnsExt(userConfig)
                //开启资料关系链本地存储
                .enableFriendshipStorage(true)
                //设置关系链变更事件监听器
                .setFriendshipProxyListener(new TIMFriendshipProxyListener() {
                    @Override
                    public void OnAddFriends(List<TIMUserProfile> users) {
                        LogUtil.e("OnAddFriends");
                    }

                    @Override
                    public void OnDelFriends(List<String> identifiers) {
                        LogUtil.e("OnDelFriends");
                    }

                    @Override
                    public void OnFriendProfileUpdate(List<TIMUserProfile> profiles) {
                        LogUtil.e("OnFriendProfileUpdate");
                    }

                    @Override
                    public void OnAddFriendReqs(List<TIMSNSChangeInfo> reqs) {
                        LogUtil.e("OnAddFriendReqs");
                    }

//                    @Override
//                    public void OnAddFriendGroups(List<TIMFriendGroup> friendgroups) {
//                        LogUtil.e("OnAddFriendGroups");
//                    }
//
//                    @Override
//                    public void OnDelFriendGroups(List<String> names) {
//                        LogUtil.e("OnDelFriendGroups");
//                    }
//
//                    @Override
//                    public void OnFriendGroupUpdate(List<TIMFriendGroup> friendgroups) {
//                        LogUtil.e("OnFriendGroupUpdate");
//                    }
                });

        //群组管理扩展用户配置
        userConfig = new TIMUserConfigGroupExt(userConfig)
                //开启群组资料本地存储
                .enableGroupStorage(true)
                //设置群组资料变更事件监听器
                .setGroupAssistantListener(new TIMGroupAssistantListener() {
                    @Override
                    public void onMemberJoin(String groupId, List<TIMGroupMemberInfo> memberInfos) {
                        LogUtil.e("onMemberJoin");
                    }

                    @Override
                    public void onMemberQuit(String groupId, List<String> members) {
                        LogUtil.e("onMemberQuit");
                    }

                    @Override
                    public void onMemberUpdate(String groupId, List<TIMGroupMemberInfo> memberInfos) {
                        LogUtil.e("onMemberUpdate");
                    }

                    @Override
                    public void onGroupAdd(TIMGroupCacheInfo groupCacheInfo) {
                        LogUtil.e("onGroupAdd");
                    }

                    @Override
                    public void onGroupDelete(String groupId) {
                        LogUtil.e("onGroupDelete");
                    }

                    @Override
                    public void onGroupUpdate(TIMGroupCacheInfo groupCacheInfo) {
                        LogUtil.e("onGroupUpdate");
                    }
                });

        RefreshEvent.getInstance().init(userConfig);
        userConfig = FriendshipEvent.getInstance().init(userConfig);
        userConfig = GroupEvent.getInstance().init(userConfig);
        userConfig = MessageEvent.getInstance().init(userConfig);
        TIMManager.getInstance().setUserConfig(userConfig);

        return userConfig;
    }
    /* 初始化（end） */


    /*  登录（start）  */

    /**
     * 登录
     *
     * @param identifier  用户名
     * @param userSig     为用户登录凭证
     * @param timCallBack 回调监听
     */
    public void login(String identifier, String userSig, TIMCallBack timCallBack) {
        TIMManager.getInstance().login(identifier, userSig, timCallBack);
    }

    /**
     * 登出
     *
     * @param timCallBack 回调监听
     */
    public void logout(TIMCallBack timCallBack) {
        TIMManager.getInstance().logout(timCallBack);
    }

    /**
     * 初始化本地存储(暂不需要)
     *
     * @param identifier 用户名
     */
    public void initStorage(String identifier) {
//        TIMManagerExt.getInstance().initStorage(identifier, new TIMCallBack() {
//            @Override
//            public void onError(int code, String desc) {
//                LogUtil.e("initStorage failed, code: " + code + "|descr: " + desc);
//            }
//
//            @Override
//            public void onSuccess() {
//                LogUtil.e( "initStorage succ");
//            }
//        });
    }

    /**
     * 获取会话实例(暂不需要)
     *
     * @param peer
     */
    public void getConversationLocal(String peer) {
//        TIMConversation conversation = TIMManager.getInstance().getConversation(TIMConversationType.C2C, peer);
//        //获取会话扩展实例
//        TIMConversationExt ext = new TIMConversationExt(conversation);
//        //获取本地消息
//        ext.getLocalMessage(5, null, new TIMValueCallBack<List<TIMMessage>>() {
//            @Override
//            public void onError(int code, String desc) {
//                LogUtil.e("get msgs failed, code: " + code + "|msg: " + desc);
//            }
//
//            @Override
//            public void onSuccess(List<TIMMessage> timMessages) {
//                LogUtil.e( "get msgs succ, size: " + timMessages.size());
//            }
//        });
    }

    /**
     * 获取当前登录用户
     */
    public String getLoginUser() {
        return TIMManager.getInstance().getLoginUser();
    }
    /* 登录（end）  */


    /* 消息收发（start） */

    /**
     * 获取会话
     *
     * @param type 会话类型
     * @param peer 参与会话的对方, C2C 会话为对方帐号 identifier, 群组Group会话为群组 ID
     * @return 会话实例
     */
    public TIMConversation getConversation(TIMConversationType type, String peer) {
        return TIMManager.getInstance().getConversation(
                type,
                peer);
    }

    /**
     * 发送消息
     *
     * @param conversation     会话实例
     * @param msg              消息内容
     * @param timValueCallBack 回调TIMValueCallBack<TIMMessage>
     */
    public void sendMessage(TIMConversation conversation, TIMMessage msg, TIMValueCallBack timValueCallBack) {
        conversation.sendMessage(msg, timValueCallBack);
    }

    /**
     * 发送在线消息（服务器不保存消息）
     *
     * @param conversation     会话实例
     * @param msg              消息内容
     * @param timValueCallBack 回调TIMValueCallBack<TIMMessage>
     */
    public void sendOnlineMessage(TIMConversation conversation, TIMMessage msg, TIMValueCallBack timValueCallBack) {
        conversation.sendOnlineMessage(msg, timValueCallBack);
    }

    /**
     * 文本消息发送
     *
     * @param type             会话类型
     * @param peer             参与会话的对方, C2C 会话为对方帐号 identifier, 群组Group会话为群组 ID
     * @param text             发送的文字
     * @param timValueCallBack 回调
     */
    public void sendMessageText(TIMConversationType type, String peer, String text, TIMValueCallBack timValueCallBack) {
        TIMMessage msg = new TIMMessage();//构造一条消息
        TIMTextElem elem = new TIMTextElem();//添加文本内容
        elem.setText(text);
        if (msg.addElement(elem) != 0) {//将elem添加到消息
            LogUtil.e("addElement failed");
            return;
        }
        sendMessage(getConversation(type, peer), msg, timValueCallBack);
    }

    /**
     * 图片消息发送
     *
     * @param type             会话类型
     * @param peer             参与会话的对方, C2C 会话为对方帐号 identifier, 群组Group会话为群组 ID
     * @param path             发送的图片路径
     * @param timValueCallBack 回调
     */
    public void sendMessageImage(TIMConversationType type, String peer, String path, TIMValueCallBack timValueCallBack) {
        TIMMessage msg = new TIMMessage();//构造一条消息
        TIMImageElem elem = new TIMImageElem();//添加图片
        elem.setPath(path);
        if (msg.addElement(elem) != 0) {//将 elem 添加到消息
            LogUtil.e("addElement failed");
            return;
        }
        sendMessage(getConversation(type, peer), msg, timValueCallBack);
    }

    /**
     * 表情消息发送
     *
     * @param type             会话类型
     * @param peer             参与会话的对方, C2C 会话为对方帐号 identifier, 群组Group会话为群组 ID
     * @param sampleByteArray  自定义 byte[]
     * @param index            自定义表情索引
     * @param timValueCallBack 回调
     */
    public void sendMessageFace(TIMConversationType type, String peer, byte[] sampleByteArray, int index, TIMValueCallBack timValueCallBack) {
        TIMMessage msg = new TIMMessage();//构造一条消息
        TIMFaceElem elem = new TIMFaceElem();//添加表情
        elem.setData(sampleByteArray);  //自定义 byte[]
        elem.setIndex(index);           //自定义表情索引
        if (msg.addElement(elem) != 0) {//将 elem 添加到消息
            LogUtil.e("addElement failed");
            return;
        }
        sendMessage(getConversation(type, peer), msg, timValueCallBack);
    }

    /**
     * 语音消息发送
     *
     * @param type             会话类型
     * @param peer             参与会话的对方, C2C 会话为对方帐号 identifier, 群组Group会话为群组 ID
     * @param filePath         语音文件路径
     * @param duration         填写语音时长
     * @param timValueCallBack 回调
     */
    public void sendMessageSound(TIMConversationType type, String peer, String filePath, int duration, TIMValueCallBack timValueCallBack) {
        TIMMessage msg = new TIMMessage();//构造一条消息
        TIMSoundElem elem = new TIMSoundElem();//添加语音
        elem.setPath(filePath); //填写语音文件路径
        elem.setDuration(duration);  //填写语音时长
        if (msg.addElement(elem) != 0) {//将 elem 添加到消息
            LogUtil.e("addElement failed");
            return;
        }
        sendMessage(getConversation(type, peer), msg, timValueCallBack);
    }

    /**
     * 地理位置消息发送
     *
     * @param type             会话类型
     * @param peer             参与会话的对方, C2C 会话为对方帐号 identifier, 群组Group会话为群组 ID
     * @param latitude         设置纬度
     * @param longitude        设置经度
     * @param timValueCallBack 回调
     */
    public void sendMessageLocation(TIMConversationType type, String peer, double latitude, double longitude, String desc, TIMValueCallBack timValueCallBack) {
        TIMMessage msg = new TIMMessage();//构造一条消息
        TIMLocationElem elem = new TIMLocationElem();//添加位置信息
        elem.setLatitude(latitude);   //设置纬度
        elem.setLongitude(longitude);   //设置经度
        elem.setDesc(desc);
        if (msg.addElement(elem) != 0) {//将 elem 添加到消息
            LogUtil.e("addElement failed");
            return;
        }
        sendMessage(getConversation(type, peer), msg, timValueCallBack);
    }

    /**
     * 小文件消息发送
     *
     * @param type             会话类型
     * @param peer             参与会话的对方, C2C 会话为对方帐号 identifier, 群组Group会话为群组 ID
     * @param filePath         文件路径
     * @param fileName         文件名称
     * @param timValueCallBack 回调
     */
    public void sendMessageLocation(TIMConversationType type, String peer, String filePath, String fileName, TIMValueCallBack timValueCallBack) {
        TIMMessage msg = new TIMMessage();//构造一条消息
        TIMFileElem elem = new TIMFileElem();//添加文件内容
        elem.setPath(filePath); //设置文件路径
        elem.setFileName(fileName); //设置消息展示用的文件名称
        if (msg.addElement(elem) != 0) {//将 elem 添加到消息
            LogUtil.e("addElement failed");
            return;
        }
        sendMessage(getConversation(type, peer), msg, timValueCallBack);
    }

    /**
     * 小文件消息发送
     *
     * @param type             会话类型
     * @param peer             参与会话的对方, C2C 会话为对方帐号 identifier, 群组Group会话为群组 ID
     * @param data             自定义 byte[]
     * @param desc             自定义描述信息
     * @param timValueCallBack 回调
     */
    public void sendMessageLocation(TIMConversationType type, String peer, byte[] data, String desc, TIMValueCallBack timValueCallBack) {
        TIMMessage msg = new TIMMessage();//构造一条消息
        TIMCustomElem elem = new TIMCustomElem();//向 TIMMessage 中添加自定义内容
        elem.setData(data);      //自定义 byte[]
        elem.setDesc(desc);     //自定义描述信息
        if (msg.addElement(elem) != 0) {//将 elem 添加到消息
            LogUtil.e("addElement failed");
            return;
        }
        sendMessage(getConversation(type, peer), msg, timValueCallBack);
    }

    /**
     * 复制消息内容到当前消息（Elem, priority, online, offlinePushInfo 等）
     *
     * @param myMsg  当前消息
     * @param srcMsg 源消息
     * @return true 复制成功
     */
    public boolean copyFrom(@NonNull TIMMessage myMsg, @NonNull TIMMessage srcMsg) {
        return myMsg.copyFrom(srcMsg);
    }

    /**
     * 消息解析
     *
     * @param msg
     */
    public void getTimMessage(TIMMessage msg) {
        for (int i = 0; i < msg.getElementCount(); ++i) {
            TIMElem elem = msg.getElement(i);
            //获取当前元素的类型
            TIMElemType elemType = elem.getType();
            LogUtil.e("elem type: " + elemType.name());
            if (elemType == TIMElemType.Text) {
                //处理文本消息
            } else if (elemType == TIMElemType.Image) {
                //处理图片消息
            }//...处理更多消息
        }
    }


    /* TIMMessage 操作（start） */

    /**
     * 获取消息扩展实例， 其中参数 msg 是 TIMMessage 的一个对象
     *
     * @param msg 源消息
     */
    public boolean isRead(@NonNull TIMMessage msg) {
        TIMMessageExt msgExt = new TIMMessageExt(msg);
        return msgExt.isRead();
    }

    /**
     * 消息的状态
     *
     * @param msg 源消息
     * @return //发送中 Sending
     * //发送成功 SendSucc
     * //发送失败 SendFail
     * //删除 HasDeleted
     * //消息被撤回 HasRevoked
     */
    public TIMMessageStatus status(@NonNull TIMMessage msg) {
        return msg.status();
    }

    /**
     * 是否自己发出的消息
     *
     * @param msg 源消息
     */
    public boolean isSelf(@NonNull TIMMessage msg) {
        return msg.isSelf();
    }

    /**
     * 获取消息发送方
     *
     * @param msg 源消息
     */
    public String getSender(@NonNull TIMMessage msg) {
        return msg.getSender();
    }

    /**
     * 正在聊天的用户
     *
     * @param msg 源消息
     */
    public String getPeer(@NonNull TIMMessage msg) {
        return msg.getConversation().getPeer();
    }

    /**
     * 消息在服务端生成的时间戳
     *
     * @param msg 源消息
     */
    public long timestamp(@NonNull TIMMessage msg) {
        return msg.timestamp();
    }

    /**
     * 消息删除(本地删除)
     *
     * @param msg 源消息
     */
    public boolean remove(@NonNull TIMMessage msg) {
        TIMMessageExt msgExt = new TIMMessageExt(msg);
        return msgExt.remove();
    }

    /**
     * 消息序列号
     *
     * @param msg 源消息
     */
    public long getSeq(@NonNull TIMMessage msg) {
        return msg.getSeq();
    }

    /**
     * 消息随机码
     *
     * @param msg 源消息
     */
    public long getRand(@NonNull TIMMessage msg) {
        return msg.getRand();
    }

    /**
     * 消息查找参数
     *
     * @param msg 源消息
     */
    public TIMMessageLocator getMessageLocator(@NonNull TIMMessage msg) {
        TIMMessageExt msgExt = new TIMMessageExt(msg);
        return msgExt.getMessageLocator();
    }
    /*  TIMMessage 操作（end）  */

    /**
     * 获取所有会话
     */
    public List<TIMConversation> getConversationList() {
        return TIMManagerExt.getInstance().getConversationList();
    }

    /**
     * 删除会话缓存
     *
     * @param type 会话类型
     * @param peer 参与会话的对方, C2C 会话为对方帐号 identifier, 群组Group会话为群组 ID
     */
    public void deleteConversation(TIMConversationType type, String peer) {
        TIMManagerExt.getInstance().deleteConversation(type, peer);
    }

    /**
     * 删除会话缓存并同时删除该会话相关的本地消息
     *
     * @param type 会话类型
     * @param peer 参与会话的对方, C2C 会话为对方帐号 identifier, 群组Group会话为群组 ID
     */
    public void deleteConversationAndLocalMsgs(TIMConversationType type, String peer) {
        TIMManagerExt.getInstance().deleteConversationAndLocalMsgs(type, peer);
    }

    /**
     * 获取聊天记录
     *
     * @param type             会话类型
     * @param peer             参与会话的对方, C2C 会话为对方帐号 identifier, 群组Group会话为群组 ID
     * @param rows             获取此会话最近的 n 条消息
     * @param lastMsg          null:不指定从哪条消息开始获取 - 等同于从最新的消息开始往前
     * @param timValueCallBack 回调List<TIMMessage>
     */
    public void getMessage(TIMConversationType type, String peer, int rows, TIMMessage lastMsg, TIMValueCallBack timValueCallBack) {
        TIMConversationExt conExt = new TIMConversationExt(getConversation(type, peer));
        conExt.getMessage(rows, //获取此会话最近的 n 条消息
                lastMsg, //不指定从哪条消息开始获取 - 等同于从最新的消息开始往前
                timValueCallBack);
    }

    /**
     * 获取会话最后一条的消息
     *
     * @param type 会话类型
     * @param peer 参与会话的对方, C2C 会话为对方帐号 identifier, 群组Group会话为群组 ID
     */
    public void getLastMsgs(TIMConversationType type, String peer) {
        TIMConversationExt conExt = new TIMConversationExt(getConversation(type, peer));
        conExt.getLastMsgs(1);
    }

    /**
     * 当前会话是否存在草稿
     *
     * @param type 会话类型
     * @param peer 参与会话的对方, C2C 会话为对方帐号 identifier, 群组Group会话为群组 ID
     */
    public boolean hasDraft(TIMConversationType type, String peer) {
        TIMConversationExt conExt = new TIMConversationExt(getConversation(type, peer));
        return conExt.hasDraft();
    }

    /**
     * 设置草稿
     *
     * @param type  会话类型
     * @param peer  参与会话的对方, C2C 会话为对方帐号 identifier, 群组Group会话为群组 ID/**
     * @param draft 草稿内容, 为 null 则表示取消草稿
     */
    public void hasDraft(TIMConversationType type, String peer, TIMMessageDraft draft) {
        TIMConversationExt conExt = new TIMConversationExt(getConversation(type, peer));
        conExt.setDraft(draft);
    }

    /**
     * 获取草稿
     *
     * @param type 会话类型
     * @param peer 参与会话的对方, C2C 会话为对方帐号 identifier, 群组Group会话为群组 ID
     */
    public TIMMessageDraft getDraft(TIMConversationType type, String peer) {
        TIMConversationExt conExt = new TIMConversationExt(getConversation(type, peer));
        return conExt.getDraft();
    }

    /**
     * 禁止当前会话的存储，只对当前初始化有效，重启后需要重新设置。
     * 需要初始后调用（暂时不用）
     */
    public void disableStorage(TIMConversationType type, String peer) {
        TIMConversationExt conExt = new TIMConversationExt(getConversation(type, peer));
        conExt.disableStorage();
    }

    /**
     * 批量删除本会话的全部本地聊天记录
     * 群组会话在清空本地聊天记录后，仍然会通过漫游拉取到本地删除了的历史消息
     *
     * @param timCallBack 回调
     */
    public void deleteLocalMessage(TIMConversationType type, String peer, TIMCallBack timCallBack) {
        TIMConversationExt conExt = new TIMConversationExt(getConversation(type, peer));
        conExt.deleteLocalMessage(timCallBack);
    }

    /**
     * 撤回消息
     *
     * @param timCallBack 回调
     */
    public void revokeMessage(TIMConversationType type, String peer, @NonNull TIMMessage msg, TIMCallBack timCallBack) {
        TIMConversationExt conExt = new TIMConversationExt(getConversation(type, peer));
        conExt.revokeMessage(msg, timCallBack);
    }

    /**
     * 比较当前消息与提供的消息定位符表示的消息是否是同一条消息
     */
    public void checkEquals(@NonNull TIMMessage msg, @NonNull TIMMessageLocator locator) {
        TIMMessageExt msgExt = new TIMMessageExt(msg);
        msgExt.checkEquals(locator);
    }
    /* 消息收发（end） */


    /* 未读消息（start） */

    /**
     * 获取当前未读消息数量
     */
    public void getUnreadMessageNum(TIMConversationType type, String peer) {
        TIMConversationExt conExt = new TIMConversationExt(getConversation(type, peer));
        conExt.getUnreadMessageNum();
    }

    /**
     * 将此消息之前的所有消息标记为已读
     *
     * @param msg         最后一条已读的消息, 传 null 表示将当前会话的所有消息标记为已读
     * @param timCallBack 回调
     */
    public void setReadMessage(TIMConversationType type, String peer, TIMMessage msg, TIMCallBack timCallBack) {
        TIMConversationExt conExt = new TIMConversationExt(getConversation(type, peer));
        conExt.setReadMessage(msg, timCallBack);
    }
    /* 未读消息（end） */

}
