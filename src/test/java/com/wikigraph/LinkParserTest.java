package com.wikigraph;

import com.wikigraph.wikidump.LinkParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

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
    Assert.assertEquals(2, connections.size());
    Assert.assertTrue(connections.contains("test link 4"));
    Assert.assertTrue(connections.contains("test link"));
  }

  @Test
  public void testComments2() {
    Collection<String> connections = linkParser.getConnections("I have a [[test link]] then <!-- A comment --> then " +
            "[[linkz]] then<!-- ANOTHER COMMENT [[with link]] --> [[endlink]]");
    Assert.assertEquals(3, connections.size());
    Assert.assertTrue(connections.contains("test link"));
    Assert.assertTrue(connections.contains("endlink"));
    Assert.assertTrue(connections.contains("linkz"));
  }

  @Test
  public void testComments3() {
    Collection<String> connections = linkParser.getConnections("<!-- h[[linkfake]] or [[2fake]] --> [[2furious]] " +
            "<!--comment-->words<!--comment2!-->[[andlink!]]");
    Assert.assertEquals(2, connections.size());
    Assert.assertTrue(connections.contains("2furious"));
    Assert.assertTrue(connections.contains("andlink!"));
  }

  @Test
  public void testIgnoreReferences() {
    Collection<String> connections = linkParser.getConnections("There is a [[test link]] and we want this but not the" +
            " reference\n==References==\n [[HI IM A REFERENCE]] and [[I'm also one]]");
    Assert.assertEquals(1, connections.size());
    Assert.assertTrue(connections.contains("test link"));
  }

  @Test
  public void testForStupidEditors() {
    Collection<String> connections = linkParser.getConnections("[[link]] <!--Unterminated Comment :( [[with " +
            "link]]some words [[ A link where some idiot \n didnt close it [[other]] [[and oh my god an unterminated " +
            "ink");
    Assert.assertEquals(1, connections.size());
    Assert.assertTrue(connections.contains("link"));
  }


}
