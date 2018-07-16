package com.neusoft.neusipo.translate.action;

import com.neusoft.neusipo.base.action.ManageBaseAction;
import com.neusoft.neusipo.translate.bizservice.TranslateService;

/**
 * 
 * <p>[描述信息：说明类的基本功能]</p>
 *
 * @author 范丹平
 * @mail fandp@neusoft.com
 * @version 1.0 Created on 2018-6-29 下午04:22:44
 */
public class ChineseToENglishAC  extends ManageBaseAction{
	private TranslateService translateService;
	private String translateInput;
	private String translateresult ;
	private String timeConsuming;
	
	 public String execute(){
		 long startTime = System.currentTimeMillis(); 
		 translateresult=translateService.queryTranslateInfo(translateInput);
		 long endTime = System.currentTimeMillis();    //获取结束时间
		 timeConsuming=(endTime-startTime)+"ms";
		 return "JSON";
		 
	 }
	

	public String getTranslateresult() {
		return translateresult;
	}


	public void setTranslateresult(String translateresult) {
		this.translateresult = translateresult;
	}

	public void setTranslateService(TranslateService translateService) {
		this.translateService = translateService;
	}

	public String getTranslateInput() {
		return translateInput;
	}

	public void setTranslateInput(String translateInput) {
		this.translateInput = translateInput;
	}


	public String getTimeConsuming() {
		return timeConsuming;
	}


	public void setTimeConsuming(String timeConsuming) {
		this.timeConsuming = timeConsuming;
	}
	
	
	
}
