package com.example.demo.mapper;

import com.example.demo.pojo.SupplementRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SupplementRuleMapper {

    @Select("select * from supplement_rule_tbl ORDER BY major_id asc ;")
    List<SupplementRule> getAllSupplementRule();

}
