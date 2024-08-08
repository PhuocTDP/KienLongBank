package com.klb.caches.controller;

import com.klb.caches.model.Tutorial;
import com.klb.caches.service.TutorialService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
@Slf4j
public class TutorialController {

  @Autowired
  TutorialService tutorialService;

  @GetMapping("/tutorials")
  public ResponseEntity<List<Tutorial>> getAllTutorials(
      @RequestParam(required = false) String title) {
    try {
      log.info("calling getAllTutorials with title {}", title);
      List<Tutorial> tutorials = new ArrayList<Tutorial>();

      if (title == null) {
        tutorials.addAll(tutorialService.findAll());
      } else {
        tutorials.addAll(tutorialService.findByTitleContaining(title));
      }

      if (tutorials.isEmpty()) {
        log.warn("no content");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      log.info("Tutorials response:\n {}", tutorials);
      return new ResponseEntity<>(tutorials, HttpStatus.OK);
    } catch (Exception e) {
      log.error("getAllTutorials error", e);
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/tutorials/{id}")
  public ResponseEntity<Tutorial> getTutorialById(@PathVariable("id") long id) {
    log.info("calling getTutorialById = {}", id);

    Optional<Tutorial> tutorialData = tutorialService.findById(id);
    log.info("Tutorial response:\n {}", tutorialData.map(Tutorial::toString).orElse(null));

    return tutorialData.map(tutorial -> new ResponseEntity<>(tutorial, HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @PostMapping("/tutorials")
  public ResponseEntity<Tutorial> createTutorial(@RequestBody Tutorial tutorial) {
    log.info("calling createTutorial");

    try {
      Tutorial _tutorial = tutorialService.save(
          new Tutorial(tutorial.getTitle(), tutorial.getDescription(), false));
      return new ResponseEntity<>(_tutorial, HttpStatus.CREATED);
    } catch (Exception e) {
      log.error("createTutorial error", e);
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping("/tutorials/{id}")
  public ResponseEntity<Tutorial> updateTutorial(@PathVariable("id") long id,
      @RequestBody Tutorial tutorial) {
    log.info("calling updateTutorial with id = " + id);

    Optional<Tutorial> tutorialData = tutorialService.findById(id);

    if (tutorialData.isPresent()) {
      Tutorial _tutorial = tutorialData.get();
      _tutorial.setTitle(tutorial.getTitle());
      _tutorial.setDescription(tutorial.getDescription());
      _tutorial.setPublished(tutorial.isPublished());
      Tutorial updatedTutorial = tutorialService.update(_tutorial);

      log.info("updatedTutorial: {}", updatedTutorial.toString());

      return new ResponseEntity<>(updatedTutorial, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/tutorials/{id}")
  public ResponseEntity<HttpStatus> deleteTutorial(@PathVariable("id") long id) {
    log.info("calling deleteTutorial with id = {}", id);

    try {
      tutorialService.deleteById(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      log.error("deleteTutorial error", e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/tutorials")
  public ResponseEntity<HttpStatus> deleteAllTutorials() {
    log.info("calling deleteAllTutorials ");

    try {
      tutorialService.deleteAll();
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      log.error("deleteAllTutorials error", e);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

  }

  @GetMapping("/tutorials/published")
  public ResponseEntity<List<Tutorial>> findByPublished() {
    try {
      List<Tutorial> tutorials = tutorialService.findByPublished(true);

      if (tutorials.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }
      return new ResponseEntity<>(tutorials, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
