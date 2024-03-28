package com.augmentaa.sparkev.model.signup.charger_list;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseChargerList {

@SerializedName("status")
@Expose
public Boolean status;
@SerializedName("message")
@Expose
public String message;
@SerializedName("count")
@Expose
public Integer count;

    @Override
    public String toString() {
        return "ResponseChargerList{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", count=" + count +
                ", data=" + data +
                '}';
    }

    @SerializedName("data")
@Expose
public List<Data> data = null;

public Boolean getStatus() {
return status;
}

public void setStatus(Boolean status) {
this.status = status;
}

public String getMessage() {
return message;
}

public void setMessage(String message) {
this.message = message;
}

public Integer getCount() {
return count;
}

public void setCount(Integer count) {
this.count = count;
}

public List<Data> getData() {
return data;
}

public void setData(List<Data> data) {
this.data = data;
}

}
