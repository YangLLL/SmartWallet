package com.example.smartwallet.tool;

import java.io.Serializable;
import java.util.Date;
/** 
* @Fields id : 消息id
* @Fields sender : 发送方
* @Fields sendDate : 发送时间
* @Fields content : 消息内容
* @Fields receiver : 接收方
*/
public class Message implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	private Integer sender;
	
	private Date sendDate;
	
	private String content;
	
	private int receiver;
	
	public Message() {
		// TODO Auto-generated constructor stub
	}
	
	public Message(int sender,Date sendDate,String content,int receiver) {
		super();
		this.sender=sender;
		this.sendDate=sendDate;
		this.content=content;
		this.receiver=receiver;
	}

	public Integer getId() {
		return id;
	}

	public int getSender() {
		return sender;
	}

	public Date getSendDate() {
		return sendDate;
	}

	public String getContent() {
		return content;
	}

	public Integer getReceiver() {
		return receiver;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setSender(Integer sender) {
		this.sender = sender;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setReceiver(Integer receiver) {
		this.receiver = receiver;
	}
	
	@Override
	public String toString() {
		return "Messages [id=" + id + ", sender=" + sender + ", sendDate=" + sendDate + ", content=" + content
				+ ", receiver=" + receiver + "]";
	}

}
