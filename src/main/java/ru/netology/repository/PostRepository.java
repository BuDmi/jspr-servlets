package ru.netology.repository;

import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

// Stub
public class PostRepository {
  private final List<Post> posts = new ArrayList<>();
  private int counterId = 0;
  public List<Post> all() {
    return posts;
  }

  public Optional<Post> getById(long id) {
    synchronized (posts) {
      if (!posts.isEmpty()) {
        for (int i = 1; i <= posts.size(); i++) {
          var curPost = posts.get(i);
          if (curPost.getId() == id) {
            return Optional.of(curPost);
          }
        }
      }
    }
    return Optional.empty();
  }

  public Post save(Post post) {
    synchronized (posts) {
      int newPostId = 0;
      if (post.getId() == newPostId) {
        counterId++;
        post.setId(counterId);
        posts.add(post);
      } else {
        for (var curPost: posts) {
          if (curPost.getId() == post.getId()) {
            curPost.setContent(post.getContent());
            return post;
          }
        }
        post.setId(posts.size());
        posts.add(post);
      }
    }
    return post;
  }

  public void removeById(long id) {
    synchronized (posts) {
      if (!posts.isEmpty()) {
        for (int i = 1; i <= posts.size(); i++) {
          if (posts.get(i).getId() == id) {
            posts.remove(i);
            return;
          }
        }
      }
    }
  }
}
