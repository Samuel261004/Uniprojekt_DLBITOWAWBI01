package com.example.demo.model;

import jakarta.persistence.*;

@Entity(name="ANSWER")
public class answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Integer postid;
    private Long userid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userID", referencedColumnName = "USERID", insertable = false, updatable = false)
    private user user;
    private String content;

    public answer() {}
    public answer(int postid, Long userid, String content) {
        this.postid = postid;
        this.userid = userid;
        this.content = content;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getPostid() {
        return postid;
    }
    public void setPostid(int postid) {
        this.postid = postid;
    }
    public Long getUserid() {
        return userid;
    }
    public void setUserid(Long userid) {
        this.userid = userid;
    }
    public user getUser() {
        return user;
    }

    public void setUser(user user) {
        this.user = user;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
}
