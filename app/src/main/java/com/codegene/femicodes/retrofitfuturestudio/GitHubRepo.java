package com.codegene.femicodes.retrofitfuturestudio;

import com.google.gson.annotations.SerializedName;

/**
 * Created by femicodes on 12/3/2017.
 */

class GitHubRepo {
    private String name;

    public GitHubRepo() {
    }

    @SerializedName("name")
    public String getName() {
        return name;
    }
}
