package pers.liujunyi.cloud.photo.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import pers.liujunyi.common.util.UserUtils;

import java.util.Optional;

/***
 * AuditorAware　设置当前用户ＩＤ
 */
@Configuration
public class UserIDAuditorBean implements AuditorAware<Long> {

    @Autowired
    private UserUtils userUtils;

    @Override
    public Optional<Long> getCurrentAuditor() {
        Long currentUserId = this.userUtils.getPresentLoginUserId();
        return Optional.ofNullable(currentUserId);
    }
}
