package com.example.cookingovereasy;

import android.content.Context;

import com.example.cookingovereasy.Models.IngredientResponse;
import com.example.cookingovereasy.Models.RandomRecipeApiResponse;
import com.example.cookingovereasy.Models.RecipeDetailsResponse;
import com.example.cookingovereasy.listeners.IngredientResponseListener;
import com.example.cookingovereasy.listeners.RandomRecipeResponseListener;
import com.example.cookingovereasy.listeners.RecipeDetailsListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class RequestManager {
    Context context;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.spoonacular.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public RequestManager(Context context) {
        this.context = context;
    }

    /**
     * Call this from the main activity to get the recipes
     * @param listener
     */
    public void getRandomRecipes(RandomRecipeResponseListener listener, List<String> tags) {
        CallRandomRecipes callRandomRecipes = retrofit.create(CallRandomRecipes.class);
        Call<RandomRecipeApiResponse> call = callRandomRecipes.callRandomRecipe(context.getString(R.string.spoonKey), "10", tags);
        call.enqueue(new Callback<RandomRecipeApiResponse>() {
            @Override
            public void onResponse(Call<RandomRecipeApiResponse> call, Response<RandomRecipeApiResponse> response) {
                if (!response.isSuccessful()){
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<RandomRecipeApiResponse> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });
    }

    public void getIngredients(IngredientResponseListener listener, boolean includeChildren, String query) {
        //CallRandomRecipes callRandomRecipes = retrofit.create(CallRandomRecipes.class);
        CallIngredients callIngredients = retrofit.create(CallIngredients.class);
        Call<IngredientResponse> call = callIngredients.callIngredient(context.getString(R.string.spoonKey), "10", query, includeChildren);
        call.enqueue(new Callback<IngredientResponse>() {
            @Override
            public void onResponse(Call<IngredientResponse> call, Response<IngredientResponse> response) {
                if (!response.isSuccessful()){
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<IngredientResponse> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });
    }

    public void getRecipeDetails(RecipeDetailsListener listener, int id) {
        CallRecipeDetails callRecipeDetails = retrofit.create(CallRecipeDetails.class);
        Call<RecipeDetailsResponse> call = callRecipeDetails.callRecipeDetails(id, context.getString(R.string.spoonKey));
        call.enqueue(new Callback<RecipeDetailsResponse>() {
            @Override
            public void onResponse(Call<RecipeDetailsResponse> call, Response<RecipeDetailsResponse> response) {
                if (!response.isSuccessful()) {
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<RecipeDetailsResponse> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });
    }

    /**
     * Interface that provides the call method for calling random recipes
     */
    private interface CallRandomRecipes {
        @GET("recipes/random")
        Call<RandomRecipeApiResponse> callRandomRecipe(
                @Query("apiKey") String apiKey,
                @Query("number") String number,
                @Query("tags") List<String> tags
        );
    }

    private interface CallIngredients {
        @GET("food/ingredients/search")
        Call<IngredientResponse> callIngredient(
                @Query("apiKey") String apiKey,
                @Query("number") String number,
                @Query("query") String query,
                @Query("addChildren") boolean includeChildren
        );
    }

    private interface CallRecipeDetails {
        @GET("recipes/{id}/information")
        Call<RecipeDetailsResponse> callRecipeDetails(
                @Path("id") int id,
                @Query("apiKey") String apiKey
        );
    }
}
