package me.gann.flaviarbottlescraper.model;

import lombok.Data;

@Data
public class LiquorBottle {

    private String name;
    private String edition;
    private String type;
    private Double rating;
    private Double price;

}
