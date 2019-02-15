package com.java4all.feign;

import java.math.BigDecimal;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * description:
 * feign调用user-server的接口
 * @author wangzhongxiang
 * @date 2019/2/15 11:19
 */
@FeignClient(value = "user-server")
public interface UserServiceApi {

  @GetMapping(value = "/user/increaseMoney")
  void increaseMoney(@RequestParam("id")Integer id,@RequestParam("money")BigDecimal money);
}
