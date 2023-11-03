
public class KeyEntity extends Entity {

	public KeyEntity(Game game, String r, int newX, int newY) {
		super(r, newX, newY);
		// TODO Auto-generated constructor stub
	}
	
	public void collidedWith(Entity other) {
	     // collisions with aliens are handled in ShotEntity and ShipEntity
	   } // collidedWith

}
