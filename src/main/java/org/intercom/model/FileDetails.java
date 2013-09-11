package org.intercom.model;

public class FileDetails {
	String name;
	long  size;
	String url;
	String type;
	String delete_url;
	String delete_type;
	String download_url;

	public String getDownload_url() {
		return download_url;
	}
	public void setDownload_url(String download_url) {
		this.download_url = download_url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long  getSize() {
		return size;
	}
	public void setSize(long  size) {
		this.size = size;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDelete_url() {
		return delete_url;
	}
	public void setDelete_url(String delete_url) {
		this.delete_url = delete_url;
	}
	public String getDelete_type() {
		return delete_type;
	}
	public void setDelete_type(String delete_type) {
		this.delete_type = delete_type;
	}
}
