package com.my.dingtalkdelete.dingtalk.models;


public class ConversationInfo {//com.alibaba.wukong.im.Conversation
    public static class ConversationType {
        public static final int CHAT = 1;
        public static final int GROUP = 2;
        public static final int SAFE_CHAT = 3;
        @java.lang.Deprecated
        public static final int SPECIAL = -1;
        public static final int UNKNOWN = 0;
    }

    public enum ConversationStatus {
        NORMAL(0),
        QUIT(1),
        KICKOUT(2),
        OFFLINE(3),
        HIDE(4),
        DISBAND(5);

        public final int value;

        private ConversationStatus(int value2) {
            this.value = value2;
        }

        public final int typeValue() {
            return this.value;
        }

        public static ConversationStatus fromValue(int value2) {
            for (ConversationStatus t : values()) {
                if (t.typeValue() == value2) {
                    return t;
                }
            }
            return NORMAL;
        }
    }

    public long categoryId;
    public long ownerId;
    public long peerId;
    public long groupId;
    public ConversationStatus status;
    public String title;
    public int type;
    public String spaceId;

    @Override
    public String toString() {
        return "ConversationInfo{" +
                "categoryId=" + categoryId +
                ", ownerId=" + ownerId +
                ", peerId=" + peerId +
                ", groupId=" + groupId +
                ", status=" + status +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", spaceId='" + spaceId + '\'' +
                '}';
    }
}
