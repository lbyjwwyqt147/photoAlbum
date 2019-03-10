package pers.liujunyi.cloud.photo.repository.elasticsearch.user;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.jpa.repository.Modifying;
import pers.liujunyi.cloud.photo.entity.UserAccounts;
import pers.liujunyi.common.repository.elasticsearch.BaseElasticsearchRepository;

import java.util.Date;
import java.util.List;

/***
 * 文件名称: UserAccountsElasticsearchRepository.java
 * 文件描述: 用户帐号信息 Elasticsearch Repository
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface UserAccountsElasticsearchRepository extends BaseElasticsearchRepository<UserAccounts, Long> {
    /**
     * 修改状态
     * @param userStatus   0：正常  1：冻结  2：离职
     * @param ids
     * @return
     */
    @Modifying(clearAutomatically = true)
    @Query("update UserAccounts u set u.userStatus = ?1, u.updateTime = ?2 where u.id in (?3)")
    int setUserStatusByIds(Byte userStatus, Date updateTime, List<Long> ids);



    /**
     * 修改用户密码
     * @param userPassword
     * @param id
     * @return
     */
    @Modifying(clearAutomatically = true)
    @Query("update UserAccounts u set u.userPassword = ?1, u.changePasswordTime = ?2, u.updateTime = ?2 where u.id = ?3")
    int setUserPasswordById(String userPassword, Date time, Long id);

    /**
     * 根据手机号获取信息
     * @param mobilePhone
     * @return
     */
    UserAccounts findFirstByMobilePhone(String mobilePhone);

    /**
     * 用户帐号登录
     * @param userAccounts  帐号
     * @param userPassword 密码
     * @return
     */
    UserAccounts findFirstByUserAccountsAndUserPassword(String userAccounts, String userPassword);

    /**
     * 用户手机登录
     * @param mobilePhone 手机号
     * @param userPassword 密码
     * @return
     */
    UserAccounts findFirstByMobilePhoneAndUserPassword(String mobilePhone, String userPassword);




}
