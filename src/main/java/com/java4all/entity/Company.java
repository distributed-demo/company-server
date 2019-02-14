package com.java4all.entity;

import java.math.BigDecimal;
import lombok.Data;

/**
 * description:
 *
 * @author wangzhongxiang
 * @date 2019/2/14 14:50
 */
@Data
public class Company {
  private int id;

  private BigDecimal money;

  private BigDecimal frozen;
}
