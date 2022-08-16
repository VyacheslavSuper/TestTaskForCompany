package ru.super4121.testtask.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Country {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("flag")
    @Expose
    private String flagUrl;
}
