package com.integrity.feshar.response;

import com.google.gson.annotations.SerializedName;
import com.integrity.feshar.models.TvShowDetails;

public class TvShowDetailsResponse {

    @SerializedName("tvShow")
    private TvShowDetails tvShowDetails;

    public TvShowDetails getTvShowDetails() {
        return tvShowDetails;
    }
}
