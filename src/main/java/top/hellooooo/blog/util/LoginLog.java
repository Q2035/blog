package top.hellooooo.blog.util;

import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author Q
 * @Date 12/11/2020 21:43
 * @Description
 */
public class LoginLog {
    private Date lastLoginTime;
    // 登录失败次数
    private int failLoginCounts;

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public int increaseFailLoginCount(){
        this.failLoginCounts++;
        return this.failLoginCounts;
    }

    public int getFailLoginCounts() {
        return failLoginCounts;
    }

    public void setFailLoginCounts(int failLoginCounts) {
        this.failLoginCounts = failLoginCounts;
    }
}
