package models;

public class Notification {
    
    private String message;
    private String raisedBy;
    private String timestamp;
    private String receiver;
    
    public Notification(String message, String raisedBy, String timestamp, String receiver){
        this.message = message;
        this.raisedBy = raisedBy;
        this.timestamp = timestamp;
        this.receiver = receiver;
    }
    
    public String getMessage(){
        return message;
    }
    
    public void setMessage(String message){
        this.message = message;
    }
    
    public String getRaisedBy(){
        return raisedBy;
    }
    
    public void setRaisedBy(String raisedBy){
        this.raisedBy = raisedBy;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
    
    

    @Override
    public String toString() {
        return message + " (by " + raisedBy + ") - " + timestamp + receiver;
    }
    
    
}

