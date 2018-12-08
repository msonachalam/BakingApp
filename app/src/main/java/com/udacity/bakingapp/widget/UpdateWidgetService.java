package com.udacity.bakingapp.widget;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import java.util.ArrayList;

public class UpdateWidgetService extends IntentService {

    public static String ACTIVITY_INGREDIENTS_LIST = "ACTIVITY_INGREDIENTS_LIST";

    public UpdateWidgetService() {
        super("UpdateWidgetService");
    }

    public static void startBakingService(Context context, ArrayList<String> fromActivityIngredientsList) {
        Intent intent = new Intent(context, UpdateWidgetService.class);
        intent.putExtra(ACTIVITY_INGREDIENTS_LIST, fromActivityIngredientsList);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            ArrayList<String> fromActivityIngredientList = intent.getExtras().getStringArrayList(ACTIVITY_INGREDIENTS_LIST);
            handleActionUpdateBakingWidgets(fromActivityIngredientList);
        }
    }

    private void handleActionUpdateBakingWidgets(ArrayList<String> fromActivityIngredientsList) {
        Intent intent = new Intent("android.appwidget.action.APPWIDGET_UPDATE2");
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE2");
        intent.putExtra(ACTIVITY_INGREDIENTS_LIST, fromActivityIngredientsList);
        sendBroadcast(intent);
    }

}
