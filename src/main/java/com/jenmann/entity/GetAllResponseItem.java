package com.jenmann.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetAllResponseItem {

	@JsonProperty("name")
	private String name;

	@JsonProperty("index")
	private String index;

	@JsonProperty("url")
	private String url;

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setIndex(String index){
		this.index = index;
	}

	public String getIndex(){
		return index;
	}

	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}
}