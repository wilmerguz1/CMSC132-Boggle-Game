
package imageblocks;

import java.awt.Color;

import utils.Picture;

public class ImageBlocks {
    static Color BLACK = new Color(0,0,0);
    static Color WHITE = new Color(255,255,255);                
    private int height;
    private int width;
    boolean [][] visited;
    Picture pic;
    
    public ImageBlocks(Picture pic) {
    	this.pic = pic;
    	this.height = pic.height();
    	this.width = pic.width();
    }
    
    private boolean isBlack(int x,int y){
        return pic.get(x,y).equals(BLACK);
    }
    
    private boolean isWhite(int x,int y){
        return pic.get(x,y).equals(WHITE);
    }
    
    /**
     * Count the number of image blocks in the given image
     * Counts the number of connected blocks in the binary digital image
     * @return number of black blocks
     */
    public int countConnectedBlocks() {
    	this.visited = new boolean[this.width][this.height];
    	int count = 0;
    	if (this.width == 0) {
    		return 0;
    	}
    	for (int row = 0; row < this.width; row++) {
    		for (int col = 0; col < this.height; col++) {
    			if (isBlack(row, col) && this.visited[row][col] == false) {
    				count++;
    				countConnectedBlocksHelper(row, col);
    			}
    		}
    	}
    	return count;
    }

    /*
     * Helps iterate through each block making sure adjacent pixels are black.
     */
    private void countConnectedBlocksHelper(int row, int col) {
    	//Check if within bounds.
    	if (row >= 0 && col >= 0 && row < this.width && col < this.height && 
    			isBlack(row, col) && this.visited[row][col] == false) {
    		this.visited[row][col] = true;
    		countConnectedBlocksHelper(row + 1, col + 1);
    		countConnectedBlocksHelper(row + 1, col);
    		countConnectedBlocksHelper(row + 1, col - 1);
    		countConnectedBlocksHelper(row, col + 1);
    		countConnectedBlocksHelper(row, col - 1);
    		countConnectedBlocksHelper(row - 1, col + 1);
    		countConnectedBlocksHelper(row - 1, col);
    		countConnectedBlocksHelper(row - 1, col - 1);
    	}
    }

    /**
     * Removes all neighboring black pixels of the provided pixel (x,y)
     * @param x
     * @param y
     * @return updated picture
     */
    public Picture delete(int x, int y) {
    	//If out of bounds or is white. 
    	if (x < 0 || y < 0 || x >= this.width || y >= this.height || 
    			isWhite(x, y)) {
    		return this.pic;
    	}
    	//Change black pixel to white pixel.
    	this.pic.set(x, y, WHITE);
    	delete(x + 1, y + 1);
    	delete(x + 1, y);
    	delete(x + 1, y - 1);
    	delete(x, y + 1);
    	delete(x, y - 1);
    	delete(x - 1, y + 1);
    	delete(x - 1, y);
    	delete(x - 1, y - 1);
    	return this.pic;
    }
   
    /**
     * Crops an image by setting all the pixels outside the provided
     * indices to the color white
     * delete everything not inside the bounds of the parameters (inclusive)
     * @param row_start
     * @param col_start
     * @param row_end
     * @param col_end
     * @return updated picture
     */
    public Picture crop(int x_start, int x_end, int y_start, int y_end) {
    	this.visited = new boolean[this.width][this.height];
    	if (this.width == 0) {
    		return null;
    	}
    	//Iterate through first part.
    	for(int row = 0; row < y_start; row++){
    		for(int col = 0; col < x_start; col++){
    			if(isBlack(row, col)){
    				cropHelper(row,col);
    			}
    		}
    	}
    	//Iterate through second part.
    	for(int row = y_end + 1; row < this.width; row++){
    		for(int col = x_end + 1; col < this.height; col++){
    			if(isBlack(row, col)){
    				cropHelper(row,col);
    			}
    		}
    	}
    	return this.pic;
	}
    
    //Turn each black pixel to a white pixel one at a time.
    private void cropHelper(int x, int y) {
    	if (x < 0 || y < 0 || x >= this.width || y >= this.height || 
    			isWhite(x, y)) {
			return;
		}
    	this.pic.set(x, y, WHITE);
    	cropHelper(x + 1, y + 1);
		cropHelper(x + 1, y);
		cropHelper(x + 1, y - 1);
		cropHelper(x, y + 1);
		cropHelper(x, y - 1);
		cropHelper(x - 1, y + 1);
		cropHelper(x - 1, y);
		cropHelper(x - 1, y - 1);
		return;
    }
    
    public static void main(String[] args) {
        
        String fileName = "images/14.png";
        Picture p = new Picture(fileName);
        ImageBlocks block = new ImageBlocks(p);
        try{
            int c1 = block.countConnectedBlocks();
            block.delete(4, 8);
            int c2 = block.countConnectedBlocks();
            p = block.crop(0, 90, 0, 90);
            int c3 = block.countConnectedBlocks();
            System.out.println("Number of connected blocks="+c1);
            System.out.println("Number of connected blocks after delete="+c2);
            System.out.println("Number of connected blocks after crop="+c3);
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }
}
