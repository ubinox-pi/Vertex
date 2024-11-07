package com.vertex.io;

public class pending_list_data {
    String Id,name,description,LongDescription,Link,Imagelink,whatToDo;
    boolean isPending;
    private String Url;

    public pending_list_data(){
        // Default constructor required for Firebase
    }

    public void setPending(boolean pending) {
        isPending = pending;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLongDescription() {
        return LongDescription;
    }

    public void setLongDescription(String longDescription) {
        LongDescription = longDescription;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public String getImagelink() {
        return Imagelink;
    }

    public void setImagelink(String imagelink) {
        Imagelink = imagelink;
    }

    public String getWhatToDo() {
        return whatToDo;
    }

    public void setWhatToDo(String whatToDo) {
        this.whatToDo = whatToDo;
    }

    public boolean isPending() {
        return isPending;
    }

    public void setIsPending(boolean isPending) {
        this.isPending = isPending;
    }

    public pending_list_data(String id, String name, String description, String longDescription, String link, String imagelink, String whatToDo, boolean isPending) {
        this.Id = id;
        this.name = name;
        this.description = description;
        this.LongDescription = longDescription;
        this.Link = link;
        this.Imagelink = imagelink;
        this.whatToDo = whatToDo;
        this.isPending = isPending;
    }
}
