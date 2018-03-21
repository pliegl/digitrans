package at.digitrans.controller;


import org.apache.logging.log4j.util.Strings;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.web.util.UriComponentsBuilder;


import javax.annotation.PostConstruct;

import at.digitrans.entities.Order;
import at.digitrans.entities.OrderItem;
import at.digitrans.exception.OperationNotSupportedException;
import at.digitrans.exception.InvalidContentException;

@RestController
@RequestMapping("/order-service")
public class OrderController {

  //Dummy data store
  private HashMap<String, Order> orderItems = new HashMap<>();

  private final Logger LOG = LoggerFactory.getLogger(this.getClass());


  /**
   * Get a single order
   */
  @GetMapping("/orders/{uuid}")
  public Order getOrder(@PathVariable String uuid) {

    LOG.info("Received GET request on /orders/{}", uuid);

    if (!orderItems.containsKey(uuid)) {
      throw new InvalidContentException("Unable to find order with uuid " + uuid);
    }

    LOG.info("Returning order nr. {}", uuid);
    return orderItems.get(uuid);
  }


  /**
   * Get all orders
   */
  @GetMapping("/orders")
  public Collection<Order> getOrders() {
    LOG.info("Received GET request on /orders");
    LOG.info("Returning all orders.");
    return orderItems.values();
  }


  /**
   * Post (create) a single order on a specific URI
   */
  @PostMapping("/orders/{uuid}")
  public ResponseEntity storeOrderOnExisting(@RequestBody Order order, @PathVariable String uuid,
                                             UriComponentsBuilder ucBuilder) {

    LOG.info("Received POST request on /orders/{uuid}");

    if (Strings.isEmpty(order.getUuid())) {
      throw new InvalidContentException("Unable to process an order without a uuid");
    }

    if (!order.getUuid().equals(uuid)) {
      throw new InvalidContentException(
          "Unable to perform POST operation. UUID of resource and submitted resource UUID do not match.");
    }

    //Does the order exist already?
    if (orderItems.containsKey(order.getUuid())) {
      throw new InvalidContentException(
          "Unable to store the order as a new order, as order with uuid " + order.getUuid()
          + " already exists");
    }

    //Store it
    orderItems.put(order.getUuid(), order);

    LOG.info("Successfully stored order with uuid {}", order.getUuid());

    //Determine the location of the newly created document
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setLocation(
        ucBuilder.path("/order-service/orders/{id}").buildAndExpand(order.getUuid()).toUri());
    return new ResponseEntity(httpHeaders, HttpStatus.CREATED);

  }


  /**
   * Post (create) orders on the orders collection
   */
  @PostMapping("/orders")
  public void storeOrder(@RequestBody List<Order> orders, UriComponentsBuilder ucBuilder) {

    LOG.info("Received POST request on /orders");

    orders.stream().forEach((o) -> {
                              if (Strings.isEmpty(o.getUuid())) {
                                throw new InvalidContentException("Unable to process an order without a uuid");
                              }

                              if (orderItems.containsKey(o.getUuid())) {
                                throw new InvalidContentException(
                                    "Unable to store the orders as, as order with uuid " + o.getUuid() + " already exists");
                              }
                            }
    );

    //Store it
    for (Order o : orders) {
      orderItems.put(o.getUuid(), o);
    }

    LOG.info("Successfully stored {} orders", orders.size());

  }


  /**
   * Put (update) a single order on an existing
   */
  @PutMapping("/orders/{uuid}")
  public void putOrderOnExisting(@RequestBody Order order, @PathVariable String uuid) {

    LOG.info("Received PUT request on /orders/" + uuid);

    if (Strings.isEmpty(order.getUuid())) {
      throw new InvalidContentException("Unable to process an order without a uuid");
    }

    if (!order.getUuid().equals(uuid)) {
      throw new InvalidContentException(
          "Unable to perform PUT operation. UUID of resource and submitted resource UUID do not match.");
    }

    //Put the order - the HashMap will take care of the create or update logic
    orderItems.put(order.getUuid(), order);

    LOG.info("Successfully updated order with uuid " + uuid);

  }


  /**
   * Put (update) orders
   */
  @PutMapping("/orders")
  public void putOrder(@RequestBody List<Order> orders) {

    LOG.info("Received PUT request on /orders");

    orders.stream().forEach((o) -> {
                              if (Strings.isEmpty(o.getUuid())) {
                                throw new InvalidContentException("Unable to process an order without a uuid");
                              }
                            }
    );

    for (Order o : orders) {
      //Put the order - the HashMap will take care of the create or update logic
      orderItems.put(o.getUuid(), o);
    }

    LOG.info("Successfully updated/created {} orders", orders.size());

  }


  /**
   * Delete an existing order
   */
  @DeleteMapping("/orders/{uuid}")
  public ResponseEntity deleteOrder(@PathVariable String uuid) {

    LOG.info("Received DELETE request on /orders/" + uuid);

    //Does the order exist already?
    if (!orderItems.containsKey(uuid)) {
      throw new InvalidContentException(
          "Unable to delete order " + uuid + " as it does not exist.");
    }

    orderItems.remove(uuid);
    LOG.info("Deleted order with uuid {}", uuid);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);

  }


  /**
   * Delete an existing order
   */
  @DeleteMapping("/orders")
  public ResponseEntity deleteOrders(@RequestBody List<Order> orders) {

    LOG.info("Received DELETE request on /orders");

    //Does the order exist?

    orders.stream().forEach((o) -> {
                              if (!orderItems.containsKey(o.getUuid())) {
                                throw new InvalidContentException(
                                    "Unable to delete order " + o.getUuid() + " as it does not exist.");
                              }
                            }
    );

    //Delete the orders
    for (Order o : orders) {
      orderItems.remove(o.getUuid());
    }

    LOG.info("Deleted {} orders", orders.size());

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);

  }


  @PostConstruct
  private void init() {

    List<OrderItem> orderItemList = new ArrayList<>();
    orderItemList.add(
        OrderItem.builder().id(1).description("Äpfel Golden Delicious").gtin("9834847348472")
            .price(new BigDecimal("12")).quantity(new BigDecimal("5")).build());
    orderItemList.add(OrderItem.builder().id(2).description("Birne groß").gtin("9845212365475")
                          .price(new BigDecimal("54")).quantity(new BigDecimal("3")).build());
    orderItemList.add(
        OrderItem.builder().id(3).description("Weintrauben rot").gtin("94213545874521")
            .price(new BigDecimal("23")).quantity(new BigDecimal("2")).build());
    Order order1 = Order.builder().uuid("o1").createdAt(
        new Date()).orderItemList(orderItemList).orderedBy("Max Mustermann").build();
    orderItems.put(order1.getUuid(), order1);

    List<OrderItem> orderItemList2 = new ArrayList<>();
    orderItemList2.add(OrderItem.builder().id(1).description("Buntstifte").gtin("9854521254521")
                           .price(new BigDecimal("23")).quantity(new BigDecimal("6")).build());
    orderItemList2.add(OrderItem.builder().id(2).description("Bleistife").gtin("9536541875421")
                           .price(new BigDecimal("66")).quantity(new BigDecimal("3.87")).build());
    orderItemList2.add(OrderItem.builder().id(3).description("Kugelschreiber").gtin("9565789542102")
                           .price(new BigDecimal("4")).quantity(new BigDecimal("2.98")).build());
    Order
        order2 =
        Order.builder().uuid("o2").createdAt(new Date()).orderItemList(orderItemList2)
            .orderedBy("Georg Gruber").build();
    orderItems.put(order2.getUuid(), order2);


  }


}
