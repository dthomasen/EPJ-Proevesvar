package dk.iha.itsmap.dpn.epjproevesvar.business;

import java.util.List;

public class NextRequisition {
    private String samplingTime;
    private List<UAnalyses> analyses;
    
    public String getSamplingTime() {
        return samplingTime;
    }
    
    public void setSamplingTime(String samplingTime){
        this.samplingTime=samplingTime;
    }
    
    public List<UAnalyses> getAnalyses() {
        return analyses;
    }
    
    public void setAnalyses(List<UAnalyses> analyses){
        this.analyses=analyses;
    }
}