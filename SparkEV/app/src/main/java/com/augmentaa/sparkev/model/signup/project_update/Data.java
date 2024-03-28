package com.augmentaa.sparkev.model.signup.project_update;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

@SerializedName("id")
@Expose
public Integer id;
@SerializedName("project_id")
@Expose
public Integer projectId;
@SerializedName("version_type")
@Expose
public String versionType;
@SerializedName("launch_dt")
@Expose
public String launchDt;
@SerializedName("platform")
@Expose
public String platform;
@SerializedName("old_version_name")
@Expose
public String oldVersionName;
@SerializedName("new_version_name")
@Expose
public String newVersionName;
@SerializedName("old_version_code")
@Expose
public Integer oldVersionCode;
@SerializedName("new_version_code")
@Expose
public Integer newVersionCode;
@SerializedName("update_type")
@Expose
public String updateType;
@SerializedName("redirection_url")
@Expose
public String redirectionUrl;
@SerializedName("release_notes")
@Expose
public String releaseNotes;
@SerializedName("min_sdk_version")
@Expose
public Integer minSdkVersion;
@SerializedName("max_sdk_version")
@Expose
public Integer maxSdkVersion;
@SerializedName("code")
@Expose
public Object code;
@SerializedName("description")
@Expose
public Object description;
@SerializedName("status")
@Expose
public String status;
@SerializedName("created_date")
@Expose
public String createdDate;
@SerializedName("createdby")
@Expose
public Integer createdby;
@SerializedName("modify_date")
@Expose
public Object modifyDate;
@SerializedName("modifyby")
@Expose
public Object modifyby;

    @Override
    public String toString() {
        return "Data{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", versionType='" + versionType + '\'' +
                ", launchDt='" + launchDt + '\'' +
                ", platform='" + platform + '\'' +
                ", oldVersionName='" + oldVersionName + '\'' +
                ", newVersionName='" + newVersionName + '\'' +
                ", oldVersionCode=" + oldVersionCode +
                ", newVersionCode=" + newVersionCode +
                ", updateType='" + updateType + '\'' +
                ", redirectionUrl='" + redirectionUrl + '\'' +
                ", releaseNotes='" + releaseNotes + '\'' +
                ", minSdkVersion=" + minSdkVersion +
                ", maxSdkVersion=" + maxSdkVersion +
                ", code=" + code +
                ", description=" + description +
                ", status='" + status + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", createdby=" + createdby +
                ", modifyDate=" + modifyDate +
                ", modifyby=" + modifyby +
                '}';
    }
}