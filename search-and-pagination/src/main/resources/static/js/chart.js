function render(chartObj) {
    var margin = chartObj.margin;

    const width = parseInt(chartObj.chart.node().offsetWidth) - margin.left - margin.right;
    const height = parseInt(width * 0.4) - margin.top - margin.bottom;

    chartObj.svg.attr('width', width + margin.left + margin.right)
            .attr('height', height + margin.top + margin.bottom);

    chartObj.x.range([0, width]);
    chartObj.y.range([height, 0]);

    chartObj.plot.select('.axis.x')
        .attr('transform', `translate(0, ${height})`)
        .call(chartObj.xAxis)
        .select('.domain').remove();

    chartObj.plot.select('.axis.y')
        .call(chartObj.yAxis)
        .call(g => g.select('.tick:last-of-type text').clone()
            .attr('x', 3)
            .attr('text-anchor', 'start')
            .attr('font-weight', 600)
            .text('$ billion'))
        .select('.domain').remove();

    chartObj.plot.select('.baseline')
        .attr('x1', 0)
        .attr('x2', width)
        .attr('y1', chartObj.y(0))
        .attr('y2', chartObj.y(0))
        .attr('fill', 'none')
        .attr('stroke', '#000')
        .attr('stroke-width', '1px')
        .attr('shape-rendering', 'crispEdges')
        .attr('stroke-dasharray', '3, 3')

    const path = chartObj.plot.selectAll('path')
        .attr('d', d => chartObj.line(d.values))
        .attr('stroke', d => chartObj.colour(d.name))
        .attr('opacity', d => d.name == 'Highlight' ? 1 : 0.5)
        .attr('id', (d, i) => `line-${d.name}`)

    path.each((d, i) => {
        const sel = d3.select(`#line-${d.name}`);
        const length = sel.node().getTotalLength();

        sel.attr('stroke-dasharray', `${length} ${length}`)
            .attr('stroke-dashoffset', length)
            .transition()
            .duration(5000)
            .attr('stroke-dashoffset', 0)
    })

    chartObj.plot.selectAll('.line-label')
        .attr('transform', d => {
            return `translate(${chartObj.x(d.value.date)}, ${chartObj.y(d.value.value)})`;
        })
        .attr('x', 5)
        .attr('dy', '.35em')
        .attr('fill', d => chartObj.colour(d.name))
        .attr('font-weight', d => d.name == 'Highlight' ? 700 : 400)
        .text(d => d.name)
        .attr('opacity', 0)
        .transition()
        .delay(4000)
        .duration(200)
        .attr('opacity', 1);


    <!--glow!!!!!!!!!! section!!!!-->
    var defs = chartObj.svg.append("defs");

    var filter = defs.append("filter")
        .attr("id","glow");

    filter.append("feGaussianBlur")
        .attr("class", "blur")
        .attr("stdDeviation","15")
        .attr("result","coloredBlur");

    var feMerge = filter.append("feMerge");
    feMerge.append("feMergeNode")
        .attr("in","coloredBlur");
    feMerge.append("feMergeNode")
        .attr("in","SourceGraphic");
    <!--glow!!!!!!!!!! section!!!!-->
}

function bindData(rawdata, chartObj) {
    // column headings, for each line
    const keys = rawdata.columns.filter(key => key != 'year');

    rawdata.forEach(d => {
        d.year = chartObj.parseDate(d.year);
    })

    const data = keys.map(name => {
        return {
            name,
            values: rawdata.map(d => {
                return {date: d.year, value: +d[name]};
            })
        }
    });

    chartObj.colour.domain(keys);

    // screen dimensions
    chartObj.x.domain(d3.extent(rawdata, d => d.year));
    chartObj.y.domain([
        d3.min(data, c => d3.min(c.values, v => v.value)) / 1.05,
        d3.max(data, c => d3.max(c.values, v => v.value)) * 1.1
    ]).nice();

    // bind data to DOM elements
    const $lines = chartObj.plot.append('g')
        .attr('class', 'lines glowed')
        .selectAll('.line')
        .data(data)
        .enter()
        .append('g')
        .attr('class', 'line')

    $lines.append('path')
        .attr('class', 'path glowed')

    d3.selectAll(".glowed").style("filter","url(#glow)");
    <!--glow!!!!!!!!!! section!!!!-->

    $lines.append('text')
        .datum(d => {
            return {
                name: d.name,
                value: d.values[d.values.length - 1]
            }
        })
        .attr('class', 'line-label')
        .attr('opacity', 0)

    chartObj.plot.append('g')
        .attr('class', 'axis x');

    chartObj.plot.append('g')
        .attr('class', 'axis y');

    chartObj.plot.append('line')
        .attr('class', 'baseline')

//    window.addEventListener('resize', debounce(render, 200));
    render(chartObj);
}

function initChart(chartObj) {
    d3.csv(chartObj.dataUrl).then(function callbackExecutor(rawdata) {bindData (rawdata, chartObj);});
}

function debounce(chartObj, func, wait, immediate){
    var chartObj;
    var timeout, args, context, timestamp, result;
    if (null == wait) wait = 100;

    function later() {
        var last = Date.now() - timestamp;

        if (last < wait && last >= 0) {
            timeout = setTimeout(later, wait - last);
        } else {
            timeout = null;
            if (!immediate) {
                result = func.apply(context, args);
                context = args = null;
            }
        }
    };

    var debounced = function(){
        context = this;
        args = arguments;
        timestamp = Date.now();
        var callNow = immediate && !timeout;
        if (!timeout) timeout = setTimeout(later, wait);
        if (callNow) {
            result = func.apply(context, args);
            context = args = null;
        }

        return result;
    };

    debounced.clear = function() {
        if (timeout) {
            clearTimeout(timeout);
            timeout = null;
        }
    };

    debounced.flush = function() {
        if (timeout) {
            result = func.apply(context, args);
            context = args = null;

            clearTimeout(timeout);
            timeout = null;
        }
    };

    return debounced;
};