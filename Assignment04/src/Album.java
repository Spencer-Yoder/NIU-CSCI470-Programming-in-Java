import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

public class Album {
    private String albumName;
    private String artistName;
    private String genre;
    private String cover;   //String to the URL where the image is saved

    //Default Constructor that sets up the Album class and save the data
    Album(String name, String artist, String type, String pic){
        albumName = name;
        artistName = artist;
        genre = type;
        cover = pic;
    }

    //Function that get the Name as a String
    public String getName() {
        return albumName;
    }

    //Function that gets the Artist Name as a String
    public String getArtistName() {
        return artistName;
    }

    //Function that get the Genre and returns it as a String
    public String getGenre() {
        return genre;
    }

    //Function that get the image and returns a ImageIcon
    public ImageIcon getCover() {
        try {
            URL imageUrl = new URL(cover);  //Make a URL to the image
            InputStream in = imageUrl.openStream(); //Open the IO
            BufferedImage bImage = ImageIO.read(in);    //Read the Image
            in.close(); //Close the IO

            BufferedImage resizedImage = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);    //Make a new Buffer image from the image that was just downloaded
            Graphics2D g2 = resizedImage.createGraphics();  //resize it
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR); //Set Render info
            g2.drawImage(bImage, 0, 0, 50, 50, null);   //Draw the Image
            g2.dispose();

            ImageIcon img = new ImageIcon(resizedImage);    //Convert the Image to a ImageIcon

            return img; //Return the image
        }
        catch(IOException e) //Catch is there is an IO Problem
        {
            System.out.println(e);

        }
        catch(Exception e)  //Get all other errors
        {
            System.out.println("ERROR: " + e);
        }
        return null;
    }
}
