/* AlienEntity.java
 * March 27, 2006
 * Represents one of the aliens
 */
public class WalkingPoliceEntity extends Entity {
	int point1;
	int point2;

  //private double moveSpeed; // horizontal speed

  private Game game; // the game in which the alien exists

  /* construct a new alien
   * input: game - the game in which the alien is being created
   *        r - the image representing the alien
   *        x, y - initial location of alien
   */
  public WalkingPoliceEntity(Game g, String r, int newX, int newY, int p1, int p2, int speed) {
    super(r, newX, newY);  // calls the constructor in Entity
    game = g;
    dx = speed;
    point1 = p1;
    point2 = p2;
  } // constructor

  /* move
   * input: delta - time elapsed since last move (ms)
   * purpose: move alien
   */
  public void move (long delta){
    // if we reach left side of screen and are moving left
    // request logic update
    if ((dx < 0) && (x < point1)) {
      dx *= -1;
    } // if

    // if we reach right side of screen and are moving right
    // request logic update
    if ((dx > 0) && (x > point2)) {
    	dx *= -1;
    } // if
    
    // proceed with normal move
    super.move(delta);
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
    /*if (y > 570) {
      game.notifyDeath();
    } // if*/
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