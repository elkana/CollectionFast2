package id.co.ppu.collectionfast2.util;

/**
 * Created by Eric on 13-Jul-17.
 */

public class NewsUtil {

    public static final String MESSAGE_STATUS_UNOPENED_OR_FIRSTTIME = "0";
    public static final String MESSAGE_STATUS_OPENED = "1";

    // 0: common text, 1: timestamp, 2: web/html
    public static final String MESSAGE_TYPE_TEXT = "0";
    public static final String MESSAGE_TYPE_TIMESTAMP = "1";
    public static final String MESSAGE_TYPE_WEB = "2";

    public static final String KEY_FROM = "key_from";
    public static final String KEY_TO = "key_to";
    public static final String KEY_UID = "key_uid";
//    public static final String KEY_STATUS = "key_status";
    //    public static final String KEY_SEQNO = "key_seqno";
    public static final String KEY_TIMESTAMP = "key_timestamp"; //yyyyMMddHHmmss
    public static final String KEY_MSG_TYPE = "key_message_type";

    public static final String KEY_ARTICLE = "key_article";
    public static final String TIMESTAMP_PATTERN = "yyyyMMddHHmmss";

    public static boolean isTitleIsNews(String title) {
        if (title == null) return false;

        return title.trim().toLowerCase().startsWith("berita:")
                || title.trim().toLowerCase().startsWith("news:");

    }
}
