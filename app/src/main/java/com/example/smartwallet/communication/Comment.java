package com.example.smartwallet.communication;

import java.io.Serializable;

public class Comment implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	private Integer blogId;
	
	private User writer;
	
	private String content;
	
	private User replier;
	
	public Comment() {
		// TODO Auto-generated constructor stub
	}
	
	public Comment(Integer blogId, User writer, String content, User replier) {
		super();
		this.blogId = blogId;
		this.writer = writer;
		this.content = content;
		this.replier = replier;
	}

	public Integer getId() {
		return id;
	}

	public Integer getBlogId() {
		return blogId;
	}

	public User getWriter() {
		return writer;
	}

	public String getContent() {
		return content;
	}

	public User getReplier() {
		return replier;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setBlogId(Integer blogId) {
		this.blogId = blogId;
	}

	public void setWriter(User writer) {
		this.writer = writer;
	}

	public void setContent(String content) {
		this.content = content;
	}


	public void setReplier(User replier) {
		this.replier = replier;
	}

	@Override
	public String toString() {
		return "Comment [id=" + id + ", blogId=" + blogId +
				", writer=" + writer + ", content=" + content +
				", replier=" + replier + "]";
	}
	
}
