/* Game.java
 * Space Invaders Main Program
 *
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.net.URL;
import java.util.ArrayList;

public class Game extends Canvas {


      	private BufferStrategy strategy;   // take advantage of accelerated graphics
        private boolean waitingForKeyPress = true;  // true if game held up until
       
     //   private final int GAME_WIDTH = 1000;  // width of game
	//	private final int GAME_HEIGHT = 685;  // height of game                         // a key is pressed
        
        private final int originalTileSize = 16; // 16 x 16 tile
        final int scale = 3;
        
        public final int tileSize = originalTileSize * scale; // 48 x 48 tile
        public final int maxScreenCol = 16;
        public final int maxScreenRow = 12;
        public final int GAME_WIDTH = tileSize * maxScreenCol; // 768 pixels
        public final int GAME_HEIGHT = tileSize * maxScreenRow; // 576 pixels

		private boolean leftPressed = false;  // true if left arrow key currently pressed
        private boolean rightPressed = false; // true if right arrow key currently pressed
      //  private boolean firePressed = false; // true if firing
        private boolean upPressed = false; // true if up is pressed
        private boolean downPressed = false; // true if down is pressed
        
        public boolean keyOn = false;
        
        private boolean isJumping = false; // true if jumping
        private int jumpTime = 0; // true if up is pressed
        private long timeStart;
        private long endTime;

        public boolean gameRunning = true;
        private ArrayList entities = new ArrayList(); // list of entities
                                                      // in game
        private ArrayList removeEntities = new ArrayList(); // list of entities
                                                            // to remove this loop
        public Entity ship;  // the ship
        public Entity key;
        public Entity coin;
        private double moveSpeed = 432; // hor. vel. of ship (px/s)
        private long lastFire = 0; // time last shot fired
        private long firingInterval = 15; // interval between shots (ms)
        private long lastLazer = 0; // time last lazer chnge
        private long lastFlash = 0; // interval between lazer (ms)
        private long lastMove = 0; // time last lazer change
       // private long carInterval = 5000; // interval between cars (ms)
        private long lastCar1 = 0;
        private long lastCar2 = 0;
        private long lastCar3 = 0;
        private long lastCar4 = 0;
        private long lastCar5 = 0;
        private long lastCar6 = 0;
        private long lastCar7 = 0;
        private long lastCar8 = 0;
        private int alienCount; // # of aliens left on screen

        private String message = ""; // message to display while waiting
                                     // for a key press

        private boolean logicRequiredThisLoop = false; // true if logic
                                                       // needs to be 
                                                       // applied this loop
        
        private String [] levels = new String [] {"maps/map.txt" , "maps/secondMap.txt", 
        		"maps/thirdMap.txt", "maps/fourthMap.txt" , "maps/fifthMap.txt", "maps/sixthMap.txt", "maps/seventhMap.txt", "maps/eighthMap.txt"};
        
 
    	int carNum = 0;
    	boolean train = true;
    	boolean levelStarted = true;
      	int[] carLocat1 = {288, 288, 350, 1, 1, 1};
    	int[] carLocat2 = {240, 240, 450, 1, 1, 1};
    	int[] carLocat3 = {432, 96, 400, 1, 1, 1};
    	
    	int[] carSpeed1 = {45, 45 , 45 , 45 , 45 , 45, 45, 45};
    	int[] carSpeed2 = {75, 75, 75 , 75 , 75 , 75, 75, 75};
    	int[] carSpeed3 = {45, 45, 45, 45, 45, 45, 45, 45, 45 };
    	
    	int startCar = 0;
    	int carLoopCount = 0;
    	
    	String[] carType = {"sprites/car0.png", "sprites/car2.png", "sprites/car3.png", "sprites/policeCar.png"};
    	String[] carType2 = {"sprites/car0.5.png", "sprites/car2.5.png", "sprites/car3.5.png", "sprites/policeCarLeft.png"};
    	
    	    	
    	public int lives = 3;
    	int level = 1;
    	int pLevel = 1;
    	
        TileManager tileM = new TileManager(this);
        PlayerManager tileP = new PlayerManager(this);
        HeartManager tileH = new HeartManager(this);
        
		public boolean coinOn = true;
		public int coinCount;
        
        private static Image startScreen;       // image displayed while welcome screen
        private static Image instructions; 
        private static Image death;
        private static Image win;       
        
        
        private static int gameStage = 0;                   // stages of game
        private static final int STARTSCREEN = 0;
        private static final int INSTRUCTIONS = 1;
        private static final int WIN = 2;
        private static final int DEATH = 3;
        
        
    	/*
    	 * Construct our game and set it running.
    	 */
    	public Game() {
    		// create a frame to contain game
    		JFrame container = new JFrame("Robber");
    
    		// get hold the content of the frame
    		JPanel panel = (JPanel) container.getContentPane();
    
    		// set up the resolution of the game
    		panel.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
    		panel.setLayout(null);
    
    		// set up canvas size (this) and add to frame
    		setBounds(0,0,1000,1000);
    		panel.add(this);
    
    		// Tell AWT not to bother repainting canvas since that will
            // be done using graphics acceleration
    		setIgnoreRepaint(true);
    
    		// make the window visible
    		container.pack();
    		container.setResizable(false);
    		container.setVisible(true);
    
    
            // if user closes window, shutdown game and jre
    		container.addWindowListener(new WindowAdapter() {
    			public void windowClosing(WindowEvent e) {
    				System.exit(0);
    			} // windowClosing
    		});
    
    		// add key listener to this canvas
    		addKeyListener(new KeyInputHandler());
    
    		// request focus so key events are handled by this canvas
    		requestFocus();

    		// create buffer strategy to take advantage of accelerated graphics
    		createBufferStrategy(2);
    		strategy = getBufferStrategy();
    
    		// initialize entities
    		initEntities();
    		
    		//firstCarSpawn();
    		
    		endTime = timeStart + 30000;
    		
    		 
    		// start the game
    		gameLoop();
    		
        } // constructor
    
    
        /* initEntities
         * input: none
         * output: none
         * purpose: Initialise the starting state of the ship and alien entities.
         *          Each entity will be added to the array of entities in the game.
    	 */
    	
    	
    	private void initEntities() {
    		
    		tileP.getCollisionMap(tileM.mapTileNum);
    		
              // create the ship and put in center of screen
    		ship = new ShipEntity(this, "tiles/HitBox.png", tileP.spawnX * 48, tileP.spawnY * 48);
    		
    		if (level == 2) {
    			key = new KeyEntity (this, "sprites/key.png", 240, 430);
    			entities.add(key);

    		} else if (level == 3) {
    			key = new KeyEntity (this, "sprites/key.png",  655, 50);
    			entities.add(key);

    		}
            entities.add(ship);
            
        


 //----------------------------------------------------------------------------------------
            
            //Coins
            if(level == 4) {
                
     			// create a block of coins
                 for (int row = 0; row < 9; row++) {
                   for (int col = 0; col < 6; col++) {
                      coin = new CoinEntity(this, "sprites/coin.png", 
                         268 + (col * 40),
                         144 + (row * 30));
                     entities.add(coin);
                     coinCount++;
                   } // for
                 } // outer for
             
             }
            
 //----------------------------------------------------------------------------------------
            
            if (level == 2) {
            	Entity walkingPolice = new WalkingPoliceEntity(this, "sprites/walkingPoliceR.png", 300, 288, 48, 672, 70);             
                entities.add(walkingPolice);
                
                Entity walkingPolice2 = new WalkingPoliceEntity(this, "sprites/walkingPoliceR.png", 336, 432, 336, 672, 70);             
                entities.add(walkingPolice2);
                
                Entity Flash = new FlashPolice(this, "sprites/FlashD.png", 144, 288);
                entities.add(Flash);
                
                Flash = new FlashPolice(this, "sprites/FlashL.png", 48, 432);
                entities.add(Flash);
                
                Flash = new FlashPolice(this, "sprites/FlashD.png", 576, 96);
                entities.add(Flash);
                
                walkingPolice = new WalkingPoliceEntity(this, "sprites/flashlightPoliceD.png", 144, 240, 0, 1000, 0);             
                entities.add(walkingPolice);
                
                walkingPolice = new WalkingPoliceEntity(this, "sprites/flashlightPoliceL.png", 144, 432, 0, 1000, 0);             
                entities.add(walkingPolice);
                
                walkingPolice = new WalkingPoliceEntity(this, "sprites/flashlightPoliceD.png", 576, 48, 0, 1000, 0);             
                entities.add(walkingPolice);
            }
            
            else if (level == 3) {
            	Entity walkingPolice = new WalkingPoliceEntity(this, "sprites/walkingPoliceR.png", 672, 480, 48, 672, 70);             
                entities.add(walkingPolice);
                
                walkingPolice = new WalkingPoliceEntity(this, "sprites/walkingPoliceL.png", 49, 432, 48, 672, 70);             
                entities.add(walkingPolice);
                
                Entity Laser = new LaserEntity(this, "sprites/laser.png", 48, 288);
                entities.add(Laser);
                
            	Entity Flash = new FlashPolice(this, "sprites/FlashU.png", 144, 48);
                entities.add(Flash);
                
                Flash = new FlashPolice(this, "sprites/FlashD.png", 384, 96);
                entities.add(Flash);
                
                Flash = new FlashPolice(this, "sprites/FlashU.png", 528, 48);
                entities.add(Flash);
                
                walkingPolice = new WalkingPoliceEntity(this, "sprites/flashlightPoliceU.png", 144, 144, 0, 1000, 0);             
                entities.add(walkingPolice);
                
                walkingPolice = new WalkingPoliceEntity(this, "sprites/flashlightPoliceD.png", 384, 48, 0, 1000, 0);             
                entities.add(walkingPolice);
                
                walkingPolice = new WalkingPoliceEntity(this, "sprites/flashlightPoliceU.png", 528, 144, 0, 1000, 0);             
                entities.add(walkingPolice);
            }
            
            else if (level >= 5) {
                
                Entity police = new PoliceEntity(this, "sprites/shootingPoliceD.png",144 , 0, 1);             
                entities.add(police);
                
                police = new PoliceEntity(this, "sprites/shootingPoliceD.png",480 , 0, 1);             
                entities.add(police);
                
            }
            else if (level > 5){
            	Entity police = new PoliceEntity(this, "sprites/shootingPoliceD.png",144 , 0, 1);             
                entities.add(police);
                
                police = new PoliceEntity(this, "sprites/shootingPoliceD.png",480 , 0, 1);             
                entities.add(police);
            }
            
            
            
    	} // initEntities

        /* Notification from a game entity that the logic of the game
         * should be run at the next opportunity 
         */
         public void updateLogic() {
           logicRequiredThisLoop = true;
         } // updateLogic
         

         /* Remove an entity from the game.  It will no longer be
          * moved or drawn.
          */
         public void removeEntity(Entity entity) {
           removeEntities.add(entity);
         } // removeEntity

         /* Notification that the player has died.
          */
         public void notifyDeath() {
        	 gameStage = DEATH;
             message = " HAHAHAHAHHAHA You DEAD!  Try again?";
             tileP.canMove = false;
             waitingForKeyPress = true;
             train = true;
          
         } // notifyDeath


         /* Notification that the play has killed all aliens
          */
         public void notifyWin(){
           gameStage = WIN;
           message = "You win! Press key to Play again! Press ESP to exit";
           waitingForKeyPress = true;
           startGame();
         } // notifyWin
         
         public void notifyLoseLife	() {
        	
             tileP.canMove = false;
             
             tileP.getCollisionMap(tileM.mapTileNum);
			 tileP.loadMap();
			 
			 //-------------------------------------------------------------------------------------------
			 	 for (int i = 1; i < entities.size(); i++) {
				 
			 		 Entity entity = (Entity) entities.get(i);
				 
			 		 if (entity instanceof ShotEntity) {
			 			 removeEntity(entity);
			 		 } // if 
			 	 }
				 
			 tileP.respawn();
			 
			if (lives == 2) {
				tileH.mapTileNum[2][0] = 0;
			} // if
				
			if (lives == 1) {
				tileH.mapTileNum[1][0] = 0;
			} // if

			if (level == 3 || level == 4) {
				keyOn = false;
	    		entities.add(key);

	    	} 
         } // notifyLoseLife
         
         public void losingLife() {
        	 lives -= 1;
        	 notifyLoseLife();
        	 
             if (lives <= 0) {
            	 tileH.mapTileNum[0][0] = 0;
            	 notifyDeath();
            	 level = 1;
            	 pLevel = 1;
             	 levelUp(0);
             } // if
         } // losingLife
         
        /* Notification than an alien has been killed
         */
         public void notifyAlienKilled() {
           alienCount--;
           
           if (alienCount == 0) {
             notifyWin();
           } // if
           
           /*
           // speed up existing aliens
           for (int i=0; i < entities.size(); i++) {
             Entity entity = (Entity) entities.get(i);
             if (entity instanceof AlienEntity) {
               // speed up by 4%
               entity.setHorizontalMovement(entity.getHorizontalMovement() * 1.04);
             } // if
           } // for*/ 
         }  // notifyAlienKilled

        /* Attempt to fire.*/
        public void tryToFire() {
          // check that we've waited long enough to fire
          if ((System.currentTimeMillis() - lastFire) < firingInterval){
            return;
          } // if

          // otherwise add a shot
          lastFire = System.currentTimeMillis();
          ShotEntity shot = new ShotEntity(this, "sprites/bullet.png", 
                            ship.getX() + 10, ship.getY() - 30, -300, true);
          entities.add(shot);
        } // tryToFire
        
        
        public void placeCars(String carType, int carStartLoc, int carLocation, int direction, int carSpeed) {
        	
        	//Entity car = new AlienEntity(this, carType[carNum],(-48), carLocat1[level - 1], 1, carSpeed1[level -1]);
        	Entity car = new AlienEntity(this, carType, carStartLoc, carLocation, direction, carSpeed);
            entities.add(car);
        	
        	
        }
        
        public void firstCarSpawning(int numofTwoLoops, int numOfOneLoop){
        	if(levelStarted && carLoopCount < numofTwoLoops) {
        		if(carLoopCount < numOfOneLoop) {
        			startCar = 300;
        		}
    			
        		//assign the last time as infinity
    			lastCar1 = 999999;
    			lastCar2 = 999999;
    			lastCar3 = 999999;
    			lastCar4 = 999999;
    			lastCar5 = 999999;
    			lastCar6 = 999999;
    			lastCar7 = 999999;
    			
    			
    			if (carLoopCount >= numofTwoLoops) {
    				//levelStarted = false;
    				startCar = 0;
    			}
    		}
        	
        } // firstCarSpawning
        
        public void spawnCars(int level){
        	
        	if(level == 1) {
        		firstCarSpawning(6,3);
        		
	          	if(System.currentTimeMillis() - lastCar1 > (4000 + (Math.random() * 3000))){
	          		
	          	
	          		lastCar1 = System.currentTimeMillis();
	          		
	          		carNum = (int) (Math.random() * 3);
	          		
	          	 	placeCars(carType[carNum], -96 + startCar, 288, 1, 50);
	          	 	
	          	 	carLoopCount++;
	                
	            }// if
	          	 
	          	 if(System.currentTimeMillis() - lastCar2 > (5000 + (Math.random() * 3000))){
	          		 lastCar2 = System.currentTimeMillis();
	          		 carNum = (int) (Math.random() * 3);
	          		 placeCars(carType2[carNum], 768 - startCar, 240, -1, 75);
	          		 carLoopCount++; 
	          	}
	          	 
	          	 if(System.currentTimeMillis() - lastCar3 > (800 + (Math.random() * 1000))){
	          		 lastCar3 = System.currentTimeMillis();
	          		 carNum = (int) (Math.random() * 3);
	          		 placeCars(carType2[carNum], 768 - startCar, 432, -1, 200);
	          		 carLoopCount++;
	          		 startCar = 0;
	          	}
	          	
	          	
	          	
        	
        	}//if level 1
        	
        	else if(level == 2) {
        	
        		firstCarSpawning(12,6);
        			
	          	if(System.currentTimeMillis() - lastCar1 > (4000 + (Math.random() * 3000))){
	          		
	          		lastCar1 = System.currentTimeMillis();
	          		
	          		carNum = (int) (Math.random() * 3);
	          		
	          	 	placeCars(carType[carNum], -96 + startCar,  144, 1, carSpeed1[level -1]);
	          	 	carLoopCount++;
	                
	            }// if
	          	 
	          	if(System.currentTimeMillis() - lastCar2 > (5000 + (Math.random() * 3000))){
	          		  
	          		lastCar2 = System.currentTimeMillis();
	          		
	          		carNum = (int) (Math.random() * 3);
	          		placeCars(carType2[carNum], 768 - startCar, 96, -1, carSpeed2[level -1]);
	                
	          		carLoopCount++;
	          	}
	          	 
	          	if(System.currentTimeMillis() - lastCar3 > (4000 + (Math.random() * 3000))){
	          		
	          		 lastCar3 = System.currentTimeMillis();
	          		carNum = (int) (Math.random() * 3);
	          		placeCars(carType2[carNum], 768 - startCar, 192, -1, carSpeed3[level -1]);
	          		
	          		carLoopCount++;
	          	}
	          	
	          	if(System.currentTimeMillis() - lastCar4 > (6000 + (Math.random() * 3000))){
	          		
	          		lastCar4 = System.currentTimeMillis();
	          		carNum = (int) (Math.random() * 3);
	          		placeCars(carType[carNum], -96 + startCar, 288, 1, carSpeed3[level -1]);
	          		
	          		carLoopCount++;
	          	}
	          	
	          	if(System.currentTimeMillis() - lastCar5 > (5000 + (Math.random() * 3000))){
	          		
	          		 lastCar5 = System.currentTimeMillis();
	          		carNum = (int) (Math.random() * 3);
	          		placeCars(carType2[carNum], 768 - startCar, 336, -1, carSpeed3[level -1]);
	          		
	          		carLoopCount++;
	          	}
	          	
	          	if(System.currentTimeMillis() - lastCar6 > (4000 + (Math.random() * 3000))){
	          		
	          		lastCar6 = System.currentTimeMillis();
	          		carNum = (int) (Math.random() * 3);
	          		
	          		placeCars(carType[carNum], -96 + startCar, 384, 1, carSpeed3[level -1]);
	          		
	          		startCar = 0;
	          		carLoopCount++;
	          	}
        	
        	}//if level 2
        	
        	else if(level == 6) {
        		
        		firstCarSpawning(10,5);
        		
        		if(System.currentTimeMillis() - lastCar1 > (5000 + (Math.random() * 3000))){
	          		
	          		lastCar1 = System.currentTimeMillis();
	          		
	          		carNum = (int) (Math.random() * 4);
	          		
	          	 	//placeCars(carType[carNum], -48,  carLocat1[level -1], 1, carSpeed1[level -1]);
	          	 	placeCars(carType[carNum], -96 + startCar,  96, 1, carSpeed1[level -1]);
	          	 	carLoopCount++;
	                
	            }// if
	          	 
	          	if(System.currentTimeMillis() - lastCar2 > (4000 + (Math.random() * 3000))){
	          		  
	          		lastCar2 = System.currentTimeMillis();
	          		
	          		carNum = (int) (Math.random() * 4);
	          		placeCars(carType2[carNum], 768 - startCar, 240, -1, carSpeed2[level -1]);
	                
	          		carLoopCount++;
	          	}
	          	 
	          	if(System.currentTimeMillis() - lastCar3 > (6000 + (Math.random() * 3000))){
	          		
	          		 lastCar3 = System.currentTimeMillis();
	          		carNum = (int) (Math.random() * 4);
	          		placeCars(carType2[carNum], 768 - startCar, 288, -1, carSpeed3[level -1]);
	          		carLoopCount++;
	          	}
	          	
	          	if(System.currentTimeMillis() - lastCar4 > (4000 + (Math.random() * 3000))){
	          		
	          		lastCar4 = System.currentTimeMillis();
	          		carNum = (int) (Math.random() * 4);
	          		placeCars(carType[carNum], -96 + startCar, 336, 1, carSpeed3[level -1]);
	          		carLoopCount++;
	          	}
	          	
	          	if(System.currentTimeMillis() - lastCar5 > (5000 + (Math.random() * 3000))){
	          		
	          		lastCar5 = System.currentTimeMillis();
	          		carNum = (int) (Math.random() * 4);
	          		placeCars(carType2[carNum], 768 - startCar, 432, -1, carSpeed3[level -1]);
	          		startCar = 0;
	          		carLoopCount++;
	          	}
        		
        	}
        	
        	else if(level == 7) {
        		
        		firstCarSpawning(14,7);
        		
        		if(System.currentTimeMillis() - lastCar1 > (5000 + (Math.random() * 3000))){
	          		
	          		lastCar1 = System.currentTimeMillis();
	          		
	          		carNum = (int) (Math.random() * 4);
	          		
	          	 	//placeCars(carType[carNum], -48,  carLocat1[level -1], 1, carSpeed1[level -1]);
	          	 	placeCars(carType[carNum], -96 + startCar,  96, 1, carSpeed1[level -1]);
	                
	          	 	carLoopCount++;
	            }// if
	          	 
	          	if(System.currentTimeMillis() - lastCar2 > (5000 + (Math.random() * 3000))){
	          		  
	          		lastCar2 = System.currentTimeMillis();
	          		
	          		carNum = (int) (Math.random() * 4);
	          		placeCars(carType2[carNum], 768 - startCar, 144, -1, carSpeed2[level -1]);
	                
	          		carLoopCount++;
	          	}
	          	 
	          	if(System.currentTimeMillis() - lastCar3 > (6000 + (Math.random() * 3000))){
	          		
	          		 lastCar3 = System.currentTimeMillis();
	          		carNum = (int) (Math.random() * 4);
	          		placeCars(carType2[carNum], 768 - startCar, 192, -1, carSpeed3[level -1]);
	          		carLoopCount++;
	          	}
	          	
	          	if(System.currentTimeMillis() - lastCar4 > (6000 + (Math.random() * 3000))){
	          		
	          		lastCar4 = System.currentTimeMillis();
	          		carNum = (int) (Math.random() * 4);
	          		placeCars(carType[carNum], -96 + startCar, 240, 1, carSpeed3[level -1]);
	          		carLoopCount++;
	          	}
	          	
	          	if(System.currentTimeMillis() - lastCar5 > (5000 + (Math.random() * 3000))){
	          		
	          		 lastCar5 = System.currentTimeMillis();
	          		carNum = (int) (Math.random() * 4);
	          		placeCars(carType2[carNum], 768 - startCar, 288, -1, carSpeed3[level -1]);
	          		carLoopCount++;
	          	}
	          	
	          	if(System.currentTimeMillis() - lastCar6 > (4000 + (Math.random() * 3000))){
	          		
	          		lastCar6 = System.currentTimeMillis();
	          		carNum = (int) (Math.random() * 4);
	          		
	          		placeCars(carType[carNum], -96 + startCar, 336, 1, 65);
	          		carLoopCount++;
	          	}
	          	
	          	if(System.currentTimeMillis() - lastCar7 > (6000 + (Math.random() * 3000))){
	          		
	          		lastCar7 = System.currentTimeMillis();
	          		carNum = (int) (Math.random() * 4);
	          		
	          		placeCars(carType[carNum], -96 + startCar, 384, 1, 75);
	          		startCar = 0;
	          		carLoopCount++;
	          	}
        		
        		
        	}
        	
        	else if(level == 8) {
        		
        		firstCarSpawning(8,4);
        		
        		if(train){
	          		
	          		lastCar8 = System.currentTimeMillis();
	          
	          	 	placeCars("sprites/train.png", -96,  96, 1, 35);
	                train = false;
	                
	            }// if
        		
        		
        		if(System.currentTimeMillis() - lastCar8 > 2500){
	          		  
	          		lastCar8 = System.currentTimeMillis();
	          		
	          		carNum = (int) (Math.random() * 4);
	          		placeCars("sprites/trainCar.png", -96, 96, 1, 35);
	          		
	                
	          	}
        		
        		
	          	if(System.currentTimeMillis() - lastCar2 > (3000 + (Math.random() * 3000))){
	          		  
	          		lastCar2 = System.currentTimeMillis();
	          		
	          		carNum = (int) (Math.random() * 4);
	          		placeCars(carType2[carNum], 768 - startCar, 192, -1, 90);
	          		carLoopCount++;
	                
	          	}
	          	 
	          	if(System.currentTimeMillis() - lastCar3 > (2000 + (Math.random() * 3000))){
	          		
	          		 lastCar3 = System.currentTimeMillis();
	          		carNum = (int) (Math.random() * 4);
	          		placeCars(carType2[carNum], 768 - startCar, 240, -1, 75);
	          		carLoopCount++;
	          	}
	          	
	          	if(System.currentTimeMillis() - lastCar4 > (3000 + (Math.random() * 3000))){
	          		
	          		lastCar4 = System.currentTimeMillis();
	          		carNum = (int) (Math.random() * 4);
	          		placeCars(carType[carNum], -96 + startCar, 288, 1, 85);
	          		carLoopCount++;
	          	}
	          	
	          	if(System.currentTimeMillis() - lastCar5 > (2000 + (Math.random() * 3000))){
	          		
	          		lastCar5 = System.currentTimeMillis();
	          		carNum = (int) (Math.random() * 4);
	          		placeCars(carType2[carNum], 768 - startCar, 336, -1, 100);
	          		carLoopCount++;
	          		startCar = 0;
	          	}
        		
        	}
               
          } // spawnCars 
        
       
	/*
	 * gameLoop
         * input: none
         * output: none
         * purpose: Main game loop. Runs throughout game play.
         *          Responsible for the following activities:
	 *           - calculates speed of the game loop to update moves
	 *           - moves the game entities
	 *           - draws the screen contents (entities, text)
	 *           - updates game events
	 *           - checks input
	 */
	public void gameLoop() {
	
          long lastLoopTime = System.currentTimeMillis();
          // keep loop running until game ends
          
          while (gameRunning) {
        	  
            // calc. time since last update, will be used to calculate
            // entities movement
            long delta = System.currentTimeMillis() - lastLoopTime;
            lastLoopTime = System.currentTimeMillis();

            //time();
            
            // get graphics context for the accelerated surface and make it black
            Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
            g.setColor(Color.black);
            g.fillRect(0,0,1000,1000);

            
          //make the player respawn and be able to move again
            if (!waitingForKeyPress && !tileP.canMove) {
            	tileP.respawn();
            }
            
            // move each entity
            if (!waitingForKeyPress) {
              for (int i = 0; i < entities.size(); i++) {
                Entity entity = (Entity) entities.get(i);
                entity.move(delta);
              } // for
            } // if

            Graphics2D g2 = (Graphics2D)g; ///
            Graphics2D g3 = (Graphics2D)g; ///
            tileM.draw(g2);
            tileH.draw(g2);
            tileP.draw(g3);
            
            if(!waitingForKeyPress) {
            	spawnCars(level);
            }
            
            // draw all entities
            for (int i = 0; i < entities.size(); i++) {
               Entity entity = (Entity) entities.get(i);
               entity.draw(g);
               
            } // for

            // brute force collisions, compare every entity
            // against every other entity.  If any collisions
            // are detected notify both entities that it has
            // occurred
           for (int i = 0; i < entities.size(); i++) {
             for (int j = i + 1; j < entities.size(); j++) {
                Entity me = (Entity)entities.get(i);
                Entity him = (Entity)entities.get(j);

                if (me.collidesWith(him)) {
                  me.collidedWith(him);
                  him.collidedWith(me);
                  
                } // if
             } // inner for
           } // outer for
           
         //----------------------------------------------------------
           //flip lazer
       
	      if(System.currentTimeMillis() - lastLazer > 2000){
	        	   
	        	lastLazer = System.currentTimeMillis();
	        	for (int i = 1; i < entities.size(); i++) {
		             Entity entity = (Entity) entities.get(i);
		                
		             if (entity instanceof LaserEntity) {
		            	 entity.setY(entity.getY() * -1);
		             } // if 
	        	} // for   
	       } // if
	      
	      if(System.currentTimeMillis() - lastFlash > 900){
       	   
	    	  lastFlash = System.currentTimeMillis();
	        	for (int i = 1; i < entities.size(); i++) {
		             Entity entity = (Entity) entities.get(i);
		                
		             if (entity instanceof FlashPolice) {
		            	 entity.setY(entity.getY() * -1);
		            	 entity.setX(entity.getX() * -1);
		             } // if 
	        	} // for   
	       } // if

           
           if (keyOn) {
        	   removeEntity(key);
           }
           
           if(coinCount == 0) {
        	   coinOn = true;
           }
         
         
           // remove dead entities
           entities.removeAll(removeEntities);
           removeEntities.clear();

           // run logic if required
           if (logicRequiredThisLoop) {
             for (int i = 0; i < entities.size(); i++) {
               Entity entity = (Entity) entities.get(i);
               entity.doLogic();
             } // for
             logicRequiredThisLoop = false;
           } // if
           
           Toolkit tk = Toolkit.getDefaultToolkit();

           
           URL url = Game.class.getResource("stages/welcome_screen.png");
           startScreen = tk.getImage(url);
           
           url = Game.class.getResource("stages/instructions.png");
           instructions = tk.getImage(url);
           
           url = Game.class.getResource("stages/win.png");
           win = tk.getImage(url);
           
           url = Game.class.getResource("stages/death.png");
           death = tk.getImage(url);
           

           // if waiting for "any key press", draw message
           if (waitingForKeyPress) {
        	   
             g.setColor(Color.white);

             if (gameStage == STARTSCREEN) {
            	 
            	 g.drawImage(startScreen, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
                 g.drawString("Robber", (800 - g.getFontMetrics().stringWidth("Robber"))/2, 250);
                 g.drawString("Press any key", (800 - g.getFontMetrics().stringWidth("Press any key"))/2, 300);
             } else if (gameStage == INSTRUCTIONS) {
            	 
            	 g.drawImage(instructions, 40, 50, 700, 500, null);
                 
             } else if (gameStage == WIN) {
            	 
            	 g.drawImage(win, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
                 g.drawString(message, (800 - g.getFontMetrics().stringWidth(message))/2, 250);
               
             } else if (gameStage == DEATH) {
            	 
            	 g.drawImage(death, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
                 g.drawString(message, (800 - g.getFontMetrics().stringWidth(message))/2, 250);
               
             } // else if 
             
           }  // if

         /*  if (downPressed && waitingForKeyPress) {
        	   waitingForKeyPress = true;
        	   
           } else {
        	   g.dispose();
               strategy.show();
           }*/
           
            // clear graphics and flip buffer
            g.dispose();
            strategy.show();

            // ship should not move without user input
            ship.setHorizontalMovement(0);
            
         // respond to user moving ship
            if (!waitingForKeyPress) {
            	
            	if(System.currentTimeMillis() - lastMove >= 90){
            		
            		// ship should not move without user input
    	            ship.setHorizontalMovement(0);
    	            
    	            // respond to user moving ship
    	            if ((leftPressed) && (!rightPressed) && (!upPressed) && (!downPressed)) {
    	            	
    	            	lastMove = System.currentTimeMillis();
    	            	
    	            	//change current position
    	            	tileP.newPlayerX -= 1;
    	            	tileP.getTileImage("tiles/robberLeft.png");
    	            	changeLevel();
    	            	
    	            } else if ((rightPressed) && (!leftPressed) && (!upPressed) && (!downPressed)) {
    	            	
    	            	lastMove = System.currentTimeMillis();
    	            	
    	            	//change current position
    	            	tileP.newPlayerX += 1;
    	            	tileP.getTileImage("tiles/robberRight.png");
    	            	changeLevel();
    	            	
    	            } // else

    	            ship.setVerticalMovement(0);
    	            
    	           
    	            if ((upPressed) && (!downPressed) && (!leftPressed) && (!rightPressed)) {
    	            	
    	            	lastMove = System.currentTimeMillis();
    	            	
    	            	//change current position
    	                tileP.newPlayerY -= 1;
    	                tileP.getTileImage("tiles/robberUp.png");
    	                changeLevel();
    	                
    	              } else if ((downPressed) && (!upPressed) && (!leftPressed) && (!rightPressed)) {
    	            	 
    	            	  lastMove = System.currentTimeMillis();
    	            	  
    	            	  //change current position
    	            	  tileP.newPlayerY += 1;
    	            	  tileP.getTileImage("tiles/robberDown.png");
    	            	  changeLevel();
    	            	
    	              } // else
            	}
            	
	            
            }

            //---------------------------------------------------------------------------------
          //enemy fire
            if (!waitingForKeyPress) {
            	
            	if(System.currentTimeMillis() - lastFire > 1500){
            		
            		for (int i = 1; i < entities.size(); i++) {
            			Entity entity = (Entity) entities.get(i);
	             		  
	                	lastFire = System.currentTimeMillis();
	                	
	                	if (entity instanceof PoliceEntity && level >= 5) {
	                		ShotEntity shot = new ShotEntity(this, "sprites/bullet.png", 
		                			entity.getX() - 10, entity.getY() + 30, 300, false);
		                	entities.add(shot);
	                	}
		             } // if
	            } // for
	       }
            
           
            // pause
           try { Thread.sleep(16); } catch (Exception e) {}

          } // while

	} // gameLoop

	//-----------------------------------------------------------------------------------------
	public void changeLevel() {
		
		switch (level) {
			case 1:
				if (tileP.newPlayerY == -1 &&  tileP.newPlayerX >= 6 &&  tileP.newPlayerX <= 9) {
					pLevel++;
					levelUp(level);
	    			level ++;
				} // if
				return;
			case 2: 
				if (tileP.newPlayerY == -1 &&  tileP.newPlayerX >= 4 &&  tileP.newPlayerX <= 11) {
					pLevel++;
					levelUp(level);
	    			level ++;
				} // if
				return;
			case 3: 
				if (keyOn && tileP.newPlayerY == -1 &&  tileP.newPlayerX >= 3 &&  tileP.newPlayerX <= 4) {
					pLevel++;
					levelUp(level);
		    		level ++;
				} // if
				return;
			case 4: 
				if (keyOn && tileP.newPlayerY >= 9 &&  tileP.newPlayerY <= 10 &&  tileP.newPlayerX == 16) {
					pLevel++;
					levelUp(level);
	    			level ++;
				} // if
				return;
			case 5: 
				if (coinOn && tileP.newPlayerY == -1 &&  tileP.newPlayerX >= 7 &&  tileP.newPlayerX <= 8) {
					pLevel++;
					levelUp(level);
	    			level ++;
				} // if
				return;
			case 6: 
			case 7:
			case 8:
				if (tileP.newPlayerY == -1) {
					pLevel++;
					levelUp(level);
	    			level ++;
				} // if
			    return;
		} // switch 
	}
	
	public void levelUp (int level) {
		
		// load new map
    	if (level < 8) {
    		entities.clear();
    		tileM.loadMap(levels[level]);
			tileP.getCollisionMap(tileM.mapTileNum);
			tileP.loadMap();
		 	tileP.respawn();
		 	lives = 3;
		 	initEntities();
		 	levelStarted = true;
		 	carLoopCount = 0;
		 	keyOn = false;
		    coinOn = false;
		 	
    	} else {
    		
    		notifyWin();
    		
    	}
    	
    } // level up
	
        /* startGame
         * input: none
         * output: none
         * purpose: start a fresh game, clear old data
         */
         private void startGame() {
            // clear out any existing entities and initalize a new set
            entities.clear();
            
            initEntities();
            //firstCarSpawn();
            
            tileH.mapTileNum[0][0] = 0;
            level = 1;
            pLevel    = 1;
            tileP.respawn();
            tileP.canMove = false;
            tileP.getCollisionMap(tileM.mapTileNum);
			tileP.loadMap();
        	levelUp(0);
			keyOn = false;
            
            // blank out any keyboard settings that might exist
            leftPressed = false;
            rightPressed = false;
          //  firePressed = false;
            upPressed = false;
            downPressed = false;
            train = true;
         } // startGame


        /* inner class KeyInputHandler
         * handles keyboard input from the user
         */
         private class KeyInputHandler extends KeyAdapter {
             
             private int pressCount = 0;  // the number of key presses since
                                          // waiting for 'any' key press
             private int counter = 1;

            /* The following methods are required
             * for any class that extends the abstract
             * class KeyAdapter.  They handle keyPressed,
             * keyReleased and keyTyped events.
             */
	          public void keyPressed(KeyEvent e) {

	              // if waiting for keypress to start game, do nothing
	           /*   if (waitingForKeyPress) {
	                return;
	              } // if*/
	              
	              // respond to move left, right or fire
	              if (e.getKeyCode() == KeyEvent.VK_LEFT) {
	                leftPressed = true;
	              } // if
	
	              if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
	                rightPressed = true;
	              } // if
	
	           /*   if (e.getKeyCode() == KeyEvent.VK_SPACE) {
	                firePressed = true;
	              } // if*/
	              
	              if (e.getKeyCode() == KeyEvent.VK_UP) {
	                upPressed = true;
	              } // if
	              
	              if (e.getKeyCode() == KeyEvent.VK_DOWN) {
	                 downPressed = true;
	              } // if

	          } // keyPressed

	          public void keyReleased(KeyEvent e) {
	        	  
	              // if waiting for keypress to start game, do nothing
	           /*   if (waitingForKeyPress) {
	                return;
	              } // if*/
	              
	              // respond to move left, right or fire
	              if (e.getKeyCode() == KeyEvent.VK_LEFT) {
	                leftPressed = false;
	              } // if
	
	              if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
	                rightPressed = false;
	              } // if
	
	           /*   if (e.getKeyCode() == KeyEvent.VK_SPACE) {
	                firePressed = false;
	              } // if*/
	              
	              if (e.getKeyCode() == KeyEvent.VK_UP) {
	                upPressed = false;
	              } // if
	              
	              if (e.getKeyCode() == KeyEvent.VK_DOWN) {
	                downPressed = false;
	              } // if

	          } // keyReleased

	        public void keyTyped(KeyEvent e) {
	        	
               // if waiting for key press to start game
	           if (waitingForKeyPress) {
	        	   
	        	   System.out.println("counter: " + counter);
        		   System.out.println("pressCount: " + pressCount);
	        	   
	        	   if (pressCount == 0 && counter == 1) {
	        		   gameStage = INSTRUCTIONS;
	        		   
	        		  /* Toolkit tk = Toolkit.getDefaultToolkit();
	        		   URL url = Game.class.getResource("stages/instructions.png");
	        		   Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
	        		   
	        		   instructions = tk.getImage(url);
           
	        		   g.drawImage(instructions, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
	                   g.drawString("Instructions are here", (800 - g.getFontMetrics().stringWidth(
	                   "Instructions are here"))/2, 300);*/
	               //  */
	        		   
	        	   } 
	        	   if (pressCount == 1) {
	        		   waitingForKeyPress = false;
	        		   startGame();
	        		   pressCount = 1;
	        		   counter = 0;
                   } else {
                	 pressCount++;
                   } // else
	        	   
               } // if waitingForKeyPress

               // if escape is pressed, end game
               if (e.getKeyChar() == 27) {
                 System.exit(0);
               } // if escape pressed

	        } // keyTyped

         } // class KeyInputHandler

	/**
	 * Main Program
	 */
	public static void main(String [] args) {
        // instantiate this object
		new Game();
	} // main
} // Game