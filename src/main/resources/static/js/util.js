var Util = {};

Util.generateLink = function(url, text, blank) {
  var template = '<a {{#blank}}target="_blank"{{/blank}} href="{{ url }}">' +
                 '{{ text }}</a>';
  var context = {url: url, text: text, blank: blank};
  return Mustache.render(template, context);
};
