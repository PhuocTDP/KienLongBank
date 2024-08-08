package com.klb.caches.service;

import com.klb.caches.model.Tutorial;
import com.klb.caches.repository.TutorialRepository;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

@Service
@EnableCaching
@Slf4j
public class TutorialService {

  @Autowired
  private TutorialRepository tutorialRepository;

  @Cacheable(value = "tutorials_all")
  public List<Tutorial> findAll() {
    log.info("Fetching all tutorials from database");
    return tutorialRepository.findAll();
  }

  @Cacheable(unless = "#result == null", value = "tutorials_by_title", key = "#title")
  public List<Tutorial> findByTitleContaining(String title) {
    log.info("Fetching tutorials with title containing '{}' from database", title);
    return tutorialRepository.findByTitleContaining(title);
  }

  @Cacheable(unless = "#result == null", value = "tutorial_by_id", key = "#id")
  public Optional<Tutorial> findById(long id) {
    log.info("Fetching tutorial with id '{}' from database", id);
    return tutorialRepository.findById(id);
  }

  @CachePut(value = {"tutorial_by_id", "tutorials_all", "tutorials_by_title",
      "published_tutorials"})
  public Tutorial save(Tutorial tutorial) {
    log.info("Saving tutorial with id '{}' to database", tutorial.getId());
    return tutorialRepository.save(tutorial);
  }

  @CachePut(value = {"tutorial_by_id", "tutorials_all"})
  public Tutorial update(Tutorial tutorial) {
    log.info("Updating tutorial with id '{}' in database", tutorial.getId());
    return tutorialRepository.save(tutorial);
  }

  @CacheEvict(value = {"tutorial_by_id", "tutorials_all", "tutorials_by_title",
      "published_tutorials"}, allEntries = true, key = "#id")
  public void deleteById(long id) {
    log.info("Deleting tutorial with id '{}' from database", id);
    tutorialRepository.deleteById(id);
  }

  @CacheEvict(value = {"tutorial_by_id", "tutorials_all", "tutorials_by_title",
      "published_tutorials"}, allEntries = true)
  public void deleteAll() {
    log.info("Deleting all tutorials from database");
    tutorialRepository.deleteAll();
  }

  @Cacheable(value = "published_tutorials", key = "#isPublished")
  public List<Tutorial> findByPublished(boolean isPublished) {
    log.info("Fetching tutorials with published status '{}' from database", isPublished);
    return tutorialRepository.findByPublished(isPublished);
  }
}
