package pers.liujunyi.cloud.photo;

import com.alibaba.fastjson.JSON;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.common.util.DateTimeUtils;
import pers.liujunyi.cloud.common.vo.menus.ModuleTreeBuilder;
import pers.liujunyi.cloud.common.vo.menus.ModuleVo;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<ModuleVo> moduleVoList = new LinkedList<>();
        ModuleVo rootModule1 = new ModuleVo();
        rootModule1.setId(1L);
        rootModule1.setModuleCode("1001");
        rootModule1.setModuleName("系统设置");
        rootModule1.setModuleType((byte)1);
        rootModule1.setModulePid(0L);
        rootModule1.setMenuIcon("flaticon-cogwheel");
        rootModule1.setMenuOpenUrl("");
        rootModule1.setStatus((byte)0);
        moduleVoList.add(rootModule1);

        List<String> functionButton = new LinkedList<>();
        functionButton.add("1");
        functionButton.add("2");
        functionButton.add("3");
        functionButton.add("4");
        functionButton.add("5");
        functionButton.add("6");
        functionButton.add("7");
        functionButton.add("8");
        functionButton.add("9");
        functionButton.add("10");




        ModuleVo childModule1 = new ModuleVo();
        childModule1.setId(10L);
        childModule1.setModuleCode("1010");
        childModule1.setModuleName("数据字典");
        childModule1.setModuleType((byte)2);
        childModule1.setModulePid(1L);
        childModule1.setMenuIcon("la-outdent");
        childModule1.setMenuOpenUrl("../dict/dict.html");
        childModule1.setStatus((byte)0);
        childModule1.setFunctionButtonGroup(functionButton);
        moduleVoList.add(childModule1);

        ModuleVo childModule2 = new ModuleVo();
        childModule2.setId(11L);
        childModule2.setModuleCode("1011");
        childModule2.setModuleName("组织机构");
        childModule2.setModuleType((byte)2);
        childModule2.setModulePid(1L);
        childModule2.setMenuIcon("la-outdent");
        childModule2.setMenuOpenUrl("../organization/organization.html");
        childModule2.setStatus((byte)0);
        childModule2.setFunctionButtonGroup(functionButton);
        moduleVoList.add(childModule2);



        ModuleVo rootModule2 = new ModuleVo();
        rootModule2.setId(2L);
        rootModule2.setModuleCode("1002");
        rootModule2.setModuleName("相册管理");
        rootModule2.setModuleType((byte)1);
        rootModule2.setModulePid(0L);
        rootModule2.setMenuIcon("flaticon-web");
        rootModule2.setMenuOpenUrl("");
        rootModule2.setStatus((byte)0);
        moduleVoList.add(rootModule2);


        ModuleVo childModule3 = new ModuleVo();
        childModule3.setId(20L);
        childModule3.setModuleCode("1020");
        childModule3.setModuleName("写真集");
        childModule3.setModuleType((byte)2);
        childModule3.setModulePid(2L);
        childModule3.setMenuIcon("la-outdent");
        childModule3.setMenuOpenUrl("../photo/album/fahrenheit/fahrenheit.html");
        childModule3.setStatus((byte)0);
        childModule3.setFunctionButtonGroup(functionButton);
        moduleVoList.add(childModule3);

        ModuleVo childModule4 = new ModuleVo();
        childModule4.setId(21L);
        childModule4.setModuleCode("1021");
        childModule4.setModuleName("婚纱照");
        childModule4.setModuleType((byte)2);
        childModule4.setModulePid(2L);
        childModule4.setMenuIcon("la-outdent");
        childModule4.setMenuOpenUrl("../photo/album/wang/wang.html");
        childModule4.setStatus((byte)0);
        childModule4.setFunctionButtonGroup(functionButton);
        moduleVoList.add(childModule4);


        ModuleVo childModule5 = new ModuleVo();
        childModule5.setId(22L);
        childModule5.setModuleCode("1022");
        childModule5.setModuleName("轮播图");
        childModule5.setModuleType((byte)2);
        childModule5.setModulePid(2L);
        childModule5.setMenuIcon("la-outdent");
        childModule5.setMenuOpenUrl("../photo/album/carousel/carousel.html");
        childModule5.setStatus((byte)0);
        childModule5.setFunctionButtonGroup(functionButton);
        moduleVoList.add(childModule5);




        ModuleVo rootModule3 = new ModuleVo();
        rootModule3.setId(3L);
        rootModule3.setModuleCode("1003");
        rootModule3.setModuleName("用户管理");
        rootModule3.setModuleType((byte)1);
        rootModule3.setModulePid(0L);
        rootModule3.setMenuIcon("flaticon-users");
        rootModule3.setMenuOpenUrl("");
        rootModule3.setStatus((byte)0);
        moduleVoList.add(rootModule3);

        ModuleVo childModule6 = new ModuleVo();
        childModule6.setId(30L);
        childModule6.setModuleCode("1030");
        childModule6.setModuleName("员工管理");
        childModule6.setModuleType((byte)2);
        childModule6.setModulePid(3L);
        childModule6.setMenuIcon("la-outdent");
        childModule6.setMenuOpenUrl("../photo/users/staff/staff.html");
        childModule6.setStatus((byte)0);
        childModule6.setFunctionButtonGroup(functionButton);
        moduleVoList.add(childModule6);


        ModuleVo childModule7 = new ModuleVo();
        childModule7.setId(31L);
        childModule7.setModuleCode("1031");
        childModule7.setModuleName("顾客管理");
        childModule7.setModuleType((byte)2);
        childModule7.setModulePid(3L);
        childModule7.setMenuIcon("la-outdent");
        childModule7.setMenuOpenUrl("../photo/users/customer/customer.html");
        childModule7.setStatus((byte)0);
        childModule7.setFunctionButtonGroup(functionButton);
        moduleVoList.add(childModule7);

        System.out.println(JSON.toJSONString(ResultUtil.success(ModuleTreeBuilder.buildListToTree(moduleVoList))));

        LocalDate localDate = LocalDate.now();
        System.out.println("LocalDate = " + localDate);

        System.out.println(DateTimeUtils.getCurrentDate());
    }
}
