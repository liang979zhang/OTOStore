package com.example.administrator.otostore.Utils;

/**
 * 作者：Rance on 2016/12/20 16:51
 * 邮箱：rance935@163.com
 */
public class Constants {
    public static final String TAG = "rance";
    public static final String BASE_URL = "http://115.28.214.108:8090";
    public static final int JiangJuanSelectRequest = 3;
    public static final int JiangJuanSelectResult = 4;


    public static final int KuaiDiSelectRequest = 5;
    public static final int KuaidiSelectResult = 6;
    /**
     * 0x001-接受消息  0x002-发送消息
     **/
    public static final int CHAT_ITEM_TYPE_LEFT = 0x001;
    public static final int CHAT_ITEM_TYPE_RIGHT = 0x002;
    /**
     * 0x003-发送中  0x004-发送失败  0x005-发送成功
     **/
    public static final int CHAT_ITEM_SENDING = 0x003;
    public static final int CHAT_ITEM_SEND_ERROR = 0x004;
    public static final int CHAT_ITEM_SEND_SUCCESS = 0x005;
    public static final String DB_NAME = "video.realm";
    public static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    public static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    public static final int SelectIndustryRequest = 1000;
    public static final int SelectIndustryReSult = 1001;
}
