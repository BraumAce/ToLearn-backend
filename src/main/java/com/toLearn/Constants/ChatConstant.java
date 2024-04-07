package com.toLearn.Constants;


/**
 * 聊天常量
 * @author BraumAce
 */
public final class ChatConstant {
    private ChatConstant() {
    }

    // 私聊
    public static final int PRIVATE_CHAT = 1;

    // 队伍群聊
    public static final int TEAM_CHAT = 2;

    // 大厅聊天
    public static final int HALL_CHAT = 3;

    // 缓存聊天大厅
    public static final String CACHE_CHAT_HALL = "tolearn:chat:chat_records:chat_hall";

    // 缓存私人聊天
    public static final String CACHE_CHAT_PRIVATE = "tolearn:chat:chat_records:chat_private:";

    // 缓存聊天队伍
    public static final String CACHE_CHAT_TEAM = "tolearn:chat:chat_records:chat_team:";
}
