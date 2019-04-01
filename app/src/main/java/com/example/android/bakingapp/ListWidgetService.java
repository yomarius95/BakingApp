package com.example.android.bakingapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingapp.object.Ingredient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.example.android.bakingapp.RecipeActivity.INGREDIENTS_LIST;

public class ListWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private ArrayList<Ingredient> mIngredientList;

    ListRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        // Get ingredients list from sharedPreferences
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        Gson gson = new Gson();
        String ingredientsJson = sharedPref.getString(INGREDIENTS_LIST, null);
        Type type = new TypeToken<ArrayList<Ingredient>>() {}.getType();
        mIngredientList = gson.fromJson(ingredientsJson, type);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mIngredientList.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.ingredients_list_item);

        views.setTextViewText(R.id.list_item_name, mIngredientList.get(i).getName());
        views.setTextViewText(R.id.list_item_quantity, String.valueOf(mIngredientList.get(i).getQuantity()));
        views.setTextViewText(R.id.list_item_measure, mIngredientList.get(i).getMeasure());

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}