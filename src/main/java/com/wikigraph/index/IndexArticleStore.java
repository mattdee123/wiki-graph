package com.wikigraph.index;

import com.wikigraph.graph.Article;
import com.wikigraph.graph.ArticleStore;
import com.wikigraph.wikidump.TitleFixer;

import java.io.File;
import java.util.Map;

import static com.wikigraph.index.Index.ARTICLE_DIR;
import static com.wikigraph.index.Index.ARTICLE_HASH_DIR;
import static com.wikigraph.index.Index.IN_DIR;
import static com.wikigraph.index.Index.OUT_DIR;

public class IndexArticleStore implements ArticleStore {


  private final ArticleIndex articleIndex;
  private final ArticleHashIndex articleHashIndex;
  private final LinkIndex outgoingIndex;
  private final LinkIndex incomingIndex;

  public IndexArticleStore(File dir) {
    articleIndex = new ArticleIndex(new File(dir, ARTICLE_DIR));
    outgoingIndex = new LinkIndex(new File(dir, OUT_DIR));
    incomingIndex = new LinkIndex(new File(dir, IN_DIR));
    articleHashIndex = new ArticleHashIndex(new File(dir, ARTICLE_HASH_DIR));
  }

  @Override
  public Article forTitle(String title) {
    title = TitleFixer.getFixer().toTitle(title);
    Integer id = articleHashIndex.getId(title);
    if (id == null) {
      return null;
    } else {
      IndexArticle article = new IndexArticle(id, outgoingIndex, incomingIndex, articleIndex);
      article.setTitle(title);
      return article;
    }
  }

  @Override
  public int numberOfArticles() {
    return articleIndex.size();
  }

  @Override
  public Article forId(int id) {
    return new IndexArticle(id, outgoingIndex, incomingIndex, articleIndex);
  }
}
