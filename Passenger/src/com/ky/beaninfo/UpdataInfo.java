package com.ky.beaninfo;

public class UpdataInfo {
	public int version;
	public String url;
	public String description;
	public String md5;
	public String forceUpdateCode;

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}