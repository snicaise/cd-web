package com.polaris.cd.web.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 *
 */
public class Quotation {

    private String origin;
    private String destination;

    private int price;

    private List<String> recommendations;

    public Quotation() {

    }

    @JsonCreator
    public Quotation(@JsonProperty("origin")String origin,
                     @JsonProperty("destination")String destination,
                     @JsonProperty("price")int price,
                     @JsonProperty("recommendations")List<String> recommendations) {
        this.origin = origin;
        this.destination = destination;
        this.price = price;
        this.recommendations = recommendations;
    }

    @JsonProperty
    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    @JsonProperty
    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    @JsonProperty
    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @JsonProperty
    public List<String> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<String> recommendations) {
        this.recommendations = recommendations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Quotation quotation = (Quotation) o;

        if (price != quotation.price) return false;
        if (origin != null ? !origin.equals(quotation.origin) : quotation.origin != null) return false;
        if (destination != null ? !destination.equals(quotation.destination) : quotation.destination != null)
            return false;
        return !(recommendations != null ? !recommendations.equals(quotation.recommendations) : quotation.recommendations != null);

    }

    @Override
    public int hashCode() {
        int result = origin != null ? origin.hashCode() : 0;
        result = 31 * result + (destination != null ? destination.hashCode() : 0);
        result = 31 * result + price;
        result = 31 * result + (recommendations != null ? recommendations.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Quotation{" +
            "origin='" + origin + '\'' +
            ", destination='" + destination + '\'' +
            ", price=" + price +
            ", recommendations=" + recommendations +
            '}';
    }
}
