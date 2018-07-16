package com.neusoft.neusipo.translate.bizservice.impl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import com.neusoft.neusipo.translate.bizservice.TranslateService;
/**
 * 
 * <p>[描述信息：说明类的基本功能]</p>
 *
 * @author 范丹平
 * @mail fandp@neusoft.com
 * @version 1.0 Created on 2018-6-29 下午04:32:19
 */
public class TranslateServiceImpl implements TranslateService {

	//Request URL:https://patentscope.wipo.int/translate/WipoCaptcha/0.09572991111886342
	/**
	 * 访问首页获取cookie 和 viewState
	 */
	public static String GetCookieAndViewstate(){
		//通过httpclient获取网页响应，将返回的响应解析为纯文本。
		  CloseableHttpClient httpClient = null;
		  CloseableHttpResponse response = null;
	      HttpGet httpGet = null;
	      String resultCookie = null;
	      String viewState="";
	      CookieStore cookieStore = new BasicCookieStore();
	      httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
	      httpGet = new HttpGet("https://patentscope.wipo.int/translate/translate.jsf?interfaceLanguage=zh");
	      try {
	    	  response=httpClient.execute(httpGet);
	    	  HttpEntity entity = response.getEntity();
	    	  String html = EntityUtils.toString(entity);
	    	  viewState= parseHtmlViewState(html);
	    	  String JSESSIONID = null;
	            List<Cookie> cookies = cookieStore.getCookies();
	            for (int i = 0; i < cookies.size(); i++) {
	                if (cookies.get(i).getName().equals("JSESSIONID")) {
	                    JSESSIONID = cookies.get(i).getValue();
	                }
	            }
	            resultCookie = JSESSIONID;
	            if (response != null)
                    response.close();
                if (httpClient != null)
                    httpClient.close();
	            
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resultCookie+","+viewState;
		
	}
	/**
	 * 
	 * <p>[描述功能]</p>
	 * 
	 * @param content
	 * @return
	 * @return: String
	 * @author: 范丹平
	 * @mail: fandp@neusoft.com
	 * @date: Created on 2018-7-2 上午08:57:34
	 */
	public static String parseHtmlViewState(String content){
		Document doc = Jsoup.parse(content);
		Element viewState=doc.getElementById("javax.faces.ViewState");
		String viewStateValue=viewState.val().toString();
		return viewStateValue;
		
	}
	private static void closeHttpClient(CloseableHttpClient client) throws IOException {
        if (client != null) {
            client.close();
        }
    }
	/**
	 * 
	 * <p>[描述功能]</p>
	 * 
	 * @param param1
	 * @param param2
	 * @return
	 * @return: String
	 * @author: 范丹平
	 * @mail: fandp@neusoft.com
	 * @date: Created on 2018-7-2 上午08:59:55
	 */
	public static String getTranslateResult(String param1,String param2){
		String paramValue=param1;
		String cookie=paramValue.split(",")[0];
		String viewState=paramValue.split(",")[1];
		CloseableHttpClient httpClient =HttpClients.createDefault();
		//String translation="";
		String current="";
		try {
            HttpPost post = new HttpPost("https://patentscope.wipo.int/translate/translate.jsf"); 
            post.addHeader("Cookie", "JSESSIONID="+cookie);
            //创建参数列表
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            list.add(new BasicNameValuePair("formToTranslate", "formToTranslate"));
            list.add(new BasicNameValuePair("formToTranslate:srcTxt", param2));
            list.add(new BasicNameValuePair("formToTranslate:langpair", "zh-NMT-en"));
            list.add(new BasicNameValuePair("formToTranslate:domain", "XXXX"));
            list.add(new BasicNameValuePair("formToTranslate:userProvidedTranslations", ""));
            list.add(new BasicNameValuePair("formToTranslate:srcSegmentToHighlight", ""));
            list.add(new BasicNameValuePair("formToTranslate:txtTranslated", ""));
            list.add(new BasicNameValuePair("formToTranslate:taptakey", ""));
            list.add(new BasicNameValuePair("formToTranslate:captchaText", ""));
            list.add(new BasicNameValuePair("javax.faces.ViewState", viewState));
            list.add(new BasicNameValuePair("javax.faces.source", "formToTranslate:formToTranslateSubmit"));
            list.add(new BasicNameValuePair("javax.faces.partial.event", "click"));
            list.add(new BasicNameValuePair("javax.faces.partial.execute", "formToTranslate:formToTranslateSubmit formToTranslate"));
            list.add(new BasicNameValuePair("javax.faces.behavior.event", "action"));
            list.add(new BasicNameValuePair("javax.faces.partial.ajax", "true"));
            
            //url格式编码
            UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(list, "UTF-8");
            post.setEntity(uefEntity);
            System.out.println("POST 请求...." + post.getURI());
            //执行请求
            CloseableHttpResponse httpResponse = httpClient.execute(post);
            try {
                HttpEntity entity = httpResponse.getEntity();
                if (null != entity) {
                    Document doc = Jsoup.parse(EntityUtils.toString(entity));
                    System.out.println(doc);
                    //解析翻译结果
                    Element elt=doc.getElementById("dstTextProposals");
                    if (elt==null) {return null;}
                    current=""+elt.html().replaceAll("<br[^>]*>","@@@").replaceAll("<[^>]+>|\n","").replaceAll("@@@ ?", "\n");
                    current=current.replaceAll("⇓[^⇑]+⇑"," ");
                    current=current.replaceAll("&nbsp;", " ")
                            .replaceAll("／","").replaceAll("  +"," ").replaceAll(" +([.;,]) *","$1 ")
                          .replaceAll("[\n\r][\n\r][\n\r][\n\r]", "");
                    while (current.charAt(0) == ' ') {current = current.substring(1);}  
                }
                
              /*  FileWriter  out=new FileWriter ("E:/test/test.txt");
                BufferedWriter bw= new BufferedWriter(out); 
                bw.write(current); 
                bw.newLine(); */
               
               
            } finally {
                httpResponse.close();
                httpClient.close();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
            	 closeHttpClient(httpClient);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
		return current;
	}
	
	public String queryTranslateInfo(String translateText) {
		 System.out.println(translateText);
		// 进行爬虫逻辑
		 String firstGet=GetCookieAndViewstate();
		 String translateResult=getTranslateResult(firstGet,translateText);
		 System.out.println(translateResult);
		 return translateResult;
	}
	
	 

}
