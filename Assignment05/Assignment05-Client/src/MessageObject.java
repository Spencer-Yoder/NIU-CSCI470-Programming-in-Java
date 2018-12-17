import java.io.Serializable;

//Object that gets send between the client software and server
public class MessageObject implements Serializable {
    private String name;    //String for name
    private String ssn; //String for SSN
    private String address; //String for address
    private String zipCode; //String for zipCode

    private String type;    //String that defines what the action is
    private String status;  //String that save the return good or bad news

    //Default constructor for all elements
    public MessageObject(String name, String ssn, String address, String zipCode, String type) {
        this.name = name;
        this.ssn = ssn;
        this.address = address;
        this.zipCode = zipCode;
        this.type = type;
    }

    //default constructor for just the type of action
    public MessageObject(String type) {
        this.type = type;
    }

    //default constructor with action and ssn
    public MessageObject(String ssn, String type) {
        this.ssn = ssn;
        this.type = type;
    }


    //Function that gets the name
    public String getName() {
        return name;
    }

    //Function that gets the SSN
    public String getSsn() {
        return ssn;
    }

    //Function that get the address
    public String getAddress() {
        return address;
    }

    //Function that gets the zip Code
    public String getZipCode() {
        return zipCode;
    }

    //Function that gets the action type
    public String getType()
    {
        return type;
    }

    //Function that sets the return output status
    public void setStatus(String successful) {
        this.status = successful;
    }

    //Function that gets the return status
    public String getStatus() {
        return status;
    }
}
