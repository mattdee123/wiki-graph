function BaseGraph() {}

BaseGraph.prototype.renderLinks = function(content) {
  $('#links').html(content);
};

BaseGraph.prototype.getDataForPage = function(page) {
  this.renderLinks('No links found.');
};

BaseGraph.prototype.reloadHash = function() {
  var page = decodeURIComponent(window.location.hash.split('#')[1]);
  $('#input-page').val(page);
  this.getDataForPage(page);
};

BaseGraph.prototype.init = function() {
  var self = this;

  $('#btn-refresh').click(function() {
    var page = $('#input-page').val();
    window.location.hash = encodeURIComponent(page);
    self.getDataForPage(page);
  });

  $('#input-page').keypress(function(e) {
    // Enter key refreshes the links.
    if (e.which === 13) {
      e.preventDefault();
      $('#btn-refresh').click();
    }
  });

  if (window.location.hash) {
    self.reloadHash();
  }

  $(window).on('hashchange', function() {
    self.reloadHash();
  });
};
