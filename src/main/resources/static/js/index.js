var WikiGraph = {};

WikiGraph.WIKI_URL = 'http://en.wikipedia.org/w/index.php?title={{ title }}' +
                        '{{#raw}}&action=raw{{/raw}}';
WikiGraph.LINK_TEMPLATE = '<a {{#blank}}target="_blank"{{/blank}} href="{{ url }}">' +
                          '{{ text }}</a>';

WikiGraph.getDataForPage = function(page) {
  $('#links').html('<i class="icon-spinner icon-spin icon-4x"></i>');
  $.ajax('/page', {
    method: 'get',
    data: {page: page},
    success: function(data) {
      data = JSON.parse(data);
      var linkify = function(text) {
        var encodedUrl = encodeURIComponent(text);
        var url = Mustache.render(WikiGraph.WIKI_URL,
                                  {title: encodedUrl});
        var urlRaw = Mustache.render(WikiGraph.WIKI_URL,
                                     {title: encodedUrl,
                                      raw: true});
        var result = Mustache.render(WikiGraph.LINK_TEMPLATE,
                                     {url: url,
                                      text: text,
                                      blank: true});
        result += '-';
        result += Mustache.render(WikiGraph.LINK_TEMPLATE,
                                  {url: '#' + encodedUrl,
                                   text: '[Graph]'});
        result += ' ';
        result += Mustache.render(WikiGraph.LINK_TEMPLATE,
                                  {url: urlRaw, text: '[Raw]', blank: true});
        return result;
      };

      result = _.map(data, linkify).join('<br>');
      $('#links').html(result);
      var baseUrl = Mustache.render(WikiGraph.WIKI_URL,
                                    {title: encodeURIComponent(page),
                                     raw: true});
      var baseLink = Mustache.render(WikiGraph.LINK_TEMPLATE,
                                     {url: baseUrl, text: 'page', blank: true});
      $('#count-links').html(data.length);
      $('#label-page').html(baseLink);
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

  $(window).bind('hashchange', function() {
    WikiGraph.reloadHash();
  });
};

WikiGraph.init();
