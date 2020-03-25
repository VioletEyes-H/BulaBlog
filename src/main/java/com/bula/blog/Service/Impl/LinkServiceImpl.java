package com.bula.blog.Service.Impl;

import com.bula.blog.Entity.Link;
import com.bula.blog.Repository.LinkRepository;
import com.bula.blog.Service.LinkService;
import com.bula.blog.Util.PageResult;
import com.bula.blog.Util.Result;
import com.bula.blog.Util.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LinkServiceImpl implements LinkService {
    @Resource
    private LinkRepository linkRepository;

    @Override
    public Map<Byte, List<Link>> getAllLinks() {
        List<Link> links = (List<Link>) linkRepository.findAll(Sort.by(Sort.Direction.ASC, "linkRank"));
        if (!CollectionUtils.isEmpty(links)) {
            //根据type进行分组
            Map<Byte, List<Link>> linksMap = links.stream().collect(Collectors.groupingBy(Link::getLinkType));
            return linksMap;
        }
        return null;
    }

    @Override
    public PageResult getLinksList(int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.ASC, "linkRank"));
        Page linkPage = linkRepository.findAll(pageable);
        PageResult pageResult = new PageResult();
        pageResult.setTotalPage(linkPage.getTotalPages());//查询总页数
        pageResult.setTotalCount(linkPage.getNumberOfElements());//查询总记录数
        pageResult.setCurrPage(linkPage.getNumber() + 1);//查询当前页数
        pageResult.setPageSize(limit);//每页总记录数
        pageResult.setList(linkPage.getContent());//数据源
        return pageResult;
    }

    @Override
    @Transactional
    public Result save(Link link) {
        Link lk = linkRepository.findByLinkName(link.getLinkName());
        if (null == lk) {
            Link saveLink = linkRepository.save(link);
            if (null == saveLink) {
                return ResultGenerator.genFailResult("添加失败");
            } else {
                return ResultGenerator.genSuccessResult("添加成功");
            }
        }
        return ResultGenerator.genFailResult("添加失败，友链已存在");
    }

    @Override
    @Transactional
    public Result update(Link link) {
        Link lk = linkRepository.findByLinkName(link.getLinkName());
        if (null == lk) {
            return ResultGenerator.genFailResult("数据不存在");
        }
        lk.setLinkUrl(link.getLinkUrl());
        lk.setLinkType(link.getLinkType());
        lk.setLinkRank(link.getLinkRank());
        lk.setLinkDescription(link.getLinkDescription());
        return ResultGenerator.genSuccessResult("修改成功");
    }

    @Override
    public Result findById(int id) {
        Link link = linkRepository.findByLinkId(id);
        if (null == link) {
            return ResultGenerator.genFailResult("数据不存在");
        }
        return ResultGenerator.genSuccessResult(link);
    }

    @Override
    @Transactional
    public Result delete(Integer[] ids) {
        for (Integer i : ids) {
            linkRepository.deleteById(i);
        }
        return ResultGenerator.genSuccessResult("删除成功");
    }
}
