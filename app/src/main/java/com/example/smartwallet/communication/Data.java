package com.example.smartwallet.communication;

import java.util.List;

/**
 * Created by lenovo on 2016/8/20.
 */
public class Data {
    private String name;
    private String headImage;
    private String itemText;
    private String itemImage;
    private List<String> commentText;
    private String date;
    private String phone;
    private String email;
    private  String title;
    private int authorId;
    private int blogId;
    private String authorBirthday;
    private String authorSex;
    private String authorAddress;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public void setItemText(String itemText) {
        this.itemText = itemText;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public void setDate(String date){
        this.date = date;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public void setBlogId(int blogId) {
        this.blogId = blogId;
    }

    public void setAuthorAddress(String authorAddress) {
        this.authorAddress = authorAddress;
    }

    public void setAuthorBirthday(String authorBirthday) {
        this.authorBirthday = authorBirthday;
    }

    public void setauthorSex(String getauthorSex) {
        this.authorSex = getauthorSex;
    }

    public String getAuthorSex() {
        return authorSex;
    }

    public String getAuthorBirthday() {
        return authorBirthday;
    }

    public String getAuthorAddress() {
        return authorAddress;
    }

    public int getBlogId() {
        return blogId;
    }

    public int getAuthorId() {
        return authorId;
    }

    public String getTitle() {
        return title;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getDate(){
        return date;
    }

    public String getItemImage() {
        return itemImage;
    }

    public String getItemText() {

        return itemText;
    }

    public List<String> getCommentText() {
        return commentText;
    }



    public void setCommentText(List<String> commentText) {
        this.commentText = commentText;
    }

//    @Override
//    public String toString() {
//        return "Data{" +
//                "name='" + name + '\'' +
//                ", headImage='" + headImage + '\'' +
//                ", itemText='" + itemText + '\'' +
//                ", itemImage='" + itemImage + '\'' +
//                ", commentText='" + commentText + '\'' +
//                '}';
//    }


    @Override
    public String toString() {
        return "Data{" +
                "name='" + name + '\'' +
                ", headImage='" + headImage + '\'' +
                ", itemText='" + itemText + '\'' +
                ", itemImage='" + itemImage + '\'' +
//                ", commentText=" + commentText +
                ", commentText='" + commentText + '\'' +
                ", date='" + date + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", title='" + title + '\'' +
                ", authorId=" + authorId +
                '}';
    }
}
