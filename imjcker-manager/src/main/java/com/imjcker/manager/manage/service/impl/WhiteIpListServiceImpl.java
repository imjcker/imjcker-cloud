package com.imjcker.manager.manage.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.imjcker.manager.manage.model.PageInfo;
import com.lemon.common.exception.ExceptionInfo;
import com.lemon.common.exception.vo.DataValidationException;
import com.lemon.common.vo.WhiteIpList;
import com.imjcker.manager.manage.mapper.WhiteIpListMapper;
import com.imjcker.manager.manage.service.RedisService;
import com.imjcker.manager.manage.service.WhiteIpListService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class WhiteIpListServiceImpl implements WhiteIpListService {

    private static Logger logger = LoggerFactory.getLogger(WhiteIpListServiceImpl.class);

    @Autowired
    private WhiteIpListMapper whiteIpListMapper;
    @Autowired
    private RedisService redisService;

    @Override
    public Map<String, Object> show(JSONObject jsonObject) {
        Map<String, Object> map = new HashMap<>();
        Integer currPage = jsonObject.getInteger("currPage");
        Integer pageSize = jsonObject.getInteger("pageSize");
        PageInfo pageInfo = new PageInfo(currPage, pageSize);
        List<WhiteIpList> ipList = whiteIpListMapper.findByPage(pageInfo);
        Integer totalCount = whiteIpListMapper.getTotalCount();
        pageInfo.setTotalCount(totalCount);
        map.put("list", ipList);
        map.put("currPage", pageInfo.getCurrentPage());
        map.put("pageSize", pageInfo.getPageSize());
        map.put("totalCount", pageInfo.getTotalCount());
        map.put("totalPage", pageInfo.getTotalPage());
        return map;
    }

    @Override
    public boolean delete(JSONObject jsonObject) {
        try {
            Integer status = whiteIpListMapper.deleteById(jsonObject.getInteger("id"));
            WhiteIpList whiteIp = whiteIpListMapper.findIpAddressById(jsonObject.getInteger("id"));
            String ipAddress = whiteIp.getIpAddress();
            String prefix;
            Set<String> ipOldSet = new HashSet<>();
            if (!ipAddress.contains("-")) {
                int index = ipAddress.lastIndexOf(".");
                prefix = ipAddress.substring(0, index + 1);//172.32.3.
                ipOldSet.add(ipAddress);//获取ip
            } else {
                String[] ipStr = ipAddress.split("-");
                int index = ipStr[0].lastIndexOf(".");
                prefix = ipStr[0].substring(0, index + 1);//172.32.3.
                ipOldSet = getIpList(ipAddress);//获取网段ip列表
            }
            ipOldSet.forEach(ip -> {
                redisService.delete("ip:" + ip);
            });
        } catch (Exception e) {
            logger.error("delete error:{}", e);
            return false;
        }
        return true;
    }

    @Override
    public Map<String, Object> search(JSONObject jsonObject) {
        Map<String, Object> map = new HashMap<>();
        Integer appKeyId = jsonObject.getInteger("appKeyId");
        String ipAddress = jsonObject.getString("ipAddress");
//        if ((StringUtils.isBlank(ipAddress)||"".equals(ipAddress)) && null == appKeyId) {
        if (null == appKeyId && StringUtils.isBlank(ipAddress)) {
            return this.show(jsonObject);
        }
        Integer currPage = jsonObject.getInteger("currPage");
        Integer pageSize = jsonObject.getInteger("pageSize");
        //入参为网段
        List<WhiteIpList> whiteIp = new ArrayList<>();
        Integer totalCountByIpAddress;
        PageInfo pageInfo = new PageInfo(currPage, pageSize);
        if (ipAddress.contains("-")) {
            int index = ipAddress.lastIndexOf(".");
            String prefix = ipAddress.substring(0, index + 1);//获取ip网段前缀查询
            whiteIp = whiteIpListMapper.findByIpAddress(prefix, appKeyId, currPage, pageSize);//查数据库所有记录,模糊查询所有ip前缀的数据
            totalCountByIpAddress = whiteIpListMapper.getTotalCountByIpAddress(prefix, appKeyId);
        } else {
            whiteIp = whiteIpListMapper.findByIpAddress(ipAddress, appKeyId, currPage, pageSize);//查数据库所有记录,模糊查询所有ip前缀的数据
            totalCountByIpAddress = whiteIpListMapper.getTotalCountByIpAddress(ipAddress, appKeyId);
        }
        pageInfo.setTotalCount(totalCountByIpAddress);
        map.put("list", whiteIp);
        map.put("currPage", pageInfo.getCurrentPage());
        map.put("pageSize", pageInfo.getPageSize());
        map.put("totalCount", pageInfo.getTotalCount());
        map.put("totalPage", pageInfo.getTotalPage());
        return map;
    }

    @Override
    public boolean save(JSONObject jsonObject) throws ParseException {
        //校验是否存在
        WhiteIpList whiteIpList = jsonObject.toJavaObject(WhiteIpList.class);
        Map<String, Object> map = isExist(whiteIpList, false);
        if (!map.isEmpty() && map.getOrDefault("isExist", true).equals(true)) {
            String existIp = map.get("IP").toString();
            throw new DataValidationException(existIp + ExceptionInfo.EXIST);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(sdf.format(new Date()));
        whiteIpList.setUpdateTime(date);
        whiteIpList.setCreateTime(date);
        Integer status = whiteIpListMapper.insert(whiteIpList);
        //缓存同步
        String ipList = map.get("IP").toString();
        if (StringUtils.isNotBlank(ipList)) {
            Set<String> ipSet = new HashSet<>();
            Collections.addAll(ipSet, ipList);//ip列表转换为set集合，依次遍历set集合存入缓存
            ipSet.forEach(ip -> {
                redisService.setToCaches("ip:" + ip, true);
            });
        }
        return true;
    }

    @Override
    public boolean edit(JSONObject jsonObject) throws ParseException {
        WhiteIpList whiteIpList = jsonObject.toJavaObject(WhiteIpList.class);
        Map<String, Object> map = isExist(whiteIpList, true);
        if (null != map && map.getOrDefault("isExist", true).equals(true)) {
            String existIp = map.get("IP").toString();
            throw new DataValidationException(existIp + ExceptionInfo.EXIST);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(sdf.format(new Date()));
        whiteIpList.setUpdateTime(date);
        Integer status = whiteIpListMapper.update(whiteIpList);
        //删除修改前的缓存,先获取ip集合，再删除缓存
        Integer id = whiteIpList.getId();
        WhiteIpList whiteIp = whiteIpListMapper.findIpAddressById(id);
        String ipAddress = whiteIp.getIpAddress();
        String prefix;
        Set<String> ipOldSet = new HashSet<>();
        if (!ipAddress.contains("-")) {
            int index = ipAddress.lastIndexOf(".");
            prefix = ipAddress.substring(0, index + 1);//172.32.3.
            ipOldSet.add(ipAddress);//获取ip
        } else {
            String[] ipStr = ipAddress.split("-");
            int index = ipStr[0].lastIndexOf(".");
            prefix = ipStr[0].substring(0, index + 1);//172.32.3.
            ipOldSet = getIpList(ipAddress);//获取网段ip列表
        }
        ipOldSet.forEach(ip -> {
            redisService.delete("ip:" + ip);
        });
        //修改后,缓存同步
        String ipList = map.get("IP").toString();
        if (StringUtils.isNotBlank(ipList)) {
            Set<String> ipSet = new HashSet<>();
            Collections.addAll(ipSet, ipList);//ip列表转换为set集合，依次遍历set集合存入缓存
            ipSet.forEach(ip -> {
                redisService.setToCaches("ip:" + ip, true);
            });
        }
        return true;
    }

    //校验网段172.32.3.175-177
    public Map<String, Object> isExist(WhiteIpList whiteIpList, boolean flag) {
        Map<String, Object> result = new HashMap<>();
        String ipAddress = whiteIpList.getIpAddress();
        //处理入参，收集所有入参ip列表
        Set<String> ipList = new HashSet<>();
        List<WhiteIpList> allIpAddress = new ArrayList<>();
        String prefix;
        if (!ipAddress.contains("-")) {
            int index = ipAddress.lastIndexOf(".");
            prefix = ipAddress.substring(0, index + 1);//172.32.3.
            ipList.add(ipAddress);//获取ip
        } else {
            String[] ipStr = ipAddress.split("-");
            int index = ipStr[0].lastIndexOf(".");
            prefix = ipStr[0].substring(0, index + 1);//172.32.3.
            ipList = getIpList(ipAddress);//获取网段ip列表
        }
        //校验: 库无记录，不存在
        if (flag)
            allIpAddress = whiteIpListMapper.findIpAddressIgnoreSelf(prefix, whiteIpList.getId());//查数据库所有记录,模糊查询所有ip前缀的数据
        else
            allIpAddress = whiteIpListMapper.findIpAddress(prefix);//查数据库所有记录,模糊查询所有ip前缀的数据
        if (null == allIpAddress || allIpAddress.size() <= 0) {
            result.put("isExist", false);
            result.put("IP", ipList);
            return result;
        }
        //2.2匹配数据库，两种情况(库里存的单条，库里存的网段)
        //依次遍历入参的ip列表
        boolean isContain = false;
        for (String ip : ipList) {
            isContain = isIpContainList(allIpAddress, ip);
            if (isContain) {
                result.put("isExist", true);
                result.put("IP", ip);
                break;
            }
        }
        if (result.isEmpty()) {
            result.put("isExist", false);
            result.put("IP", ipList);
        }

        return result;
    }

    /**
     * 根据网段，获取ip列表（eg: 172.32.3.175-177）
     *
     * @param ipAddress
     * @return
     */
    public Set<String> getIpList(String ipAddress) {
        String[] ipList = ipAddress.split("-");//ipList[1]=177
        int index = ipList[0].lastIndexOf(".");
        String prefix = ipList[0].substring(0, index + 1);//172.32.3.
        Integer suffix = Integer.valueOf(ipList[0].substring(index + 1));//175
        Integer last = Integer.valueOf(ipList[1]);//177
        if (suffix >= last)
            throw new DataValidationException(ExceptionInfo.ERROR_VALUE);
        Set<String> list = new HashSet<>();
        for (; suffix <= last; suffix++) {
            list.add(prefix + suffix);
        }
        return list;
    }

    /**
     * ip匹配allIpAddress集合
     *
     * @param allIpAddress
     * @param ip
     * @return
     */
    public boolean isIpContainList(List<WhiteIpList> allIpAddress, String ip) {
        boolean isContain = false;
        for (WhiteIpList ipTemp : allIpAddress) {//ipAddressTemp为数据库中存的网段
            Set<String> tempList = new HashSet<>();
            String ipAddressTemp = ipTemp.getIpAddress();
            if (!ipAddressTemp.contains("-")) {
                tempList.add(ipAddressTemp);
            } else {
                tempList = getIpList(ipAddressTemp);//获取网段ip列表
            }
            isContain = tempList.contains(ip);
            if (isContain) {
                logger.info("ip:{}已存在", ip);
                break;
            }
        }
        return isContain;
    }

    /**
     * 同步现有数据库历史数据到redis中
     */
    @Override
    public void syncRedis() {
        List<WhiteIpList> ipList = whiteIpListMapper.findAll();
        logger.info("ip白名單大小:{}",ipList.size());
        if (null != ipList && ipList.size() > 0) {
            ipList.forEach(whiteIp -> {
                String ipAddress = whiteIp.getIpAddress();
                if (!ipAddress.contains("-")) {//ip
                    logger.info("ip白名單:{}",ipAddress);
                    redisService.setToCaches("ip:" + ipAddress, true);
                } else {//ip段，获取ip列表
                    Set<String> ipSet = getIpList(ipAddress);//获取网段ip列表
                    logger.info("ipAddress白名單:{},size={}",ipAddress,ipSet.size());
                    ipSet.forEach(ip -> {
                        redisService.setToCaches("ip:" + ip, true);
                    });
                }
            });
        }
    }
}
