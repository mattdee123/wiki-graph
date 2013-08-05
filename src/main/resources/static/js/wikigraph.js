function WikiGraph() {}

WikiGraph.prototype = new BaseGraph();

WikiGraph.prototype.generateWikiLink_ = function(title, raw) {
  var wikiUrl = 'http://en.wikipedia.org/w/index.php?title={{ title }}' +
                 '{{#raw}}&action=raw{{/raw}}';
  var context = {title: encodeURIComponent(title), raw: raw};
  return Mustache.render(wikiUrl, context);
};

WikiGraph.prototype.showWikiLinks_ = function(data, page) {
  var self = this;

  var linkify = function(text) {
    var url = self.generateWikiLink_(text);
    var urlRaw = self.generateWikiLink_(text, true);
    var result = Util.generateLink(url, text, true);
    result += '-';
    result += Util.generateLink('#' + encodeURIComponent(text), '[Graph]');
    result += ' ';
    result += Util.generateLink(urlRaw, '[Raw]', true);
    return result;
  };

  result = _.map(data, linkify).join('<br>');
  self.renderLinks(result);
  var baseUrl = self.generateWikiLink_(page);
  var baseUrlRaw = self.generateWikiLink_(page, true);
  var baseLink = Util.generateLink(baseUrl, page, true);
  var baseLinkRaw = Util.generateLink(baseUrlRaw, '[raw]', true);
  $('#count-links').html(data.length);
  $('#label-page').html(baseLink + ' ' + baseLinkRaw);
};

WikiGraph.prototype.getDataForPage = function(page) {
  var self = this;
  $('#links').html('<i class="icon-spinner icon-spin icon-4x"></i>');
  $.ajax('/page', {
    method: 'get',
    data: {page: page},
    success: function(data) {
      data = JSON.parse(data);
      self.showWikiLinks_(data, page);
    },
    error: function() {
      $('#links').html('<h3>That page doesn\'t exist.</h3>');
      $('#count-links').html('0');
    }
  });
};

WikiGraph.prototype.init = function() {
  BaseGraph.prototype.init.apply(this);
};

var graph = new WikiGraph();
graph.init();
