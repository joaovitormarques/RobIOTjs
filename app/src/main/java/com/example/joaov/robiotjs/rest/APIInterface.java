package com.example.joaov.robiotjs.rest;

/**
 * Created by joaov on 14-Nov-17.
 */

import com.example.joaov.robiotjs.Model.Task;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface APIInterface {
        @POST("/tasks")
        Call<Task> executeTask(@Body Task task);
}