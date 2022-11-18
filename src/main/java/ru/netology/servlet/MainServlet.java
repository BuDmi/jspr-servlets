package ru.netology.servlet;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
  private PostController controller;

  private final String METHOD_GET = "GET";
  private final String METHOD_POST = "POST";
  private final String METHOD_DELETE = "DELETE";

  private final String POST_PATH = "/api/posts";
  private final String POST_PATH_EXTRA = "/api/posts/\\d+";

  @Override
  public void init() {
    final var factory = new DefaultListableBeanFactory();
    final var reader = new XmlBeanDefinitionReader(factory);
    reader.loadBeanDefinitions("beans.xml");
    controller = factory.getBean(PostController.class);
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {
    // если деплоились в root context, то достаточно этого
    try {
      final var path = req.getRequestURI();
      final var method = req.getMethod();

      // primitive routing
      if (method.equals(METHOD_GET) && path.equals(POST_PATH)) {
        controller.all(resp);
        return;
      }
      if (method.equals(METHOD_GET) && path.matches(POST_PATH_EXTRA)) {
        // easy way
        final var id = getPostId(path);
        controller.getById(id, resp);
        return;
      }
      if (method.equals(METHOD_POST) && path.equals(POST_PATH)) {
        controller.save(req.getReader(), resp);
        return;
      }
      if (method.equals(METHOD_DELETE) && path.matches(POST_PATH_EXTRA)) {
        // easy way
        final var id = getPostId(path);
        controller.removeById(id, resp);
        return;
      }
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } catch (Exception e) {
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

  private long getPostId(String path) {
    return Long.parseLong(path.substring(path.lastIndexOf("/")));
  }
}

