var WikiGraph = {};

WikiGraph.generateWikiLink_ = function(title, raw) {
  var wikiUrl = 'http://en.wikipedia.org/w/index.php?title={{ title }}' +
                 '{{#raw}}&action=raw{{/raw}}';
  var context = {title: encodeURIComponent(title), raw: raw};
  return Mustache.render(wikiUrl, context);
};

WikiGraph.getDataForPage = function(page) {
  $('#links').html('<i class="icon-spinner icon-spin icon-4x"></i>');
  $.ajax('/page', {
    method: 'get',
    data: {page: page},
    success: function(data) {
      data = JSON.parse(data);
      var linkify = function(text) {
        var url = WikiGraph.generateWikiLink_(text);
        var urlRaw = WikiGraph.generateWikiLink_(text, true);
        var result = Util.generateLink(url, text, true);
        result += '-';
        result += Util.generateLink('#' + encodeURIComponent(text), '[Graph]');
        result += ' ';
        result += Util.generateLink(urlRaw, '[Raw]', true);
        return result;
      };

      result = _.map(data, linkify).join('<br>');
      $('#links').html(result);
      var baseUrl = WikiGraph.generateWikiLink_(page);
      var baseUrlRaw = WikiGraph.generateWikiLink_(page, true);
      var baseLink = Util.generateLink(baseUrl, page, true);
      var baseLinkRaw = Util.generateLink(baseUrlRaw, '[raw]', true);
      $('#count-links').html(data.length);
      $('#label-page').html(baseLink + ' ' + baseLinkRaw);
    },
    error: function() {
      $('#links').html('<h3>That page doesn\'t exist.</h3>');
      $('#count-links').html('0');
    }
  });
};

WikiGraph.reloadHash = function() {
  var page = decodeURIComponent(window.location.hash.split('#')[1]);
  $('#input-page').val(page);
  WikiGraph.getDataForPage(page);
};

WikiGraph.init = function() {
  $('#btn-refresh').click(function() {
    var page = $('#input-page').val();
    window.location.hash = encodeURIComponent(page);
    WikiGraph.getDataForPage(page);
  });

  $('#input-page').keypress(function(e) {
    // Enter key refreshes the links.
    if (e.which === 13) {
      e.preventDefault();
      $('#btn-refresh').click();
    }
  });

  if (window.location.hash) {
    WikiGraph.reloadHash();
  }

  $(window).on('hashchange', WikiGraph.reloadHash);
};

WikiGraph.init();
