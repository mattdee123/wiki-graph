var WikiGraph = {};

WikiGraph.getDataForPage = function(page) {
  $('#links').html('<i class="icon-spinner icon-spin icon-4x"></i>');
  $.ajax('/page', {
    method: 'get',
    data: {page: page},
    success: function(data) {
      data = JSON.parse(data);
      result = data.join('<br>');
      $('#links').html(result);
      $('#count-links').html(data.length);
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
