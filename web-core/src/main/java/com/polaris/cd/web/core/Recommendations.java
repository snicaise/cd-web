package com.polaris.cd.web.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 *
 */
public class Recommendations {

    private final String destination;

    private List<String> recommendations;

    @JsonCreator
    public Recommendations(@JsonProperty("destination") String destination,
                           @JsonProperty("recommendations") List<String> recommendations)
    {
        this.destination = destination;
        this.recommendations = recommendations;
    }

    public String getDestination() {
        return destination;
    }

    public List<String> getRecommendations() {
        return recommendations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Recommendations that = (Recommendations) o;

        if (destination != null ? !destination.equals(that.destination) : that.destination != null) return false;
        return !(recommendations != null ? !recommendations.equals(that.recommendations) : that.recommendations != null);

    }

    @Override
    public int hashCode() {
        int result = destination != null ? destination.hashCode() : 0;
        result = 31 * result + (recommendations != null ? recommendations.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Recommendations{" +
            "destination='" + destination + '\'' +
            ", recommendations=" + recommendations +
            '}';
    }
}
