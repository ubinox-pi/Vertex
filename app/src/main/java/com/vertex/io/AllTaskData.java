package com.vertex.io;

public class AllTaskData{
    String Id,name,description,LongDescription,Link,Imagelink,whatToDo;
    boolean isVisible;

    public AllTaskData(){
        // Default constructor required for Firebase
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

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public AllTaskData(String id, String name, String description, String longDescription, String link, String imagelink, String whatToDo, boolean isVisible) {
        this.Id = id;
        this.name = name;
        this.description = description;
        this.LongDescription = longDescription;
        this.Link = link;
        this.Imagelink = imagelink;
        this.whatToDo = whatToDo;
        this.isVisible = isVisible;
    }
}
