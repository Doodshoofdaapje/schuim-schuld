package com.boris.schuimschuld.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;

public class DynamicLayoutFillers {

    public static void Linear(Context context, LinearLayout layout, ArrayList<? extends View> cards, Integer marginBottom) {
        layout.removeAllViews();
        for (View card : cards) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 0, marginBottom);
            card.setLayoutParams(params);
            layout.addView(card);
        }
    }

    public static void Table(Context context, TableLayout table, ArrayList<? extends View> cards, int maxColumnCount) {
        table.removeAllViews();
        int cardCounter = 0;
        TableRow row = null;
        for (View card : cards) {
            if (cardCounter % maxColumnCount == 0) {
                row = new TableRow(context);
                table.addView(row);
            }

            TableRow.LayoutParams params = new TableRow.LayoutParams();
            params.setMargins(25, 0, 25, 50);
            card.setLayoutParams(params);
            row.addView(card);

            cardCounter++;
        }
    }
}