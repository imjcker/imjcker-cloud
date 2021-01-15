package com.imjcker.gateway.locator;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.zuul.filters.RefreshableRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * freshExecutor period 30 seconds
 */
public class CustomRouteLocator extends SimpleRouteLocator implements RefreshableRouteLocator {

    private static final Logger log = LoggerFactory.getLogger(CustomRouteLocator.class);
    private static final String asynchronous = "asynchronous";
    private static final String callbackUrl = "callbackUrl";

    private JdbcTemplate jdbcTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
    private ZuulProperties zuulProperties;
    private String asynchronousService;
    private DiscoveryClient discoveryClient;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public CustomRouteLocator(String servletPath, ZuulProperties properties, String asynchronousService, DiscoveryClient discoveryClient) {
        super(servletPath, properties);
        this.zuulProperties = properties;
        this.asynchronousService = asynchronousService;
        this.discoveryClient = discoveryClient;
    }

    @Override
    public void refresh() {
        doRefresh();
    }

    @Override
    protected Map<String, ZuulProperties.ZuulRoute> locateRoutes() {
        LinkedHashMap<String, ZuulProperties.ZuulRoute> routesMap = new LinkedHashMap<>();
        //从db中加载路由信息
        routesMap.putAll(locateFromDB());
        //从配置文件中加载路由信息
        routesMap.putAll(super.locateRoutes());
/*        log.info("路由记录: {}", routesMap.size());
        routesMap.forEach((k, v)-> log.info("k = {} v = {}", k, v));*/

        LinkedHashMap<String, ZuulProperties.ZuulRoute> values = new LinkedHashMap<>();
        routesMap.forEach((k, v) -> {
            if (!k.startsWith("/")) {
                k = "/" + k;
            }
            if (StringUtils.hasText(this.zuulProperties.getPrefix())) {
                k = this.zuulProperties.getPrefix() + k;
                if (!k.startsWith("/")) {
                    k = "/" + k;
                }
            }
            values.put(k, v);
        });
        return values;
    }

    private Map<String, ZuulProperties.ZuulRoute> locateFromDB() {
        LinkedHashMap<String, ZuulProperties.ZuulRoute> routes = new LinkedHashMap<>();
        Set<ZuulRouteVO> serviceRouters = redisTemplate.opsForSet().members("serviceRouters");
        if (CollectionUtils.isEmpty(serviceRouters)) {
            serviceRouters = new HashSet<>(jdbcTemplate.query("SELECT * FROM service_router WHERE enabled = TRUE ORDER BY path DESC ", new BeanPropertyRowMapper<>(ZuulRouteVO.class)));
            if (CollectionUtils.isNotEmpty(serviceRouters)) {
                redisTemplate.opsForZSet().add("serviceRouters", serviceRouters);

                serviceRouters.forEach(result -> {
                    if (StringUtils.isEmpty(result.getPath()) || StringUtils.isEmpty(result.getUrl())
                            || StringUtils.isEmpty(result.getServiceId())) {
                        return;
                    }
                    ZuulProperties.ZuulRoute zuulRoute = new ZuulProperties.ZuulRoute();
                    try {
                        BeanUtils.copyProperties(result, zuulRoute);
                    } catch (BeansException e) {
                        log.error("load properties from db error..{}", e.getMessage());
                    }
                    routes.put(zuulRoute.getPath(), zuulRoute);
                });
            }
        }
        return routes;
    }

    /**
     * rewrite match route to specify a custom rule, in this case, synchronize or not.
     *
     * @param path original request path
     * @return a matching route.
     */
    @Override
    public Route getMatchingRoute(final String path) {
        Route route = super.getMatchingRoute(path);
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> params = new HashMap<>();
        for (String key : parameterMap.keySet()) {
            params.put(key, parameterMap.get(key)[0]);
        }
        // 0 同步, 1 异步
        String p_asynchronous = (String) params.get(asynchronous);
        String p_callbackUrl = (String) params.get(callbackUrl);

        //处理异步情况, 将路由改到异步服务
        if ("1".equals(p_asynchronous) && !StringUtils.isEmpty(p_callbackUrl)) {
            String location = route.getLocation();
            log.info("original location is: " + location);
            if (!StringUtils.isEmpty(asynchronousService)) {
                List<ServiceInstance> serviceInstances = discoveryClient.getInstances(asynchronousService);
                if (CollectionUtils.isNotEmpty(serviceInstances)) {
                    log.info("change location to: {}", asynchronousService);
                    route.setLocation(asynchronousService);
                } else {
                    log.error("didn't find any service named: {}, set location back to: {}", asynchronousService, route.getLocation());
                }
            }
        }
        return route;
    }


    public static class ZuulRouteVO {

        private String id;

        private String name;

        private String path;

        private String serviceId;

        private String url;

        private Boolean stripPrefix = true;

        private Boolean retryable;

        private Boolean enabled;

        private String description;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getServiceId() {
            return serviceId;
        }

        public void setServiceId(String serviceId) {
            this.serviceId = serviceId;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Boolean getStripPrefix() {
            return stripPrefix;
        }

        public void setStripPrefix(Boolean stripPrefix) {
            this.stripPrefix = stripPrefix;
        }

        public Boolean getRetryable() {
            return retryable;
        }

        public void setRetryable(Boolean retryable) {
            this.retryable = retryable;
        }

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }
    }

}
