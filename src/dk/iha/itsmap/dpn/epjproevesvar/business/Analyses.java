 package dk.iha.itsmap.dpn.epjproevesvar.business;
import java.util.List;
public class Analyses {
    private String analysisName;
    private String value;
    private Boolean outsideNormalRange;
    private List<HistoricValues> historicValues;
    private String min;
    private String max;

    public String getAnalysisName(){
        return analysisName;
    }
    
    public void setAnalysisName(String analysisName){
        this.analysisName=analysisName;
    }
    
    public String getValue(){
        return value;
    }
    
    public void setValue(String value){
        this.value=value;
    }
    
    public Boolean getOutsideNormalRange(){
        return outsideNormalRange;
    }
    
    public void setOutsideNormalRange(Boolean outsideNormalRange){
        this.outsideNormalRange=outsideNormalRange;
    }
    
    public String getMin() {
        return min;
    }
    
    public void setMin(String min){
        this.min=min;
    }
    
    public String getMax() {
        return max;
    }
    
    public void setMax(String max){
        this.max=max;
        
    }
    public List<HistoricValues> getHistoricValues() {
        return historicValues;   
    }
    
    public void setHistoricValues(List<HistoricValues> historicValues){
        this.historicValues=historicValues;
    }
}