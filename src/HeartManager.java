import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

//import default.GameLoop;

public class HeartManager {

	// GamePanel gp;
	Game gp;
	
	Tile[] tile;
	
	int mapTileNum[][];
	
	public HeartManager(Game gp){
		this.gp = gp;
		
		tile = new Tile[10];
		mapTileNum = new int[gp.maxScreenCol][gp.maxScreenRow];
		
		getTileImage();
		loadMap("maps/heartMap.txt");
		 
	} // constructor 
	
	public void getTileImage() {
		try {
			tile[0] = new Tile();
			tile[0].image = ImageIO.read(getClass().getResourceAsStream("tiles/blank.png"));
			
			tile[1] = new Tile();
			tile[1].image = ImageIO.read(getClass().getResourceAsStream("sprites/heart.png"));
			
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
			
			if (gp.level < 7 && gp.lives == 3) {
				
				mapTileNum[2][0] = 1;
				mapTileNum[1][0] = 1;
				mapTileNum[0][0] = 1;
			}
						
			if(col == gp.maxScreenCol) {
				col = 0;
				x = 0;
				row ++;
				y += gp.tileSize;
			} // if
			
		} // while
		
		
	} // draw
	
	
} // TileManager