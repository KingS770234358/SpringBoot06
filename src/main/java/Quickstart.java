// 导入需要的shiro包
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Simple Quickstart application showing how to use Shiro's API.*
 * @since 0.9 RC2
 */
public class Quickstart {
    // 使用日志门面输出日志
    private static final transient Logger log = LoggerFactory.getLogger(Quickstart.class);
    public static void main(String[] args) {
        // 工厂模式 加载配置文件
        // 配置文件中配置realms users roles 和 permissions
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        // 通过工厂创建一个 安全管理器对象 securityManager 实例
        SecurityManager securityManager = factory.getInstance();
        // 使 安全管理器实例成为JVM单例
        SecurityUtils.setSecurityManager(securityManager);
        // =========================以上是创建shiro安全环境========================
        // =========================重点配置========================
        //#############################1.获得当前执行用户对象Subject:###########################################
        Subject currentUser = SecurityUtils.getSubject();
        //#############################2.通过当前用户使用shiro的session#############
        // (而不是http session)做些事情 (对于 web 或者 EJB 容器不需要这个session)
        Session session = currentUser.getSession();
        // 利用session存储信息
        session.setAttribute("someKey", "aValue");
        // 利用session获取信息
        String value = (String) session.getAttribute("someKey");
        if (value.equals("aValue")) {
            log.info("Retrieved the correct value! [" + value + "]");
        }
        //#############################3.登录当前用户 角色 和 权限 是否被认证!###########################################
        if (!currentUser.isAuthenticated()) {
            // token通过账号密码生成的令牌
            UsernamePasswordToken token = new UsernamePasswordToken("lonestarr", "vespa");
            token.setRememberMe(true); // 设置记住我
            try {
                currentUser.login(token); // [重点]执行登录操作
            } catch (UnknownAccountException uae) {
                // 和.ini文件匹配时不存在当前账号会报错
                log.info("There is no user with username of " + token.getPrincipal());
            } catch (IncorrectCredentialsException ice) {
                // 和.ini文件匹配时密码错误会报错
                log.info("Password for account " + token.getPrincipal() + " was incorrect!");
            } catch (LockedAccountException lae) {
                // 用户锁定报错
                log.info("The account for username " + token.getPrincipal() + " is locked.  " +
                        "Please contact your administrator to unlock it.");
            }
            catch (AuthenticationException ae) {
                // 认证异常
            }
        }
        //4.输出身份信息 (一个用户名): currentUser.getPrincipal()返回用户名
        // currentUser.还有许多方法返回用户的不同信息
        log.info("User [" + currentUser.getPrincipal() + "] logged in successfully.");
        //5.测试当前用户是否有某个角色:
        if (currentUser.hasRole("schwartz")) {
            log.info("May the Schwartz be with you!");
        } else {
            log.info("Hello, mere mortal.");
        }
        //6.测试用户是否有以下权限 (非实例级) 是否有lightsabe:wield权限  [粗粒度-权限更多]
        if (currentUser.isPermitted("lightsaber:wield")) {
            log.info("You may use a lightsaber ring.  Use it wisely.");
        } else {
            log.info("Sorry, lightsaber rings are for schwartz masters only.");
        }
        //7.测试用户是否有winnebago:drive:eagle5权限  [细粒度-权限更少]
        if (currentUser.isPermitted("winnebago:drive:eagle5")) {
            log.info("You are permitted to 'drive' the winnebago with license plate (id) 'eagle5'.  " +
                    "Here are the keys - have fun!");
        } else {
            log.info("Sorry, you aren't allowed to drive the 'eagle5' winnebago!");
        }

        //8.结束退出!
        currentUser.logout();
        //9.结束系统
        System.exit(0);
    }
}
