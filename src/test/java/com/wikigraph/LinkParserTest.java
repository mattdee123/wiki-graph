package com.wikigraph;

import java.util.Collection;
import java.util.List;

import com.wikigraph.wikidump.LinkParser;
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
    Collection<String> connections = linkParser.getConnections("text [[test link]]blah no[[test-link2]]");
    Assert.assertEquals(2, connections.size());
    Assert.assertTrue(connections.contains("test link"));
    Assert.assertTrue(connections.contains("test-link2"));
  }

  @Test
  public void testGetConnections_pipe() throws Exception {
    Collection<String> connections = linkParser.getConnections("[[test link | title]]");
    Assert.assertEquals(1, connections.size());
    Assert.assertTrue(connections.contains("test link"));

  }

  @Test
  public void testGetConnections_pound() throws Exception {
    Collection<String> connections = linkParser.getConnections("[[test link#testing|name]][[#section]]");
    Assert.assertEquals(1, connections.size());
    Assert.assertTrue(connections.contains("test link"));
  }

  @Test
  public void testGetConnections_pipeAndPound() throws Exception {
    Collection<String> connections = linkParser.getConnections("[[test link#hi | title]]");
    Assert.assertEquals(1, connections.size());
    Assert.assertTrue(connections.contains("test link"));
  }

  @Test
  public void testGetConnections_braces() throws Exception {
    Collection<String> connections = linkParser.getConnections("currency = [[{{#property:p38}}]] ($)");
    Assert.assertEquals(0, connections.size());
  }

  @Test
  public void testComments() {
    Collection<String> connections = linkParser.getConnections("Here is a [[test link]] <!-- P.S. - don't include " +
            "this [[test link 2]] or [[test link3]] okay? --> but lets include [[test link 4]].  Sound good?");
    Assert.assertTrue(connections.contains("test link 4"));
    Assert.assertTrue(connections.contains("test link"));
  }

  @Test
  public void testIgnoreReferences() {
    Collection<String> connections = linkParser.getConnections("There is a [[test link]] and we want this but not the" +
            " reference\n==References==\n [[HI IM A REFERENCE]] and [[I'm also one]]");
    System.out.println(connections);
    Assert.assertEquals(1, connections.size());
    Assert.assertTrue(connections.contains("test link"));
  }
}
