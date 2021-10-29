package com.jenmann.entity;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GetAllResponse{

	@JsonProperty("count")
	private int count;

	@JsonProperty("results")
	private List<GetAllResponseItem> results;

	public void setCount(int count){
		this.count = count;
	}

	public int getCount(){
		return count;
	}

	public void setResults(List<GetAllResponseItem> results){
		this.results = results;
	}

	public List<GetAllResponseItem> getResults(){
		return results;
	}
}