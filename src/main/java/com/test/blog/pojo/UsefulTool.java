package com.test.blog.pojo;

public class UsefulTool {
    private Long id;
    private String link;
    private String websiteName;
    private String description;

    @Override
    public String toString() {
        return "UsefulTool{" +
                "id=" + id +
                ", link='" + link + '\'' +
                ", websiteName='" + websiteName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getWebsiteName() {
        return websiteName;
    }

    public void setWebsiteName(String websiteName) {
        this.websiteName = websiteName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
