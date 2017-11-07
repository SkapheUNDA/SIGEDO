package la.netco.commons.utils;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;


public class LifeCycleListener implements PhaseListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6242131804784412138L;
	public void beforePhase(PhaseEvent event) {
        System.out.println("Fase Anterior: " + event.getPhaseId());
    }
    public void afterPhase(PhaseEvent event) {
        System.out.println("Fase Posterior: " + event.getPhaseId());
    }
    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }

}
