package com.java4all.service.impl;

import com.java4all.dao.CompanyDao;
import com.java4all.service.CompanyService;
import java.math.BigDecimal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * description:
 * tcc try逻辑
 * 1.@Service("companyServiceImpl")这里必须指定此bean名称，tcc过程依靠此名称区分执行tcc中哪个逻辑
 * 2.参与tcc的方法必须添加@Transactional注解
 * 3.建议tcc每个步骤，方法执行后添加日志，方便问题排查
 *
 * @author wangzhongxiang
 * @date 2019/2/14 15:06
 */
@Slf4j
@Service("companyServiceImpl")
public class CompanyServiceImpl implements CompanyService{

  @Autowired
  private CompanyDao companyDao;

  @Override
  @Transactional
  public int increaseMoney(Integer id, BigDecimal money) {
    int line = companyDao.increaseMoney(id, money);
    log.info("【try】 increaseMoney: id = "+id+",money ="+money);
    return line;
  }
}
