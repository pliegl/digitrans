package at.digitrans.controller;


import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;



import javax.annotation.PostConstruct;

import at.digitrans.entities.Order;
import at.digitrans.entities.OrderItem;
import at.digitrans.exception.OrderNotFoundException;

@RestController
@RequestMapping("/order-service")
public class OrderController {

  //Dummy data store
  private HashMap<String, Order> orderItems = new HashMap<>();

  private final Logger LOG = LoggerFactory.getLogger(this.getClass());


  /**
   * Get a single order
   * @param uuid
   * @return
   */
  @GetMapping("/orders/{uuid}")
  public Order getOrder(@PathVariable String uuid) {

    if (!orderItems.containsKey(uuid)) {
      throw new OrderNotFoundException("Unable to find order with uuid " + uuid);
    }



    LOG.info("Returning order nr. {}", uuid);



    return orderItems.get(uuid);
  }


  /**
   * Get all orders
   * @return
   */
  @GetMapping("/orders")
  public Collection<Order> getOrders() {
    LOG.info("Returning all orders.");
    return orderItems.values();
  }


  @PostMapping("/orders")
  public void storeOrder(@RequestBody Order order) {

    //Assign a uuid
    order.setUuid(UUID.randomUUID().toString());
    orderItems.put(order.getUuid(), order);

  }






  @PostConstruct
  private void init() {

    List<OrderItem> orderItemList = new ArrayList<>();
    orderItemList.add(OrderItem.builder().id(1).description("Äpfel Golden Delicious").gtin("9834847348472").price(new BigDecimal("12")).quantity(new BigDecimal("5")).build());
    orderItemList.add(OrderItem.builder().id(2).description("Birne groß").gtin("9845212365475").price(new BigDecimal("54")).quantity(new BigDecimal("3")).build());
    orderItemList.add(OrderItem.builder().id(3).description("Weintrauben rot").gtin("94213545874521").price(new BigDecimal("23")).quantity(new BigDecimal("2")).build());
    Order order1 = Order.builder().uuid("o1").createdAt(
        new Date()).orderItemList(orderItemList).orderedBy("Max Mustermann").build();
    orderItems.put(order1.getUuid(), order1);

    List<OrderItem> orderItemList2 = new ArrayList<>();
    orderItemList2.add(OrderItem.builder().id(1).description("Buntstifte").gtin("9854521254521").price(new BigDecimal("23")).quantity(new BigDecimal("6")).build());
    orderItemList2.add(OrderItem.builder().id(2).description("Bleistife").gtin("9536541875421").price(new BigDecimal("66")).quantity(new BigDecimal("3.87")).build());
    orderItemList2.add(OrderItem.builder().id(3).description("Kugelschreiber").gtin("9565789542102").price(new BigDecimal("4")).quantity(new BigDecimal("2.98")).build());
    Order order2 = Order.builder().uuid("o2").createdAt(new Date()).orderItemList(orderItemList2).orderedBy("Georg Gruber").build();
    orderItems.put(order2.getUuid(), order2);



  }



}
