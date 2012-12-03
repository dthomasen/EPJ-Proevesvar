package dk.iha.itsmap.dpn.epjproevesvar.business;

import java.util.List;
public class LabResult {
    private NextRequisition nextRequisition;
    private List<Answers> answers;

    public NextRequisition getNextRequisition() {
        return nextRequisition;
    }

    public void setNextRequisition(NextRequisition nextRequisition){
        this.nextRequisition=nextRequisition;
    }
    
    public List<Answers> getAnswers() {
        return answers;
    }
    
    public void setAnswers(List<Answers> answers){
        this.answers=answers;
    }
}