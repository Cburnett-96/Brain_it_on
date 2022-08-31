package com.aqp.brainiton.other;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class SynonymOnly extends AsyncTask<String, Integer, String> {

    String myurl;
    Context context;
    TextView def;

    public SynonymOnly(Context context, TextView def){
        this.context = context;
        this.def = def;
    }
    @Override
    protected String doInBackground(String... params) {
        myurl = params[0];
        URL to;
        try {
            to =new URL (myurl);
            BufferedReader br=new BufferedReader(new InputStreamReader(to.openStream()));
            String inputLine;

            while((inputLine=br.readLine())!=null){
                System.out.println(inputLine); // in the form noun|syn|world ..
                return inputLine;
            }
        }catch (Exception e){
            Log.e("Error Syn", "Error");
            return e.toString();
        }

        return "NULL";
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        StringBuilder definition = new StringBuilder();
        try{
            JSONObject json = new JSONObject(s);
            JSONObject results = new JSONObject();
            try{
                results = json.getJSONObject("adjective");
            }catch(Exception e){
                try{
                    results = json.getJSONObject("verb");
                }catch(Exception b){
                    try{
                        results = json.getJSONObject("noun");
                    }catch(Exception c){
                        Toast.makeText(context, "No Synonym For This Word", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            try{
                JSONArray res = results.getJSONArray("syn");
                for (int i=0;i<5 && i < res.length();i++){
                    definition.append(res.getString(i)).append("\n");
                }

            }catch(Exception e){
                definition.append("No Synonym For This Word!\n");

            }
            def.setText(definition.toString());

        }catch(Exception e){
            def.setText("No Synonym For This Word!");
        }
    }
}
