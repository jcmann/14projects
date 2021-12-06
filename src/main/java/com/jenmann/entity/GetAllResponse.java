package com.jenmann.entity;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The GetAllResponse represents the data received from the DND 5e API when requesting all of a certain endpoint/data type.
 * In this application it is only used to get a list of all the monsters available via the API. The API does not return
 * an array of all the entities requested. It returns a JSON object that matches this entity's shape instead.
 *
 * @author jcmann
 */
public class GetAllResponse{

	/**
	 * The number of resources returned for the endpoint
	 */
	@JsonProperty("count")
	private int count;

	/**
	 * An array of objects representing all the resources returned.
	 */
	@JsonProperty("results")
	private List<GetAllResponseItem> results;


	/**
	 * Set count.
	 *
	 * @param count the count
	 */
	public void setCount(int count){
		this.count = count;
	}

	/**
	 * Get the count instance variable
	 * @return count instance variable
	 */
	public int getCount(){
		return count;
	}

	/**
	 * Set results.
	 *
	 * @param results the results
	 */
	public void setResults(List<GetAllResponseItem> results){
		this.results = results;
	}

	/**
	 * Get results list.
	 *
	 * @return the list
	 */
	public List<GetAllResponseItem> getResults(){
		return results;
	}
}