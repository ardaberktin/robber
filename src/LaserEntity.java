/* AlienEntity.java
 * March 27, 2006
 * Represents one of the aliens
 */
public class LaserEntity extends Entity {

  //private double moveSpeed; // horizontal speed

  private Game game; // the game in which the alien exists

  /* construct a new alien
   * input: game - the game in which the alien is being created
   *        r - the image representing the alien
   *        x, y - initial location of alien
   */
  
  public void changelazer() {
	  this.setX(this.getX() * -1);
  }
  
  public LaserEntity(Game g, String r, int newX, int newY) {
    super(r, newX, newY);  // calls the constructor in Entity
    game = g;
  } // constructor

  /* move
   * input: delta - time elapsed since last move (ms)
   * purpose: move alien
   */
  public void move (long delta){
    
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
