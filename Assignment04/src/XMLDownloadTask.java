import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class XMLDownloadTask extends SwingWorker<List<Integer>, Integer> {

    private XMLDownloadPanel delegate;  //Reference to the colling object
    private String URLAddress;  //String for the URL to the RSS

    //Default constructor for the XMLDownLoadTask class
    //Arguments: Reference to the calling class, and a string or the URL
    //Return: None
    XMLDownloadTask(XMLDownloadPanel xmlDownloadPanel, String address) {
        URLAddress = address;
        delegate = xmlDownloadPanel;
    }


    //Function that actually does the download in the background
    //**Most of the code was taken from the notes on XML Downloading
    //Arguments: None
    //Return: None
    @Override
    protected List<Integer> doInBackground() {
        Thread clockThread = new Thread(new Timer(delegate));
        clockThread.start();

        String xmlString;
        HttpURLConnection connection = null;

        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            // Create a URL object from a String that contains a valid URL
            URL url = new URL(URLAddress);
            // Open an HTTP connection for the URL
            connection = (HttpURLConnection) url.openConnection();
            // Set HTTP request method
            connection.setRequestMethod("GET" );
            // If the HTTP status code is 200, we have successfully connected
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Use a mutable StringBuilder to store the downloaded text
                StringBuilder xmlResponse = new StringBuilder();
                // Create a BufferedReader to read the lines of XML from the connection's input stream
                BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                // Read lines of XML and append them to the StringBuilder
                //object until the end of the stream is reached
                String strLine;
                while ((strLine = input.readLine()) != null) {
                    xmlResponse.append(strLine);
                }
                // Convert the StringBuilder object to a String
                xmlString = xmlResponse.toString();

                //Send it to the Album Handler class
                saxParser.parse(new InputSource(new ByteArrayInputStream(xmlString.getBytes("utf-8" ))), new AlbumHandler());

                // Close the input stream
                input.close();
            }
        }
        catch (ParserConfigurationException e) {
            System.out.println(e);
        }
        catch(SAXException e) {
            System.out.println(e);
        }
        catch (MalformedURLException e) { // Handle MalformedURLException
            System.out.println(e);
        }
        catch (IOException e) { // Handle IOException
            System.out.println(e);
        }
        catch(Exception e) {
            System.out.println("Error: " + e);
        }
        finally{
            // close connection
            if (connection != null) {
                connection.disconnect();
            }
        }
        clockThread.stop();
        return null;
    }

    //Inside class that extends DefaultHandler that does the Parsing or the XML Data
    //**A good section of this class was taken from the XML Parsing Notes
    public class AlbumHandler extends DefaultHandler {
        private boolean bName = false;  //Booleans for keep reading characters between tags
        private boolean bArtist = false;
        private boolean bCover = false;

        private String name;    //Strings to store the info in the XML
        private String artist;
        private String genre;
        private String cover;

        //Function that gets called when opening tags get called
        public void startElement(String uri, String localName,String qName, Attributes attributes) {
            if (qName.equalsIgnoreCase("im:name" )) {   //If the opening tag is name
                bName = true;   //set the bool to true to receive the next chars
                name = "";  //Clear the String
            }

            if (qName.equalsIgnoreCase("im:artist" )) {
                bArtist = true; //set the bool to true to receive the next chars
                artist = ""; //Clear the String
            }

            if (qName.equalsIgnoreCase("im:image" )) {
                bCover = true;//set the bool to true to receive the next chars
                cover = ""; //Clear the String
            }

            if (qName.equalsIgnoreCase("category" ))    //If it is category tag get the attribute from it
                genre = attributes.getValue("label" );

        }
        // This method may be called multiple times for a given element.
        public void characters(char ch[], int start, int length) {
            if (bName)
                name = name + new String(ch, start, length);    //add the char to the string and update it

            if (bArtist)
                artist = artist + new String(ch, start, length); //add the char to the string and update it

            if (bCover)
                cover = cover + new String(ch, start, length); //add the char to the string and update it
        }
        //Function that gets called when the end tag is received
        public void endElement(String uri, String localName, String qName) {
            if (qName.equalsIgnoreCase("im:name" ))
                bName = false;  //Set the current listener to false
            if (qName.equalsIgnoreCase("im:artist" ))
                bArtist = false;
            if (qName.equalsIgnoreCase("im:image" ))
                bCover = false;
            if (qName.equalsIgnoreCase("entry" )) {
                delegate.displayAlbum(new Album(name, artist, genre, cover));   //when the whole album is received, Save it and send it to the Table
            }
        }
    }
}
