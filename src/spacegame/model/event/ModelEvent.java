package spacegame.model.event;

import java.io.Serializable;

import org.bushe.swing.event.AbstractEventServiceEvent;


public abstract class ModelEvent extends AbstractEventServiceEvent implements Serializable, Cloneable {

	private static final long serialVersionUID = 3629097389962161230L;

	public ModelEvent(Object source) {
		super(source);
	}

	@Override
	public ModelEvent clone() {
		try {
			return (ModelEvent) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError("This should not ever happen");
		}
	}

}
