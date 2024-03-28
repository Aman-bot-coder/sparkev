package com.augmentaa.sparkev.model.signup.mobile_static_text;

public class Data {
    public int id;
    public int project_id;
    public String title1;
    public String title2;
    public String desc1;
    public String desc2;
    public String module;
    public String image_url;
    public String collapsable;
    public String status;


    @Override
    public String toString() {
        return "Data{" +
                "id=" + id +
                ", project_id=" + project_id +
                ", title1='" + title1 + '\'' +
                ", title2='" + title2 + '\'' +
                ", desc1='" + desc1 + '\'' +
                ", desc2='" + desc2 + '\'' +
                ", module='" + module + '\'' +
                ", image_url='" + image_url + '\'' +
                ", collapsable='" + collapsable + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
