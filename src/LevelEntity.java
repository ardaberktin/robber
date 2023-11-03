
public class LevelEntity extends Entity {
	
	private Game game; 
	
	public LevelEntity(Game g, String r, int newX, int newY) {
		super(r, newX, newY);
		game = g;
	}

	
	/* collidedWith
	   * input: other - the entity with which the alien has collided
	   * purpose: notification that the alien has collided
	   *          with something
	   */
	   public void collidedWith(Entity other) {
	     // collisions with aliens are handled in ShotEntity and ShipEntity
	   } // collidedWith

} // LevelEntity
