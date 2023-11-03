import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

//import default.GameLoop;

public class TileManager {

	// GamePanel gp;
	Game gp;
	
	Tile[] tile;
	
	int mapTileNum[][];
	
	public TileManager(Game gp){
		this.gp = gp;
		
		tile = new Tile[20];
		mapTileNum = new int[gp.maxScreenCol][gp.maxScreenRow];
		
		getTileImage();
		loadMap("maps/map.txt");
		 
	} // constructor 
	
	public void getTileImage() {
		try {
			tile[0] = new Tile();
			tile[0].image = ImageIO.read(getClass().getResourceAsStream("tiles/grass.png"));
			
			tile[1] = new Tile();
			tile[1].image = ImageIO.read(getClass().getResourceAsStream("tiles/water.png"));
			
			tile[2] = new Tile();
			tile[2].image = ImageIO.read(getClass().getResourceAsStream("tiles/wall.png"));
			
			tile[3] = new Tile();
			tile[3].image = ImageIO.read(getClass().getResourceAsStream("tiles/road.png"));
			
			tile[4] = new Tile();
			tile[4].image = ImageIO.read(getClass().getResourceAsStream("tiles/bridgeCenter.png"));
			
			tile[5] = new Tile();
			tile[5].image = ImageIO.read(getClass().getResourceAsStream("tiles/ground.png"));
			
			tile[6] = new Tile();
			tile[6].image = ImageIO.read(getClass().getResourceAsStream("tiles/tree.png"));
			
			tile[7] = new Tile();
			tile[7].image = ImageIO.read(getClass().getResourceAsStream("tiles/sidewalk.png"));
			
			tile[8] = new Tile();
			tile[8].image = ImageIO.read(getClass().getResourceAsStream("tiles/bank.png"));
			
			tile[9] = new Tile();
			tile[9].image = ImageIO.read(getClass().getResourceAsStream("tiles/ground.png"));
			
			tile[10] = new Tile();
			tile[10].image = ImageIO.read(getClass().getResourceAsStream("tiles/door.png"));
			
			tile[11] = new Tile();
			tile[11].image = ImageIO.read(getClass().getResourceAsStream("tiles/laser.png"));
			
			tile[12] = new Tile();
			tile[12].image = ImageIO.read(getClass().getResourceAsStream("tiles/trainTrack.png"));
			
			tile[13] = new Tile();
			tile[13].image = ImageIO.read(getClass().getResourceAsStream("tiles/roadLineUp.png"));
			
			tile[14] = new Tile();
			tile[14].image = ImageIO.read(getClass().getResourceAsStream("tiles/roadLineDown.png"));
			
			tile[15] = new Tile();
			tile[15].image = ImageIO.read(getClass().getResourceAsStream("tiles/bridgeRight.png"));
			
			tile[16] = new Tile();
			tile[16].image = ImageIO.read(getClass().getResourceAsStream("tiles/bridgeLeft.png"));
			
			
		}catch (IOException e){
			e.printStackTrace();
		} // catch 
	} // getTileImage
	
public void loadMap (String fileName) {
		
        // declare variables
        String [] contents = null;
        int length = 0;
        String folderName = "";
        String resource = "";
     
        try {

           // input
           folderName = "/subFolder/"; // if the file is contained in the same folder as the .class file, make this equal to the empty string
           resource = fileName;

           // this is the path within the jar file
           InputStream input = TileManager.class.getResourceAsStream(folderName + resource);
           if (input == null) {
        	   
               // this is how we load file within editor (eg eclipse)
               input = TileManager.class.getClassLoader().getResourceAsStream(resource);
           } // if
           BufferedReader in = new BufferedReader(new InputStreamReader(input));   
          
           int col = 0;
		   int row = 0;
			
			while(col < gp.maxScreenCol && row < gp.maxScreenRow) {
				String line = in.readLine();
				
				while (col < gp.maxScreenCol) {
					
					String numbers [] = line.split(" ");
					
					int num = Integer.parseInt(numbers[col]);
					mapTileNum[col][row] = num;
					col ++;
					
				} // while
				
				// when it hits 16, reset col and increment row
				if (col == gp.maxScreenCol) {
					col = 0;
					row++;
				} // if
				
			} // while

           in.close();
       } catch (Exception e) {
           System.out.println("File Input Error");
       } // catch
		
		/*try {
			InputStream is = getClass().getResourceAsStream("maps/map.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			int col = 0;
			int row = 0;
			
			while(col < gp.maxScreenCol && row < gp.maxScreenRow) {
				String line = br.readLine();
				
				while (col < gp.maxScreenCol) {
					
					String numbers [] = line.split(" ");
					
					int num = Integer.parseInt(numbers[col]);
					mapTileNum[col][row] = num;
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
			
		} // catch */
		
	} // loadMap
	
	
	public void draw (Graphics2D g2) {
		
		int col = 0;
		int row = 0;
		int x = 0;
		int y = 0;
		
		while (col < gp.maxScreenCol && row < gp.maxScreenRow) {
			
			int titleNum = mapTileNum[col][row];
			
			g2.drawImage(tile[titleNum].image, x, y, gp.tileSize, gp.tileSize, null);
			col ++;
			x += gp.tileSize;
			
			if(col == gp.maxScreenCol) {
				col = 0;
				x = 0;
				row ++;
				y += gp.tileSize;
			} // if
			
		} // while
		
		/*g2.drawImage(tile[0].image, 0, 0, gp.tileSize, gp.tileSize, null);
		g2.drawImage(tile[1].image, 48, 0, gp.tileSize, gp.tileSize, null);
		g2.drawImage(tile[2].image, 96, 0, gp.tileSize, gp.tileSize, null);*/
				
	   
	} // draw
	
	
} // TileManager