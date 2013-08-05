function BaseGraph() {}

BaseGraph.prototype.render = function(heading, content) {
  $('#content-heading').html(heading);
  $('#content-body').html(content);
};

BaseGraph.prototype.showLoading = function() {
  this.render('Loading', '<i class="icon-spinner icon-spin icon-4x"></i>');
};

BaseGraph.prototype.getDataForPage = function() {
  this.render('<h3>No content loaded</h3>', 'No links found.');
};

BaseGraph.prototype.reloadHash = function() {
  this.page = decodeURIComponent(window.location.hash.split('#')[1]);
  $('#input-page').val(this.page);
  this.showDataForPage();
};

BaseGraph.prototype.loadContent = function() {
  this.page = $('#input-page').val();
  window.location.hash = encodeURIComponent(this.page);
  this.showDataForPage();
};

BaseGraph.prototype.init = function() {
  var self = this;
  self.page = '';
  self.data = {};

  $('#btn-refresh').click(function() {
    self.loadContent();
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
