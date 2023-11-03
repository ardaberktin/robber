import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

//import default.GameLoop;

public class PlayerManager {

	// GamePanel gp;
	Game gp;
	
	Tile[] tile;
	
	int mapTileNum[][];
	int collisionMap[][] = new int[16][12];
	int newPlayerX = 20;
	int newPlayerY = 20;
	int playerX;
	int playerY;
	int spawnX;
	int spawnY;
	int currentLevel;
	
	boolean canMove = true;
	
	public PlayerManager(Game gp){
		this.gp = gp;
		
		tile = new Tile[10];
		mapTileNum = new int[gp.maxScreenCol][gp.maxScreenRow];
		
		getTileImage("tiles/robberUp.png");
			loadMap();
		 
	} // constructor 
	
	public void getTileImage(String pic) {
		try {
			
			tile[0] = new Tile();
			tile[0].image = ImageIO.read(getClass().getResourceAsStream("tiles/blank.png"));
			
			tile[1] = new Tile();
			tile[1].image = ImageIO.read(getClass().getResourceAsStream(pic));
			
	
		}catch (IOException e){
			e.printStackTrace();
		} // catch 
	} // getTileImage
	
	public void getCollisionMap(int[][] newCollisionMap) {
		
		for(int i = 0; i < 16; i++){

			for(int k = 0; k < 12; k++){
				
				collisionMap[i][k] = newCollisionMap[i][k];
			}
		}
	}
	
	public void loadMap () {
		InputStream is;
		
		try {
			if (gp.pLevel == 4) {
				is = getClass().getResourceAsStream("maps/playerMap2.txt");
			}
			else if (gp.pLevel == 5) {
				is = getClass().getResourceAsStream("maps/playerMap3.txt");
			}
			else {
				is = getClass().getResourceAsStream("maps/playerMap.txt");
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			int col = 0;
			int row = 0;
			
			while(col < gp.maxScreenCol && row < gp.maxScreenRow) {
				String line = br.readLine();
				
				while (col < gp.maxScreenCol) {
					
					String numbers [] = line.split(" ");
					
					int num = Integer.parseInt(numbers[col]);
					mapTileNum[col][row] = num;
					if (num == 1) {
						newPlayerX = col;
						newPlayerY = row;
						playerX = col;
						playerY = row;
						spawnX = col;
						spawnY = row;
					}
					col ++;
					
				} // while
				
				// when it hits 16, reset col and increment row
				if (col == gp.maxScreenCol) {
					col = 0;
					row++;
				} // if
				
			} // while
			
			br.close();
			
		} catch (Exception e) {
			
		} // catch 
		
	} // loadMap
	
	public void respawn() {
		newPlayerX = spawnX;
		newPlayerY = spawnY;
		canMove = true;
		gp.ship.setX(newPlayerX * 48);
		gp.ship.setY(newPlayerY * 48);
	}
	
	public void draw (Graphics2D g3) {
		
		int col = 0;
		int row = 0;
		int x = 0;
		int y = 0;
		
		while (col < gp.maxScreenCol && row < gp.maxScreenRow) {
			
			//move player
			//check if the player is trying to move
			if (newPlayerX != playerX || newPlayerY != playerY ) {
				
				//check if the player is trying to move out of bounds
				if(newPlayerY <= 11 && newPlayerY >= 0 && newPlayerX <= 15 && newPlayerX >= 0 && canMove) {
					
					//check if the new position is a wall
					if(collisionMap[newPlayerX][newPlayerY] == 2 || collisionMap[newPlayerX][newPlayerY] == 6 || collisionMap[newPlayerX][newPlayerY] == 11) {
						
						//stop player from moving 
						newPlayerX = playerX;
						newPlayerY = playerY; 
					} else {
						
						//move player to new location
						mapTileNum[playerX][playerY] = 0;
						mapTileNum[newPlayerX][newPlayerY] = 1;
						
						//set old position to new position
						playerX = newPlayerX;
						playerY = newPlayerY;
						
						//set hitbox to player position
						gp.ship.setX(newPlayerX * 48);
						gp.ship.setY(newPlayerY * 48);
						
						if(collisionMap[playerX][playerY] == 1) { 
							gp.losingLife();
						}
					}//inner if else
					
				} else {
					//stop player from moving 
					newPlayerX = playerX;
					newPlayerY = playerY;
				}//middle if else
				
			} else {
				//stop player from moving 
				newPlayerX = playerX;
				newPlayerY = playerY;
			}//outer if else
			
			
			int titleNum = mapTileNum[col][row];
			
			g3.drawImage(tile[titleNum].image, x, y, gp.tileSize, gp.tileSize, null);
			
			col ++;
			x += gp.tileSize;
			
			if(col == gp.maxScreenCol) {
				col = 0;
				x = 0;
				row ++;
				y += gp.tileSize;
			} // if
			
		} // while

	   
	} // draw
	
	
} // TileManager