package at.digitrans.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Builder
public class Order {

  //Unique uuid of an order item
  private String uuid;

  //Creation date
  private Date createdAt;

  //Ordered by
  private String orderedBy;

  //List of ordered items
  private List<OrderItem> orderItemList = new ArrayList<>();

  @Tolerate
  public Order () {

  }


}
