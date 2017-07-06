package com.example.smartwallet.personalCenter;

/**
 * Created by lenovo on 2016/12/8.
 */
public class FriendsData  {
    private String friendsName;
    private String friendsHeadImage;


    public String getFriendsName() {
        return friendsName;
    }

    public void setFriendsName(String name) {
        this.friendsName = name;
    }

    public String getFriendsHeadImage() {
        return friendsHeadImage;
    }

    public void setFriendsHeadImage(String headImage) {
        this.friendsHeadImage = headImage;
    }


    @Override
    public String toString() {
        return "friendsData{" +
                "friendsName='" + friendsName + '\'' +
                ", friendsHeadImage='" + friendsHeadImage + '\'' +
                '}';
    }
}
