package com.soccerbee.api.news;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.soccerbee.common.EnumResponse;

@RestController
@RequestMapping("/news")
public class NewsController {
  @Autowired NewsService newsService;

  @PostMapping("/regist")
  public EnumResponse regist(NewsDto newsDto) {
    return newsService.regist(newsDto);
  }

  @PostMapping("/modify")
  public EnumResponse modify(@RequestBody NewsDto newsDto) {
    return newsService.modify(newsDto);
  }

  @PostMapping("/modifyImage")
  public EnumResponse modifyImage(NewsDto newsDto) {
    return newsService.modifyImage(newsDto);
  }

  @GetMapping("/getList")
  public List<NewsDto> getList() {
    return newsService.getList();
  }

  @DeleteMapping("/delete/{idfNoticeNew}")
  public EnumResponse delete(@PathVariable Integer idfNoticeNew) {
    return newsService.delete(idfNoticeNew);
  }
}

