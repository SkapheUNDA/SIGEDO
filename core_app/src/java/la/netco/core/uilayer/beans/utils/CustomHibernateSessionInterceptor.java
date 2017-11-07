package la.netco.core.uilayer.beans.utils;

import org.springframework.orm.hibernate3.support.OpenSessionInViewInterceptor;

public class CustomHibernateSessionInterceptor extends OpenSessionInViewInterceptor {

    public CustomHibernateSessionInterceptor() {
      setFlushMode(FLUSH_AUTO);
   }
}
