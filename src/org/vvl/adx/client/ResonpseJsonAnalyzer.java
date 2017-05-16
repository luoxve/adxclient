package org.vvl.adx.client;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.Map;

public class ResonpseJsonAnalyzer {
    // 响应的JSON
    private String respJson;
    // 出价
    private String price;
    // 广告物料URL
    private String imgurl;
    // win notice rul
    private String nurl;
    // 广告展示监播地址
    private String impurl;
    // 广告点击监播地址
    private String curl;

    public ResonpseJsonAnalyzer(String respJson) {
        this.respJson = respJson;
    }

    /**
     * 解析得到price\imgurl\nurl\impurl\curl
     */
    public void analyze() {
        JSONObject jsonObject = new JSONObject();
        Map<String, String> jsonMap = jsonObject.fromObject(respJson);
        JSONArray seatBidList = JSONArray.fromObject(jsonMap.get("seatbid"));
        Map<String, String> seatBidMap = (Map) seatBidList.get(0);
        JSONArray bidList = JSONArray.fromObject(seatBidMap.get("bid"));
        Map<String, Object> bidMap = (Map) bidList.get(0);
        price = bidMap.get("price").toString();
        nurl = bidMap.get("nurl").toString();
        JSONArray curlList = JSONArray.fromObject(bidMap.get("curl"));
        if (curlList.size() > 0) {
            curl = curlList.get(0).toString();
        }
        JSONArray impurlList = JSONArray.fromObject(bidMap.get("impurl"));
        if (impurlList.size() > 0) {
            impurl = impurlList.get(0).toString();
        }
        String admJson = bidMap.get("adm").toString();
        admJson = admJson.substring(1, admJson.length() - 1);
        Map<String, String> admMap = jsonObject.fromObject(admJson);
        JSONArray imgurlList = JSONArray.fromObject(admMap.get("imgurls"));
        if (imgurlList.size() > 0) {
            imgurl = imgurlList.get(0).toString();
        }
    }

    public void toStr() {
        System.out.println("price:" + price);
        System.out.println("nurl:" + nurl);
        System.out.println("curl:" + curl);
        System.out.println("impurl:" + impurl);
        System.out.println("imgurl:" + imgurl);
    }

    public String getPrice() {
        return price;
    }

    public String getImgurl() {
        return imgurl;
    }

    public String getNurl() {
        return nurl;
    }

    public String getImpurl() {
        return impurl;
    }

    public String getCurl() {
        return curl;
    }
}
