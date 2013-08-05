function WikiGraph() {}

WikiGraph.prototype = new BaseGraph();

WikiGraph.prototype.generateWikiLink_ = function(title, raw) {
  var wikiUrl = 'http://en.wikipedia.org/w/index.php?title={{ title }}' +
                 '{{#raw}}&action=raw{{/raw}}';
  var context = {title: encodeURIComponent(title), raw: raw};
  return Mustache.render(wikiUrl, context);
};

WikiGraph.prototype.formatContentHeader_ = function() {
  var template = '{{ &baseLink }} {{ &baseLinkRaw }} - {{ count }} links found';
  var baseUrl = this.generateWikiLink_(this.page);
  var baseUrlRaw = this.generateWikiLink_(this.page, true);
  var baseLink = Util.generateLink(baseUrl, this.page, true);
  var baseLinkRaw = Util.generateLink(baseUrlRaw, '[raw]', true);
  var context = {
    baseLink: baseLink,
    baseLinkRaw: baseLinkRaw,
    count: this.data.length || 0
  };
  return Mustache.render(template, context);
};

WikiGraph.prototype.formatWikiLinks_ = function(data, page) {
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

  return _.map(data, linkify).join('<br>');
};

WikiGraph.prototype.showDataForPage = function(page) {
  var self = this;
  self.showLoading();
  var result;
  $.ajax('/page', {
    method: 'get',
    data: {page: this.page},
    success: function(data) {
      data = JSON.parse(data);
      this.data = data;
      var heading = self.formatContentHeader_();
      var content = self.formatWikiLinks_(data, page);
      self.render(heading, content);
    },
    error: function() {
      this.data = '';
      var heading = 'Page not found';
      var content = '<h3>That page doesn\'t exist.</h3>';
      self.render(heading, content);
    }
  });
};

WikiGraph.prototype.init = function() {
  BaseGraph.prototype.init.apply(this);
};

var graph = new WikiGraph();
graph.init();
