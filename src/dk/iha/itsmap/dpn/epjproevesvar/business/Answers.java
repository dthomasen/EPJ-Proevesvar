package dk.iha.itsmap.dpn.epjproevesvar.business;

import java.util.List;

public class Answers {
	private List<Analyses> analyses;
    private String samplingTime;
    
    public String getSamplingTime(){
        return samplingTime;
    }
    
    public void setSamplingTime(String samplingTime){
        this.samplingTime=samplingTime;
    }
    
    public List<Analyses> getAnalyses(){
        return analyses;
    }
    
    public void setAnalyses(List<Analyses> analyses){
        this.analyses=analyses;
    }
}