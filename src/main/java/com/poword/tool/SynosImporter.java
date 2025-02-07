package com.poword.tool;

import com.poword.dao.StarDictDao;
import com.poword.dao.SynosDao;
import com.poword.model.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONObject;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;


public class SynosImporter{
    private SynosDao synosDao = new SynosDao();
    private StarDictDao starDictDao = new StarDictDao();

    public void insertSynos(String filePath){
        ArrayList<JSONObject> jsonObjects = getJsonObjects(filePath);
        ArrayList<SynosModel> synosModelList = getSynosArray(jsonObjects);
        for (SynosModel synosModel : synosModelList) {
            int id = starDictDao.getIdByWord(synosModel.getWord());
            if (id == 0){
                continue;
            }
            synosModel.setId(id);
            synosDao.createRecord(synosModel);
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

    private ArrayList<SynosModel> getSynosArray(ArrayList<JSONObject> jsonList) {
        ArrayList<SynosModel> synosModelList = new ArrayList<SynosModel>();
        // 提取 synos 字段
        for (JSONObject jsonObject : jsonList) {
            try{
                SynosModel synosModel = new SynosModel();
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
                synosModel.setWord(word);
                synosModel.setSynos(synosJsonArray.toString());
                synosModel.setSource(source);
                synosModelList.add(synosModel);
            } catch (JSONException e) {
                // System.err.println("Skipping invalid line: " + jsonObject.toString());
            }
        }
        return synosModelList;
    }
}
