package im.zego.zimkit.token;

import im.zego.zimkit.constant.AppConfig;

public class TokenManager {
    public static String genToken(String userId) {
        long appId = AppConfig.APP_ID;    // 请替换为你的 appId，从即构控制台获取
        String serverSecret = AppConfig.ServerSecret;  // 请替换为你的 serverSecret，从即构控制台获取，
        int effectiveTimeInSeconds = 60 * 60 * 2;   // 有效时间，2h
        // // 基础鉴权 token，payload字段可传空
        String payload = "{\"payload\":\"payload\"}";
        TokenServerAssistant.VERBOSE = false;    // 调试时，置为 true, 可在控制台输出更多信息；正式运行时，最好置为 false
        TokenServerAssistant.TokenInfo token = TokenServerAssistant.generateToken04(appId, userId, serverSecret, effectiveTimeInSeconds, payload);
        System.out.println(token);

        if (token.error == null || token.error.code == TokenServerAssistant.ErrorCode.SUCCESS) {
            System.out.println("\r\ndecrypt the token ...");
            return token.data;
        } else {
            return null;
        }
    }
}
