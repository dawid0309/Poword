package com.poword.service;

import com.poword.dao.SynosDao;
import com.poword.model.Synos;


public class SynosService {
    private SynosDao synosDao = new SynosDao();

    public void createRecord(Synos synos){
        if (synos == null) {
            throw new IllegalArgumentException("Synos object cannot be null");
        }
        if (synos.getWord() == null || synos.getWord().isEmpty()) {
            throw new IllegalArgumentException("Word cannot be null or empty");
        }
        if (synos.getSynos() == null || synos.getSynos().isEmpty()) {
            throw new IllegalArgumentException("Synos cannot be null or empty");
        }
        if (synos.getSource() == null || synos.getSource().isEmpty()) {
            throw new IllegalArgumentException("Source cannot be null or empty");
        }
        
        synosDao.createRecord(synos);
    }
}
