package com.polaris.cd.web.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorMessage {

    private final String message;
    private final String details;

    public ErrorMessage(String message) {
        this(message, null);
    }

    @JsonCreator
    public ErrorMessage(@JsonProperty("message") String message, @JsonProperty("details") String details) {
        this.message = message;
        this.details = details;
    }

    public String getMessage() {
        return message;
    }

    public String getDetails() {
        return details;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ErrorMessage that = (ErrorMessage) o;

        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        return !(details != null ? !details.equals(that.details) : that.details != null);

    }

    @Override
    public int hashCode() {
        int result = message != null ? message.hashCode() : 0;
        result = 31 * result + (details != null ? details.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ErrorMessage{" +
            "message='" + message + '\'' +
            ", details='" + details + '\'' +
            '}';
    }
}
