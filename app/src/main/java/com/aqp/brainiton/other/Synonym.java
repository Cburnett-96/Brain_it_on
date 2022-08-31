package com.aqp.brainiton.other;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class Synonym extends AsyncTask<String, Integer, String> {
    String myurl;
    Context context;
    TextView syn, an;

    public Synonym(Context context, TextView syn, TextView ant) {
        this.context = context;
        this.syn = syn;
        this.an = ant;
    }

    @Override
    protected String doInBackground(String... strings) {
        myurl = strings[0];
        URL to;
        try {
            to = new URL(myurl);
            BufferedReader br = new BufferedReader(new InputStreamReader(to.openStream()));
            String inputLine;

            while ((inputLine = br.readLine()) != null) {
                System.out.println(inputLine);
                return inputLine;
            }
        } catch (Exception e) {
            Log.e("Error Syn", "Error");
            return e.toString();
        }
        return "NULL";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        StringBuilder definition = new StringBuilder();
        StringBuilder anto = new StringBuilder();
        try {
            JSONObject json = new JSONObject(s);
            JSONObject results = new JSONObject();
            try {
                results = json.getJSONObject("adjective");
            } catch (Exception e) {
                try {
                    results = json.getJSONObject("verb");
                } catch (Exception b) {
                    try {
                        results = json.getJSONObject("similar terms");
                    } catch (Exception d) {
                        try {
                            results = json.getJSONObject("noun");
                        } catch (Exception c) {
                            Toast.makeText(context, "No Synonym For This Word", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            try {
                JSONArray res = results.getJSONArray("syn");
                for (int i = 0; i < 5 && i < res.length(); i++) {
                    definition.append(res.getString(i)).append("\n");
                }

            } catch (Exception e) {
                definition.append("No Synonym For This Word!\n");

            }
            anto.append("");
            try {
                JSONArray an = results.getJSONArray("ant");
                for (int i = 0; i < 5 && i < an.length(); i++) {
                    anto.append(an.getString(i)).append("\n");
                }
            } catch (Exception c) {
                anto.append("No Antonym For This Word!\n");
            }

            syn.setText(definition.toString());
            an.setText(anto.toString());

        } catch (Exception e) {
            //System.out.println(e);
            syn.setText("No Synonym For This Word!");
            an.setText("No Antonym For This Word!");
        }
    }
}
