d3.helper = {};

d3.helper.tooltip = function(){
    var tooltipDiv;
    var bodyNode = d3.select('body').node();
    var attrs = [];
    var text = "";
    var styles = [];

    function tooltip(selection) {

        selection.on("mouseover", function(d, i){
            var name, value;
            // Clean up lost tooltips
            d3.select('body').selectAll('div.tooltip').remove();
            // Append tooltip
            tooltipDiv = d3.select('body').append('div');
            for (var i in attrs) {
                var name = attrs[i][0];
                if (typeof attrs[i][1] === "function") {
                    value = attrs[i][1](d, i);
                } else value = attrs[i][1];
                if (name === "class") value += " tooltip";
                tooltipDiv.attr(name, value);
            }
            for (var i in styles) {
                name = styles[i][0];
                if (typeof styles[i][1] === "function") {
                    value = styles[i][1](d, i);
                } else value = styles[i][1];
                tooltipDiv.style(name, value);
            }
            var absoluteMousePos = d3.mouse(bodyNode);
            tooltipDiv.style('left', (absoluteMousePos[0] + 10)+'px')
                .style('top', (absoluteMousePos[1] - 15)+'px')
                .style('position', 'absolute')
                .style('z-index', 1001);
            // Add text using the accessor function
            var tooltipText = '';
            if (typeof text === "function") tooltipText = text(d, i);
            else if (typeof text != "undefined" || typeof text != null) tooltipText = text;
            // Crop text arbitrarily
            tooltipDiv.style('width', function(d, i){return (tooltipText.length > 80) ? '300px' : null;})
                .html(tooltipText);
        })
        .on('mousemove', function(d, i) {
            // Move tooltip
            var absoluteMousePos = d3.mouse(bodyNode);
            tooltipDiv.style('left', (absoluteMousePos[0] + 10)+'px')
                .style('top', (absoluteMousePos[1] - 15)+'px');
            var tooltipText = '';
            if (typeof text === "string") tooltipText = text;
            if (typeof text === "function") tooltipText = text(d, i);
            tooltipDiv.html(tooltipText);
        })
        .on("mouseout", function(d, i){
            // Remove tooltip
            tooltipDiv.remove();
        });

    }

    tooltip.attr = function(name, value) {
        attrs.push(arguments);
        return this;
    }

    tooltip.text = function(value) {
        text = value;
        return this;
    }

    tooltip.style = function(name, value) {
        styles.push(arguments);
        return this;
    }

    return tooltip;
};
