package spacegame.model.event;

import spacegame.model.DamageType;
import spacegame.model.IDamageable;

public abstract class DamageableEvent extends ModelEvent {

	private static final long serialVersionUID = 8274319369786837970L;

	public static final class Damaged extends DamageableEvent {

		private static final long serialVersionUID = 404380802116333298L;
		
		private float damageAmount;
		
		private DamageType damageType;

		public Damaged(IDamageable source, float damageAmount, DamageType damageType) {
			super(source);
			this.damageAmount = damageAmount;
			this.damageType=damageType;
		}

		public float getDamageAmount() {
			return damageAmount;
		}

		public DamageType getDamageType() {
			return damageType;
		}
		
	}
	
	public static final class Repaired extends DamageableEvent {

		private static final long serialVersionUID = -301334389234227669L;
		
		private float repairAmount;

		public Repaired(IDamageable source, float repairAmount) {
			super(source);
			this.repairAmount = repairAmount;
		}

		public float getRepairAmount() {
			return repairAmount;
		}

	}
	
	public static final class Death extends DamageableEvent {

		private static final long serialVersionUID = -5160547751914780241L;

		protected Death(IDamageable source) {
			super(source);
		}

	}

	protected DamageableEvent(IDamageable source) {
		super(source);
	}

	@Override
	public IDamageable getSource() {
		return (IDamageable) super.getSource();
	}

}
