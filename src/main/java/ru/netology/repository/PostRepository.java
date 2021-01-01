package ru.netology.repository;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class PostRepository {

  private final AtomicLong maxPostId = new AtomicLong();
  private final Map<Long, Post> repositoryMap = new ConcurrentHashMap<>();

  public PostRepository() {
    maxPostId.set(1);
  }

  public List<Post> all() {
    return new ArrayList<>(repositoryMap.values());
  }

  public Optional<Post> getById(long id) {
    return Optional.ofNullable(repositoryMap.get(id));
  }

  public Post save(Post post) {
    if (post.getId() == 0) {
      var newId = generateNewId();
      post.setId(newId);
      repositoryMap.put(newId, post);
    } else {
      if (repositoryMap.containsKey(post.getId())) {
        repositoryMap.replace(post.getId(), post);
      } else {
        throw new NotFoundException();
      }
    }
    return post;
  }

  private long generateNewId() {
    return maxPostId.getAndIncrement();
  }

  public boolean removeById(long id) {
    if (repositoryMap.containsKey(id)) {
      repositoryMap.remove(id);
      return true;
    }
    return false;
  }
}
