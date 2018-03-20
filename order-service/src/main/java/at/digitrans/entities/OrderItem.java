package at.digitrans.entities;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Builder
public class OrderItem {


  private int id;
  private String description;
  private String gtin;
  private BigDecimal quantity;
  private BigDecimal price;

  @Tolerate
  public OrderItem() {

  }

  /**
   * Get the line item amount
   * @return
   */
  public BigDecimal getLineItemAmount() {
    return quantity.multiply(price);
  }

}
