package com.demo.model;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestLoginParams {
	public String Cookie;
    public String SystemName;
    public String AfterLoginPage;
    
    @JsonProperty("Cookie")
    @JsonGetter("Cookie")
	public String getCookie() {
		return Cookie;
	}
    
	public void setCookie(String cookie) {
		Cookie = cookie;
	}
	
	@JsonProperty("SystemName")
    @JsonGetter("SystemName")
	public String getSystemName() {
		return SystemName;
	}
	public void setSystemName(String systemName) {
		SystemName = systemName;
	}
	@JsonProperty("AfterLoginPage")
    @JsonGetter("AfterLoginPage")
	public String getAfterLoginPage() {
		return AfterLoginPage;
	}
	public void setAfterLoginPage(String afterLoginPage) {
		AfterLoginPage = afterLoginPage;
	}
	public RequestLoginParams(String cookie,String systemName,String afterLoginPage) {
		setCookie(cookie);
		setSystemName(systemName);
		setAfterLoginPage(afterLoginPage);
	}
}
