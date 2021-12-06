package com.jenmann.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This represents a single item in the results array returned by the DND 5e API. Each item does not contain a full
 * entity of whatever type is being requested, but only the entity's name, index, and URL in the API. The name
 * and index are returned by the full entity endpoints, but the URL is not.
 *
 * @author jcmann
 */
public class GetAllResponseItem {

	/**
	 * The entity's name
	 */
	@JsonProperty("name")
	private String name;

	/**
	 * The lower snake case index, unique identiifer, of the resource
	 */
	@JsonProperty("index")
	private String index;

	/**
	 * The API URL for the resource, if you were to make a request to get all of its information (the full resource)
	 */
	@JsonProperty("url")
	private String url;

	/**
	 * Set name.
	 *
	 * @param name the name
	 */
	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	/**
	 * Set index.
	 *
	 * @param index the index
	 */
	public void setIndex(String index){
		this.index = index;
	}

	/**
	 * Get index string.
	 *
	 * @return the string
	 */
	public String getIndex(){
		return index;
	}

	/**
	 * Set url.
	 *
	 * @param url the url
	 */
	public void setUrl(String url){
		this.url = url;
	}

	/**
	 * Get url string.
	 *
	 * @return the string
	 */
	public String getUrl(){
		return url;
	}
}