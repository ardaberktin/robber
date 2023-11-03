/* AlienEntity.java
 * March 27, 2006
 * Represents one of the aliens
 */
public class PoliceEntity extends Entity {

  private double moveSpeed = 100; // horizontal speed
  
  private long changeInterval = 500; // interval between cars (ms)
  private long lastChange = 0;
  boolean moveRight = true;

  private Game game; // the game in which the alien exists

  /* construct a new alien
   * input: game - the game in which the alien is being created
   *        r - the image representing the alien
   *        x, y - initial location of alien
   */
  public PoliceEntity(Game g, String r, int newX, int newY, int direction) {
    super(r, newX, newY);  // calls the constructor in Entity
    game = g;
    dx = moveSpeed * direction;  // start off moving left
  } // constructor

  /* move
   * input: delta - time elapsed since last move (ms)
   * purpose: move alien
   */
  public void move (long delta){
	  
	  
    // if we reach left side of screen and are moving left
    // request logic update
    if ((dx < 0) && (x < 0)) {
      game.removeEntity(this);   // logic deals with moving entities
                            // in other direction and down screen
    } // if

    
    
    if((System.currentTimeMillis() - lastChange) > changeInterval){
    	lastChange = System.currentTimeMillis();
    	if (this.getX() < 50) {
    		moveRight = true;
    	}
    	else if (this.getX() > 556) {
    		moveRight = false;
    	}
    	else if (Math.random() >= .5) {
    		moveRight = false;
    	} 
    	else {
    		moveRight = true;
    	}
    }
    
    
 // proceed with normal move
    if (moveRight) {
    	super.move(delta);
    } else {
    	super.move(-delta);
    }
  } // move

  /* doLogic
   * Updates the game logic related to the aliens,
   * ie. move it down the screen and change direction
   */
  public void doLogic() {
    // swap horizontal direction and move down screen 10 pixels
    dx *= -1;
    dy *= -1;

    // if bottom of screen reached, player dies
    if (y > 570) {
      game.notifyDeath();
    } // if
  } // doLogic

  /* collidedWith
   * input: other - the entity with which the alien has collided
   * purpose: notification that the alien has collided
   *          with something
   */
   public void collidedWith(Entity other) {
     // collisions with aliens are handled in ShotEntity and ShipEntity
   } // collidedWith
  
} // AlienEntity class