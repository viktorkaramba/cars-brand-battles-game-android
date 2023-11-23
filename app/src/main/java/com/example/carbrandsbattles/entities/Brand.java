package com.example.carbrandsbattles.entities;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Brand {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("imageBrand")
    private String imageBrand;
}
