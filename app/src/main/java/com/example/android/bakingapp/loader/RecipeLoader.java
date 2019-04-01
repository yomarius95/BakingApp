package com.example.android.bakingapp.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.text.TextUtils;
import com.example.android.bakingapp.RecipeUtils;
import com.example.android.bakingapp.object.Recipe;

import java.util.List;

public class RecipeLoader extends AsyncTaskLoader<List<Recipe>> {

    private String mUrl;

    public RecipeLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Recipe> loadInBackground() {
        if (TextUtils.isEmpty(mUrl)) {
            return null;
        }
        return RecipeUtils.fetchRecipeData(mUrl);
    }
}
