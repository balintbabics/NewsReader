package hu.bba.myfirstapp.models;

import com.google.gson.annotations.SerializedName;


public class ContentImageDataResponse {

    @SerializedName("responseData")
    ContentImageResult resultList;

    public ContentImageResult getResultList() {
        return resultList;
    }

    public void setResultList(ContentImageResult resultList) {
        this.resultList = resultList;
    }
}