package com.chemguan.controller.socket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;

public class ServerMessage implements Serializable {
	@JsonIgnore
	private static final long serialVersionUID = 1L;
	

	private MessageType type;
	
	private String userParam;
	
	private String content;
	

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	public String getUserParam() {
		return userParam;
	}

	public void setUserParam(String userParam) {
		this.userParam = userParam;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public ServerMessage(MessageType type, String userParam, String content) {
		super();
		this.type = type;
		this.userParam = userParam;
		this.content = content;
	}
	
	public String toJson(){
		String json=null;
		ObjectMapper mapper=new ObjectMapper();
		try {
			json=mapper.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

}
