package pers.liujunyi.cloud.photo.service.user.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.liujunyi.cloud.photo.entity.UserAccounts;
import pers.liujunyi.cloud.photo.repository.elasticsearch.user.UserAccountsElasticsearchRepository;
import pers.liujunyi.cloud.photo.service.user.UserAccountsElasticsearchService;
import pers.liujunyi.common.repository.elasticsearch.BaseElasticsearchRepository;
import pers.liujunyi.common.service.impl.BaseElasticsearchServiceImpl;

/***
 * 文件名称: UserAccountsElasticsearchServiceImpl.java
 * 文件描述: 用户帐号信息 Elasticsearch Service impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class UserAccountsElasticsearchServiceImpl extends BaseElasticsearchServiceImpl<UserAccounts, Long> implements UserAccountsElasticsearchService {

    @Autowired
    private UserAccountsElasticsearchRepository userAccountsElasticsearchRepository;

    public UserAccountsElasticsearchServiceImpl(BaseElasticsearchRepository<UserAccounts, Long> baseElasticsearchRepository) {
        super(baseElasticsearchRepository);
    }


}