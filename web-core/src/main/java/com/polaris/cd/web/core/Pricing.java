package com.polaris.cd.web.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
public class Pricing {

    private final String origin;
    private final String destination;
    private final int price;

    @JsonCreator
    public Pricing(@JsonProperty("origin") String origin,
                   @JsonProperty("destination") String destination,
                   @JsonProperty("price") int price)
    {
        this.origin = origin;
        this.destination = destination;
        this.price = price;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pricing pricing = (Pricing) o;

        if (price != pricing.price) return false;
        if (origin != null ? !origin.equals(pricing.origin) : pricing.origin != null) return false;
        return !(destination != null ? !destination.equals(pricing.destination) : pricing.destination != null);

    }

    @Override
    public int hashCode() {
        int result = origin != null ? origin.hashCode() : 0;
        result = 31 * result + (destination != null ? destination.hashCode() : 0);
        result = 31 * result + price;
        return result;
    }

    @Override
    public String toString() {
        return "Pricing{" +
            "origin='" + origin + '\'' +
            ", destination='" + destination + '\'' +
            ", price=" + price +
            '}';
    }
}
