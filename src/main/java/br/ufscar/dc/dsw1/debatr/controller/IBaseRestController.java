package br.ufscar.dc.dsw1.debatr.controller;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.simple.JSONObject;

public interface IBaseRestController<T> {
    default boolean isJsonValid(String jsonInString) {
        try {
            return new ObjectMapper().readTree(jsonInString) != null;
        } catch (IOException e) {
            return false;
        }
    } 

    // Obs.: Métodos privados em interfaces estão disponíveis apenas no Java 9+.
    public T parse(JSONObject json);
}
