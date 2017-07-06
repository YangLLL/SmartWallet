package com.example.smartwallet.communication;

import java.io.Serializable;
import  java.util.Date;
import java.util.Set;

;
/** 
* @Fields id : 博文id
* @Fields title : 文章标题
* @Fields autor : 作者
* @Fields sendTime : 发布时间
* @Fields content : 文章内容
* @Fields likes : 点赞数
* @Fields cNumber : 评论数
* @Fields comments : 评论
*/ 
public class Blog implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Integer id;
	
	private String title;
	
	private User author;
	
	private Date sendTime;
	
	private String content;
	
	private String pictures;
	
	private int likes;
	
	private int cNumber;
	
	private Set<Comment> comments;
	
	public Blog() {
		// TODO Auto-generated constructor stub
	}

	public Blog(String title, User author, Date sendTime, String content, String pictures) {
		super();
		this.title = title;
		this.author = author;
		this.sendTime = sendTime;
		this.content = content;
		this.pictures = pictures;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPictures() {
		return pictures;
	}

	public void setPictures(String pictures) {
		this.pictures = pictures;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public int getcNumber() {
		return cNumber;
	}

	public void setcNumber(int cNumber) {
		this.cNumber = cNumber;
	}

	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	@Override
	public String toString() {
		return "Blog [id=" + id + ", title=" + title + ", author=" + author + ", sendTime=" + sendTime + ", content="
				+ content + ", pictures=" + pictures + ", likes=" + likes + ", cNumber=" + cNumber + ", comments="
				+ comments + "]";
	}
	
}
