package ru.ws.utils.security;

import ru.ws.utils.Constants;
import ru.ws.utils.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebFilter(urlPatterns = {"/*"})
public class CorsFilter implements Filter {

    @Value("${logging.version}")
    private String loggingVersion;
    
    @Value("${link.verify}")
    private String linkVerify;

    private static final Logger logger = LoggerFactory.getLogger(CorsFilter.class);

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        String username;
        HttpServletResponse response = null;
        HttpServletRequest request = null;
        Map<String, String> mapHeader = null;

        try {

            MDC.put(Constants.VERSION, loggingVersion);            

            // Lets make sure that we are working with HTTP (that is, against HttpServletRequest and HttpServletResponse objects)
            if (req instanceof HttpServletRequest && res instanceof HttpServletResponse) {

                response = (HttpServletResponse) res;
                request = (HttpServletRequest) req;
                
                mapHeader = Utility.getHeadersInfo(request);
                MDC.put(Constants.ID_TRANSACTION, mapHeader.get(Constants.ID_TRANSACTION));
                username = mapHeader.get(Constants.X_USERNAME);
                MDC.put(Constants.USER, username);
                
                logger.debug("Start Verify CorsFilter");
                logger.debug("Call USER: {}, METHOD: {}", username, request.getMethod());

                // Access-Control-Allow-Origin
                response.addHeader("Access-Control-Allow-Origin", linkVerify);

                // Access-Control-Max-Age
                response.addHeader("Access-Control-Max-Age", "3600");

                // Access-Control-Allow-Credentials
                response.addHeader("Access-Control-Allow-Credentials", "true");

                // Access-Control-Allow-Methods
                response.addHeader("Access-Control-Allow-Methods", "POST, GET, DELETE,PUT,OPTIONS");

                // Access-Control-Allow-Headers
                response.addHeader("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization, x_username, id_transaction");
                response.addHeader("Access-Control-Expose-Headers", "x_username, id_transaction");

                response.addHeader("If-Modified-Since", "Mon, 26 Jul 1997 05:00:00 GMT");
                response.addHeader("Cache-Control", "no-cache");
                
                //OPTIONS: method preflight
                if (!"OPTIONS".equalsIgnoreCase(request.getMethod())) {
                    chain.doFilter(req, res);
                }
            }
        } catch (Exception e) {
            logger.error("The error is: ", e);
        } finally {
            logger.debug("End Verify CorsFilter");
            MDC.remove(Constants.USER);
            MDC.remove(Constants.ID_TRANSACTION);
            MDC.remove(Constants.VERSION);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {}

}
