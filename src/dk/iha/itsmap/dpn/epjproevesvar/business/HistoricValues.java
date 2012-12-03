package dk.iha.itsmap.dpn.epjproevesvar.business;

public class HistoricValues {
	
    private String samplingTime;
    private String value;
    private Boolean outsideNormalRange;
    
    public String getSamplingTime() {
        return samplingTime;
    }
    
    public void setSamplingTime(String samplingTime){
        this.samplingTime=samplingTime;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value){
        this.value=value;
    }
    
    public Boolean getOutsideNormalRange() {
        return outsideNormalRange;
    }
    
    public void setOutsideNormalRange(Boolean outsideNormalRange){
        this.outsideNormalRange=outsideNormalRange;
    }
}
