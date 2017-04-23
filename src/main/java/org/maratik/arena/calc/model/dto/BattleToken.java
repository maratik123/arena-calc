package org.maratik.arena.calc.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by maratik.
 */
public class BattleToken {
    @JsonProperty("token")
    private String token;

    @Override
    public String toString() {
        return "BattleToken{" +
                "token='" + token + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BattleToken that = (BattleToken) o;

        return token != null ? token.equals(that.token) : that.token == null;
    }

    @Override
    public int hashCode() {
        return token != null ? token.hashCode() : 0;
    }

    public String getToken() {

        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public BattleToken() {

    }

    public BattleToken(String token) {

        this.token = token;
    }
}
