var WikiGraph = {};

WikiGraph.WIKI_URL_RAW = 'http://en.wikipedia.org/w/index.php?title={{ title }}&action=raw';
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
        var url = Mustache.render(WikiGraph.WIKI_URL_RAW,
                                  {title: encodeURIComponent(text)});
        var result = Mustache.render(WikiGraph.LINK_TEMPLATE,
                                     {url: url, text: text, blank: true});
        return result;
      };

      result = _.map(data, linkify).join('<br>');
      $('#links').html(result);
      var baseUrl = Mustache.render(WikiGraph.WIKI_URL_RAW,
                                    {title: encodeURIComponent(page)});
      var baseLink = Mustache.render(WikiGraph.LINK_TEMPLATE,
                                     {url: baseUrl, text: data.length, blank: true});
      console.log(baseLink);
      $('#count-links').html(baseLink);
    },
    error: function() {
      $('#links').html('<h3>That page doesn\'t exist.</h3>');
      $('#count-links').html('0');
    }
  });
};

WikiGraph.init = function() {
  $('#btn-refresh').click(function() {
    WikiGraph.getDataForPage($('#input-page').val());
  });

  $('#input-page').keypress(function(e) {
    // Enter key refreshes the links.
    if (e.which === 13) {
      e.preventDefault();
      $('#btn-refresh').click();
    }
  });
};

WikiGraph.init();
