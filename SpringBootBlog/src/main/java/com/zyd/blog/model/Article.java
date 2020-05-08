package com.zyd.blog.model;

import java.time.LocalDateTime;
import javax.persistence.*;

public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String author;

    @Column(name = "user_id")
    private String userId;

    private String title;

    private Integer type;

    private String star;

    private String watch;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    private String content;

    private String summary;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * @return user_id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * @return star
     */
    public String getStar() {
        return star;
    }

    /**
     * @param star
     */
    public void setStar(String star) {
        this.star = star;
    }

    /**
     * @return watch
     */
    public String getWatch() {
        return watch;
    }

    /**
     * @param watch
     */
    public void setWatch(String watch) {
        this.watch = watch;
    }

    /**
     * @return created_time
     */
    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    /**
     * @param createdTime
     */
    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * @return update_time
     */
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * @return content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return summary
     */
    public String getSummary() {
        return summary;
    }

    /**
     * @param summary
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", author=").append(author);
        sb.append(", userId=").append(userId);
        sb.append(", title=").append(title);
        sb.append(", type=").append(type);
        sb.append(", star=").append(star);
        sb.append(", watch=").append(watch);
        sb.append(", createdTime=").append(createdTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", content=").append(content);
        sb.append(", summary=").append(summary);
        sb.append("]");
        return sb.toString();
    }
}