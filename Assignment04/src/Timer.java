public class Timer implements  Runnable{
    private XMLDownloadPanel delegate;  //reference to the GUI Panel

    //Default constructor that saves the reference to the XMLDownloadPanel
    public Timer(XMLDownloadPanel xmlDownloadPanel) {
        delegate = xmlDownloadPanel;
    }

    //Function that keeps track of the timer and add 1 to it
    //Arguments: None
    //Returned: None
    @Override
    public void run() {
        while (true)
        {
            delegate.setTime(); //update the time
            try {
                Thread.sleep(1000); //sleep 1 second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            catch (Exception e)
            {
                System.out.println(e);
            }
        }
    }
}
