package com.yado.pryado.pryadonew.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * 更新实体类
 * 
 */
@SuppressWarnings("serial")
public class Update implements Serializable {

	@JSONField(name="versionCode")
	private int versionCode;
	@JSONField(name="versionName")
	private String versionName;
	@JSONField(name="downloadUrl")
	private String downloadUrl;
	@JSONField(name="updateTime")
	private String updateTime;
	@JSONField(name="updateLog")
	private String updateLog;

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateLog() {
		return updateLog;
	}

	public void setUpdateLog(String updateLog) {
		this.updateLog = updateLog;
	}
}
