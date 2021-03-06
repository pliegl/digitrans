package at.digitrans.webservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.Endpoint;

import at.digitrans.exceptions.ServiceException;


/**
 * Sample Web Service
 *
 * @author pl
 */
@WebService(name = "ArithmeticService")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
public class ArithmeticService {


  /**
   * Define a logger
   */
  private static final Logger LOG = LoggerFactory.getLogger(ArithmeticService.class);

  static {
    System.setProperty("com.sun.xml.ws.fault.SOAPFaultBuilder.disableCaptureStackTrace", "false");
  }

  public enum Gender {MALE, FEMALE};

  /**
   * Simple function for adding two parameters
   */
  @WebMethod(operationName = "addFunction")
  @WebResult(name = "sum")
  public BigDecimal add(@WebParam(name = "Addend_A") BigDecimal addend_a,
                        @WebParam(name = "Addend_B") BigDecimal addend_b) throws ServiceException {

    LOG.debug("Received add request with addend {} and addend {}",
              new Object[]{addend_a, addend_b});

    if (addend_a == null || addend_b == null) {
      LOG.debug("Received invalid request for add function");
      throw (new ServiceException("Parameter invalid"));
    }

    return addend_a.add(addend_b);
  }

  /**
   * Simple function for subtracting two values
   */
  @WebMethod(operationName = "subtractFunction")
  @WebResult(name = "difference")
  public BigDecimal subtract(@WebParam(name = "Minuend") BigDecimal minuend,
                             @WebParam(name = "Subtrahend") BigDecimal subtrahend)
      throws ServiceException {

    LOG.debug("Received subtract request with minuend {} and subtrahend {}",
              new Object[]{minuend, subtrahend});

    if (minuend == null || subtrahend == null) {
      LOG.debug("Received invalid request for subtract function");
      throw (new ServiceException("Parameter invalid"));
    }

    return minuend.subtract(subtrahend);
  }


  /**
   * Determines if value is even/odd and positive/negative
   */
  @WebMethod(operationName = "whoAmI")
  @WebResult(name = "evaluationResult")
  public String whoAmI(@WebParam(name = "inputInteger") BigDecimal input) throws ServiceException {
    StringBuffer sb = new StringBuffer();

    if (input == null) {
      LOG.debug("Received invalid request for evaluation function");
      throw (new ServiceException("Parameter invalid"));
    }

    LOG.debug("Received whoAmI request with parameter {}", input);

    sb.append("The passed value ").append(input.toPlainString()).append(" is ");

    //Determine if even or odd
    if (isEven(input)) {
      sb.append("even");
    } else {
      sb.append("odd");
    }

    sb.append(" and ");
    //Determine if positive or negative
    if (isNegative(input)) {
      sb.append("negative");
    } else {
      sb.append("positive");
    }

    return sb.toString();
  }


  /**
   * Returns the system the app is running on
   * @return
   * @throws ServiceException
   */
  @WebMethod(operationName = "whoAreYou")
  @WebResult(name = "nameOfApplication")
  public String whoAreYou() throws ServiceException {
    StringBuilder sb = new StringBuilder();
    sb.append("I am ").append(System.getProperty("sun.java.command")).append(" running on ")
        .append(System.getProperty("os.name")).append(" version ")
        .append(System.getProperty("os.version"));
    LOG.debug("Telling the client who I am. {}", sb.toString());
    return sb.toString();
  }


  /**
   * Adds a "Dipl.-Ing." to the name
   * @param gender
   * @param lastName
   * @return
   * @throws ServiceException
   */
  @WebMethod(operationName = "promoteMeToMaster")
  @WebResult(name = "masterizedName")
  public String promoteMeToMaster(@WebParam(name="gender") Gender gender, @WebParam(name = "lastName") String lastName) throws ServiceException {

    StringBuilder sb = new StringBuilder();
    if (Gender.MALE.equals(gender)) {
      sb.append("Herr ");
    }
    else {
      sb.append("Frau ");
    }
    sb.append("Dipl.-Ing. ");
    sb.append(lastName);

    LOG.debug("Telling the client the masterized string: {}", sb.toString());

    return sb.toString();


  }


  /**
   * Determine if negative or positive
   */
  private boolean isNegative(BigDecimal d) {
    if (d.intValue() < 0) {
      return true;
    }

    return false;
  }


  /**
   * Determine if even or odd
   */
  private boolean isEven(BigDecimal d) {

    if (d.intValue() % 2 == 0) {
      return true;
    }

    return false;
  }


  /**
   * Invoker
   */
  public static void main(String[] args) {
    LOG.debug("Starting Arithmetic Web Service.");
    Endpoint endpoint = Endpoint.publish(
        "http://0.0.0.0:8080/arithmeticservice",
        new ArithmeticService());
    LOG.debug("Access service under http://0.0.0.0:8080/arithmeticservice?wsdl");
  }

}
