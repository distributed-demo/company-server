package com.java4all.service.impl;

import com.java4all.dao.CompanyDao;
import com.java4all.service.CompanyService;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.extern.slf4j.Slf4j;
import org.bytesoft.bytetcc.supports.spring.aware.CompensableContextAware;
import org.bytesoft.compensable.CompensableContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * description:
 * tcc confirm逻辑
 * 1.@Service("companyServiceConfirm")这里必须指定此bean名称，tcc过程依靠此名称区分执行tcc中哪个逻辑
 * 2.参与tcc的方法必须添加@Transactional注解
 * 3.建议tcc每个步骤，方法执行后添加日志，方便问题排查
 *
 * @author wangzhongxiang
 * @date 2019/2/14 15:06
 */
@Slf4j
@Service("companyServiceConfirm")
public class CompanyServiceConfirm implements CompanyService,CompensableContextAware{

  /**tcc的上下文变量*/
  private CompensableContext compensableContext;

  @Autowired
  private CompanyDao companyDao;

  @Override
  @Transactional
  public int increaseMoney(Integer id, BigDecimal money) {
    //获取try阶段的参数
    Object money1 = this.compensableContext.getVariable("money");
    money = new BigDecimal(money1.toString());
    log.info("从CompensableContext中获取的try阶段的值为："+money);
    int line = companyDao.confirmIncreaseMoney(id, money);
    log.info("【confirm】 increaseMoney: id = "+id+",money ="+money);
    return line;
  }

  @Override
  public void setCompensableContext(CompensableContext context) {
    this.compensableContext = context;
  }
}
