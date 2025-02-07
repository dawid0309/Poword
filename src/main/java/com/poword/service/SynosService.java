package com.poword.service;

import com.poword.dao.SynosDao;
import com.poword.model.SynosModel;


public class SynosService {
    private SynosDao synosDao = new SynosDao();

    public void createRecord(SynosModel synosModel){
        if (synosModel == null) {
            throw new IllegalArgumentException("Synos object cannot be null");
        }
        if (synosModel.getWord() == null || synosModel.getWord().isEmpty()) {
            throw new IllegalArgumentException("Word cannot be null or empty");
        }
        if (synosModel.getSynos() == null || synosModel.getSynos().isEmpty()) {
            throw new IllegalArgumentException("Synos cannot be null or empty");
        }
        if (synosModel.getSource() == null || synosModel.getSource().isEmpty()) {
            throw new IllegalArgumentException("Source cannot be null or empty");
        }

        synosDao.createRecord(synosModel);
    }
}
