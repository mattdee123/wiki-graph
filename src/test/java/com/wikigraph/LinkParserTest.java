package com.wikigraph;

import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LinkParserTest {

  private LinkParser linkParser;
  @Before
  public void setUp() throws Exception {
    linkParser = new LinkParser();
  }

  @Test
  public void testGetConnections_basicLink() throws Exception {
    List<String> connections = linkParser.getConnections("[[test link]]");
    Assert.assertEquals(1, connections.size());
    Assert.assertEquals("test link", connections.get(0));
  }

  @Test
  public void testGetConnections_twoLinks() throws Exception {
    List<String> connections = linkParser.getConnections("text [[test link]]blah no[[test-link2]]");
    Assert.assertEquals(2, connections.size());
    Assert.assertEquals("test link", connections.get(0));
    Assert.assertEquals("test-link2", connections.get(1));
  }

  @Test
  public void testGetConnections_pipe() throws Exception {
    List<String> connections = linkParser.getConnections("[[test link | title]]");
    Assert.assertEquals(1, connections.size());
    Assert.assertEquals("test link", connections.get(0));
  }

  @Test
  public void testGetConnections_pound() throws Exception {
    List<String> connections = linkParser.getConnections("[[test link#testing|name]][[#section]]");
    Assert.assertEquals(1, connections.size());
    Assert.assertEquals("test link", connections.get(0));
  }

  @Test
  public void testGetConnections_pipeAndPound() throws Exception {
    List<String> connections = linkParser.getConnections("[[test link#hi | title]]");
    Assert.assertEquals(1, connections.size());
    Assert.assertEquals("test link", connections.get(0));
  }
}
