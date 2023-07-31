package com.atharvakale.facerecognition;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogsActivity extends AppCompatActivity {
    private DBHelper myDb;
    private HashMap<String, SimilarityClassifier.Recognition> registered = new HashMap<>(); //saved Faces

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

        myDb = new DBHelper(this);
        registered = readFromSP(); //Load saved faces from memory when app starts

        ArrayList<String> emp = new ArrayList<>();
        if (registered.isEmpty()) {
            emp.add("NA");
        } else {
            emp.add("Select User");
            String[] names = new String[registered.size()];
            String[] namesShow = new String[registered.size()];
            int i = 0;
            for (Map.Entry<String, SimilarityClassifier.Recognition> entry : registered.entrySet()) {
                String[] user = entry.getKey().split("&");
                if (user.length > 1) {
                    namesShow[i] = user[0] + " " + user[1];
                } else {
                    namesShow[i] = entry.getKey();
                }
                names[i] = entry.getKey();
                emp.add(namesShow[i]);
                i = i + 1;
            }
        }
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, emp);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getUserLogs(emp.get(position), position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getUserLogs(String user, int position) {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (position == 0) {
            Toast.makeText(this, "Select a proper user", Toast.LENGTH_SHORT).show();
            recyclerView.setAdapter(null);
        } else {
            List<Map<String, String>> list = myDb.getUserData(user.split(" ")[0]);
            LogsAdapter adapter = new LogsAdapter(this, list);
            recyclerView.setAdapter(adapter);
        }
    }

    private HashMap<String, SimilarityClassifier.Recognition> readFromSP() {
        SharedPreferences sharedPreferences = getSharedPreferences("HashMap", MODE_PRIVATE);
        String defValue = new Gson().toJson(new HashMap<String, SimilarityClassifier.Recognition>());
        String json = sharedPreferences.getString("map", defValue);
        TypeToken<HashMap<String, SimilarityClassifier.Recognition>> token = new TypeToken<HashMap<String, SimilarityClassifier.Recognition>>() {
        };
        HashMap<String, SimilarityClassifier.Recognition> retrievedMap = new Gson().fromJson(json, token.getType());
        //During type conversion and save/load procedure,format changes(eg float converted to double).
        //So embeddings need to be extracted from it in required format(eg.double to float).
        for (Map.Entry<String, SimilarityClassifier.Recognition> entry : retrievedMap.entrySet()) {
            float[][] output = new float[1][Constant.OUTPUT_SIZE];
            ArrayList arrayList = (ArrayList) entry.getValue().getExtra();
            arrayList = (ArrayList) arrayList.get(0);
            for (int counter = 0; counter < arrayList.size(); counter++) {
                output[0][counter] = ((Double) arrayList.get(counter)).floatValue();
            }
            entry.getValue().setExtra(output);

        }
        return retrievedMap;
    }
}