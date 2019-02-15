package com.java4all.controller;

import com.java4all.feign.UserServiceApi;
import com.java4all.service.CompanyService;
import java.math.BigDecimal;
import lombok.extern.slf4j.Slf4j;
import org.bytesoft.compensable.Compensable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * description:
 * try逻辑有2种写法：
 * 1.直接写在controller中
 * 在controller中直接引入dao层，调用dao层方法，
 * 此时，@Compensable添加在controller中；
 * 2.写在接口的实现类中
 * 此时，在controller中引入实现类时，需要明确指定bean名称。
 * @Compensable注解，应该仅仅添加在controller中
 * 如果仅仅添加在try逻辑的实现类上，那么仅仅会执行try逻辑，cc逻辑不会执行；
 * 如果try逻辑的实现类和controller都添加，那么cc逻辑会执行两遍。
 *
 * 在官方文档中，明确指出了：必须在try的实现类添加@Compensable，而controller建议添加。
 * 此说法经过验证有问题。可能是之前版本遗留问题，但是文档没有及时更新。
 * @author wangzhongxiang
 * @date 2019/2/14 14:38
 */
@Compensable(
    interfaceClass = CompanyService.class,
    confirmableKey = "companyServiceConfirm",
    cancellableKey = "companyServiceCancel")
@Slf4j
@RestController
@RequestMapping("company")
public class CompanyController {

  /**
   * 在调用此接口时，首先执行的是tcc中的try,
   * 因此，我们需要明确指定引入的究竟是此接口的哪一个实现类，
   * 首先执行try，try逻辑写在companyServiceImpl类中，我们就需要明确指定，
   * @Autowired private CompanyService companyService
   * 上述这种写法是不可以的，无法识别究竟是哪一个实现类，因此实现类，需要明确指定bean名称
   * @Compensable注解扫描时，在哪一种状态执行哪个实现类方法，是
   * */
  @Autowired
  private CompanyService companyServiceImpl;

  @Autowired
  private UserServiceApi userServiceApi;

  /**
   * 由于tcc的每一个阶段，使用的参数是相同的，那么，如果在try时，如果对参数做了任何处理然后去操作数据库，
   * 那么在cc阶段的实现类中，也需要做相同的处理。
   * 否则，可以借助CompensableContextAware & CompensableContext
   * @param id
   * @param money
   */
  @PostMapping("increaseMoney")
  @Transactional
  public void increaseMoney(Integer id,BigDecimal money){
    log.info("money:"+money);
    //feign调用   调用user-server给企业加钱
    userServiceApi.increaseMoney(id,money.multiply(new BigDecimal(0.2)).setScale(2,BigDecimal.ROUND_HALF_UP));
    int line = companyServiceImpl.increaseMoney(id, money.multiply(new BigDecimal(0.8)).setScale(2,BigDecimal.ROUND_HALF_UP));
    log.info("修改行数为："+line);
  }
}
