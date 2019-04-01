package com.example.android.bakingapp;

import android.text.TextUtils;
import com.example.android.bakingapp.object.Ingredient;
import com.example.android.bakingapp.object.Recipe;
import com.example.android.bakingapp.object.Step;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import timber.log.Timber;

public final class RecipeUtils {

    public static ArrayList<Recipe> fetchRecipeData(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Timber.e("Error closing input stream", e);
        }

        return extractRecipeFromJson(jsonResponse);
    }
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Timber.e("Error with creating URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Timber.e("Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Timber.e("Problem retrieving the recipe JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static ArrayList<Recipe> extractRecipeFromJson(String recipeJSON) {
        if (TextUtils.isEmpty(recipeJSON)) {
            return null;
        }

        ArrayList<Recipe> recipes = new ArrayList<>();

        try {
            JSONArray results = new JSONArray(recipeJSON);
            for (int i = 0; i < results.length(); i++) {
                JSONObject recipe = results.getJSONObject(i);
                String name = recipe.getString("name");
                int servings = recipe.getInt("servings");
                ArrayList<Ingredient> ingredientsList = new ArrayList<>();
                if (recipe.optJSONArray("ingredients") != null) {
                    JSONArray ingredients = recipe.getJSONArray("ingredients");
                    for (int j = 0; j < ingredients.length(); j++) {
                        JSONObject ingredient = ingredients.getJSONObject(j);
                        String ingredientName = ingredient.getString("ingredient");
                        String measure = ingredient.getString("measure");
                        double quantity = ingredient.getDouble("quantity");

                        ingredientsList.add(new Ingredient(ingredientName, measure, quantity));
                    }
                }
                ArrayList<Step> stepsList = new ArrayList<>();
                if (recipe.optJSONArray("steps") != null) {
                    JSONArray steps = recipe.getJSONArray("steps");
                    for (int k = 0; k < steps.length(); k++) {
                        JSONObject step = steps.getJSONObject(k);
                        String shortDesc = step.getString("shortDescription");
                        String description = step.getString("description");
                        String mediaUrl = null;
                        if (!TextUtils.isEmpty(step.optString("videoURL")))
                        {
                            mediaUrl = step.getString("videoURL");
                        } else if (!TextUtils.isEmpty(step.optString("thumbnailURL"))){
                            mediaUrl = step.getString("thumbnailURL");
                        }

                        stepsList.add(new Step(shortDesc, description, mediaUrl));
                    }
                }

                recipes.add(new Recipe(name, servings, ingredientsList, stepsList));
            }
            return recipes;
        } catch (JSONException e) {
            Timber.e("Problem parsing the recipe JSON results", e);
        }
        return null;
    }
}
