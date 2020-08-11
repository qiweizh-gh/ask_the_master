package com.atm.askthemaster.model;

import android.content.res.Resources;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;

import com.atm.askthemaster.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CategoryBuilderHelper {
    public static void createCategories(DatabaseReference mReference, Resources resources) {

        InputStream is = resources.openRawResource(R.raw.categories);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8)
        );

        int order = 0;
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                String parentId = orderToId(order);
                saveNodeToDB(order++, tokens[0], null, (tokens.length - 1), mReference);
                for (int i = 1; i < tokens.length; i++) {
                    saveNodeToDB(order++, tokens[i], parentId, 0, mReference);
                }
            }
        } catch (IOException e) {
            Log.d("test", "Error on line: " + line, e);
            e.printStackTrace();
        }
    }

    private static void saveNodeToDB(int order, String name, String parentId, int numChild, DatabaseReference mReference) {
        List<String> childIds = new ArrayList<>();
        if (numChild > 0) {
            for (int i = 1; i <= numChild; i++) {
                childIds.add(orderToId(order + i));
            }
        }
        Category node = new Category.CategoryBuilder()
                .setCid(orderToId(order))
                .setName(name)
                .setParentId(parentId)
                .setChildIds(childIds)
                .build();
        mReference.child(node.getCid()).setValue(node);
    }

    private static String orderToId(int order) {
        return order < 10 ? "c0" + order : "c" + order;
    }
}