package at.digitrans.exceptions;

import javax.xml.ws.WebFault;

/**
 * Created by pl
 * Exception thrown by sample service
 */
@WebFault(name="ServiceFault", targetNamespace = "http://www.digitrans.at")
public class ServiceException extends Exception {


    public ServiceException(){
        super();
    }

    public ServiceException(String message) {
        super(message);
    }


}
