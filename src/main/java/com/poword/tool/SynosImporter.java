package com.poword.tool;

import com.google.gson.JsonObject;
import com.poword.dao.StarDictDao;
import com.poword.dao.SynosDao;
import com.poword.helper.DatabaseConnectHelper;
import com.poword.model.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.Identity;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;
import org.json.JSONStringer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class SynosImporter{
    private SynosDao synosDao = new SynosDao();
    private StarDictDao starDictDao = new StarDictDao();

    public void insertSynos(String filePath){
        ArrayList<JSONObject> jsonObjects = getJsonObjects(filePath);
        ArrayList<Synos> synosList = getSynosArray(jsonObjects);
        for (Synos synos : synosList) {
            int id = starDictDao.getIdByWord(synos.getWord());
            if (id == 0){
                continue;
            }
            synos.setId(id);
            synosDao.createRecord(synos);
        }
    }


    /**
     * @param filePath
     */
    private ArrayList<JSONObject> getJsonObjects(String filePath){
        ArrayList<JSONObject> jsonList = new ArrayList<JSONObject>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                try{
                    JSONObject jsonObject = new JSONObject(currentLine);
                    jsonList.add(jsonObject);
                } catch (JSONException e) {
                    // System.err.println("Skipping invalid line: " + currentLine);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonList;
    }

    private ArrayList<Synos> getSynosArray(ArrayList<JSONObject> jsonList) {
        ArrayList<Synos> synosList = new ArrayList<Synos>();
        // 提取 synos 字段
        for (JSONObject jsonObject : jsonList) {
            try{
                Synos synos = new Synos();
                String word = jsonObject.getJSONObject("content")
                        .getJSONObject("word")
                        .getString("wordHead");
                JSONArray synosJsonArray = jsonObject
                        .getJSONObject("content")
                        .getJSONObject("word")
                        .getJSONObject("content")
                        .getJSONObject("syno")
                        .getJSONArray("synos");
                String source = jsonObject.getString("bookId").split("_")[0];
                synos.setWord(word);
                synos.setSynos(synosJsonArray.toString());
                synos.setSource(source);
                synosList.add(synos);
            } catch (JSONException e) {
                // System.err.println("Skipping invalid line: " + jsonObject.toString());
            }
        }
        return synosList;
    }
}