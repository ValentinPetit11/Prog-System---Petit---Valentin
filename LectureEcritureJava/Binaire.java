import java.io.FileWriter;
import java.io.IOException;

public class Binaire {
    
	
	public static void main(String[] args) {
        
		Image img = Image.read_bin("input.ppm");  
		img.write_bin("copy.ppm"); 
    }
}