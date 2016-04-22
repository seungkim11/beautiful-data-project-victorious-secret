package edu.csula.datascience.r.acquisition;

import edu.csula.datascience.acquisition.Collector;

import java.util.Collection;

/**
 * Created by tj on 4/21/16.
 */
public class CommentCollector<T, CommentNode> implements Collector<T, CommentNode> {
  @Override
  public Collection<T> mungee(Collection<CommentNode> src) {
    return null;
  }

  @Override
  public void save(Collection<T> data) {

  }
}
