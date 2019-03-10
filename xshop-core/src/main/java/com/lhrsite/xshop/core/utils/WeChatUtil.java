package com.lhrsite.xshop.core.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;

public class WeChatUtil {
    private static final String APPID = "wxb0e10c5ebcf43787";
    private static final String APPSECRET = "7fb3175a07af2296bbd641a5ca59e994";
    private static Long accessTokenGetTime = 0L;

    private static String accessToken = "";

    private static String getAccessToken() throws IOException {
        if ((new Date().getTime() - accessTokenGetTime) / 1000L > 7000) {
            String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + APPID + "&secret=" + APPSECRET;

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            if (response.body() != null) {

                accessTokenGetTime = new Date().getTime();
                String result = response.body().string();
                JSONObject resultObj = new JSONObject(result);
                accessToken = resultObj.getString("access_token");
                System.out.println(accessToken);

            } else {
                System.out.println("获取access_token失败！");
                return "";
            }
        }
        return accessToken;

    }

    public static String getOpenId(String code) throws IOException {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + APPID + "&secret=" + APPSECRET + "&code=" + code + "&grant_type=authorization_code";
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        if (response.body() != null) {

            accessTokenGetTime = new Date().getTime();
            String result = response.body().string();
            JSONObject resultObj = new JSONObject(result);
            String openId = resultObj.getString("openid");
            System.out.println(openId);
            return openId;
        } else {
            System.out.println("获取openId失败！");
            return "";
        }
    }

    public static void main(String[] args) throws IOException {
        getAccessToken();
    }

}
